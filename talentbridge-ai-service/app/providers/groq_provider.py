import json
import os
from typing import Any

import groq
from groq import Groq

from app.providers.base_provider import (
    AiProvider,
    ProviderResult,
)
from app.providers.provider_exceptions import (
    ProviderApiError,
    ProviderAuthenticationError,
    ProviderConfigurationError,
    ProviderConnectionError,
    ProviderRateLimitError,
    ProviderResponseError,
    ProviderTimeoutError,
)
from app.services.prompt_service import PromptMessage


def read_required_environment_value(name: str) -> str:
    """
    Read a required non-empty environment variable.
    """

    value = os.getenv(name, "").strip()

    if not value:
        raise ProviderConfigurationError(
            f"Required configuration '{name}' is missing."
        )

    return value


def read_integer_environment_value(
    name: str,
    default: int,
    minimum: int,
) -> int:
    """
    Read and validate an integer environment variable.
    """

    raw_value = os.getenv(name, str(default)).strip()

    try:
        value = int(raw_value)
    except ValueError as error:
        raise ProviderConfigurationError(
            f"Configuration '{name}' must be an integer."
        ) from error

    if value < minimum:
        raise ProviderConfigurationError(
            f"Configuration '{name}' must be at least {minimum}."
        )

    return value


def read_float_environment_value(
    name: str,
    default: float,
    minimum: float,
    maximum: float,
) -> float:
    """
    Read and validate a floating-point environment variable.
    """

    raw_value = os.getenv(name, str(default)).strip()

    try:
        value = float(raw_value)
    except ValueError as error:
        raise ProviderConfigurationError(
            f"Configuration '{name}' must be numeric."
        ) from error

    if value < minimum or value > maximum:
        raise ProviderConfigurationError(
            (
                f"Configuration '{name}' must be between "
                f"{minimum} and {maximum}."
            )
        )

    return value


class GroqAiProvider(AiProvider):
    """
    Groq implementation of the provider-independent AI interface.
    """

    PROVIDER_NAME = "groq"

    def __init__(self) -> None:
        self.api_key = read_required_environment_value(
            "GROQ_API_KEY"
        )

        self.model_name = read_required_environment_value(
            "LLM_MODEL"
        )

        self.timeout_seconds = read_float_environment_value(
            name="LLM_TIMEOUT_SECONDS",
            default=20.0,
            minimum=1.0,
            maximum=120.0,
        )

        self.max_retries = read_integer_environment_value(
            name="LLM_MAX_RETRIES",
            default=1,
            minimum=0,
        )

        self.temperature = read_float_environment_value(
            name="LLM_TEMPERATURE",
            default=0.2,
            minimum=0.0,
            maximum=1.0,
        )

        self.max_completion_tokens = (
            read_integer_environment_value(
                name="LLM_MAX_COMPLETION_TOKENS",
                default=1800,
                minimum=1,
            )
        )

        self.client = Groq(
            api_key=self.api_key,
            timeout=self.timeout_seconds,
            max_retries=self.max_retries,
        )

    @staticmethod
    def parse_json_object(content: str) -> dict[str, Any]:
        """
        Parse provider content and require a JSON object.

        JSON arrays, plain text, empty content, and invalid JSON
        are rejected.
        """

        if not content.strip():
            raise ProviderResponseError()

        try:
            parsed_content = json.loads(content)
        except json.JSONDecodeError as error:
            raise ProviderResponseError() from error

        if not isinstance(parsed_content, dict):
            raise ProviderResponseError()

        return parsed_content

    def generate_json(
        self,
        messages: list[PromptMessage],
    ) -> ProviderResult:
        """
        Send controlled messages to Groq and return parsed JSON.
        """

        try:
            response = self.client.chat.completions.create(
                model=self.model_name,
                messages=messages,
                temperature=self.temperature,
                max_completion_tokens=(
                    self.max_completion_tokens
                ),
                response_format={
                    "type": "json_object",
                },
                stream=False,
            )

            if not response.choices:
                raise ProviderResponseError()

            raw_content = (
                response.choices[0].message.content or ""
            )

            parsed_content = self.parse_json_object(
                raw_content
            )

            return ProviderResult(
                provider=self.PROVIDER_NAME,
                model_name=self.model_name,
                content=parsed_content,
            )

        except ProviderResponseError:
            raise

        except groq.AuthenticationError as error:
            raise ProviderAuthenticationError() from error

        except groq.APITimeoutError as error:
            raise ProviderTimeoutError() from error

        except groq.RateLimitError as error:
            raise ProviderRateLimitError() from error

        except groq.APIConnectionError as error:
            raise ProviderConnectionError() from error

        except groq.APIStatusError as error:
            retryable = error.status_code >= 500

            raise ProviderApiError(
                retryable=retryable,
            ) from error