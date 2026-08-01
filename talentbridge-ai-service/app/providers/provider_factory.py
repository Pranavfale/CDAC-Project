import os

from app.providers.base_provider import AiProvider
from app.providers.groq_provider import GroqAiProvider
from app.providers.provider_exceptions import (
    ProviderConfigurationError,
)


def create_ai_provider() -> AiProvider:
    """
    Create the configured AI-provider implementation.

    The provider is created only when generation is requested.
    This allows the health endpoint and mock endpoint to run even
    when a real provider key is not configured.
    """

    provider_name = os.getenv(
        "LLM_PROVIDER",
        "groq",
    ).strip().lower()

    if provider_name == "groq":
        return GroqAiProvider()

    raise ProviderConfigurationError(
        f"Unsupported AI provider: {provider_name}"
    )