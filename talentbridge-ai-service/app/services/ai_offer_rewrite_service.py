import re
from collections.abc import Callable
from datetime import datetime

from pydantic import ValidationError

from app.providers.base_provider import AiProvider
from app.providers.provider_exceptions import ProviderResponseError
from app.providers.provider_factory import create_ai_provider
from app.schemas.request_schemas import RewriteOfferRequest
from app.schemas.response_schemas import (
    RewriteOfferSuccessResponse,
    RewriteProviderContent,
)
from app.services.prompt_service import prompt_service


class AiOfferRewriteService:
    """
    Rewrites one offer-document section through the configured
    AI provider.

    The provider output is validated before it is returned to
    Spring Boot.
    """

    NUMBER_PATTERN = re.compile(
        r"\d[\d,]*(?:\.\d+)?"
    )

    def __init__(
        self,
        provider_factory: Callable[[], AiProvider] = (
            create_ai_provider
        ),
    ) -> None:
        self.provider_factory = provider_factory

    @staticmethod
    def normalize_text(value: str) -> str:
        """
        Normalize whitespace and letter case for comparisons.
        """

        return " ".join(value.split()).casefold()

    @classmethod
    def contains_text(
        cls,
        expected: str,
        actual: str,
    ) -> bool:
        """
        Return True when the expected verified fact appears
        inside the generated content.
        """

        return cls.normalize_text(expected) in (
            cls.normalize_text(actual)
        )

    @classmethod
    def extract_number_tokens(
        cls,
        value: str,
    ) -> set[str]:
        """
        Extract numeric values from text.

        Commas are removed so that values such as 8,00,000 and
        800000 are treated as the same numeric fact.
        """

        matches = cls.NUMBER_PATTERN.findall(value)

        return {
            match.replace(",", "")
            for match in matches
        }

    @classmethod
    def validate_numeric_facts(
        cls,
        current_content: str,
        rewritten_content: str,
    ) -> None:
        """
        Ensure numeric facts are neither removed nor invented.

        This helps prevent accidental changes to compensation,
        dates, percentages, durations, and other numeric values.
        """

        current_numbers = cls.extract_number_tokens(
            current_content
        )

        rewritten_numbers = cls.extract_number_tokens(
            rewritten_content
        )

        if current_numbers != rewritten_numbers:
            raise ProviderResponseError()

    @classmethod
    def validate_existing_verified_facts(
        cls,
        request: RewriteOfferRequest,
        rewritten_content: str,
    ) -> None:
        """
        Preserve verified facts already present in the current
        content.
        """

        verified_facts = [
            request.facts.candidateName,
            request.facts.position,
            request.facts.companyName,
        ]

        for fact in verified_facts:
            fact_exists_in_current_content = cls.contains_text(
                fact,
                request.currentContent,
            )

            fact_exists_in_rewritten_content = cls.contains_text(
                fact,
                rewritten_content,
            )

            if (
                fact_exists_in_current_content
                and not fact_exists_in_rewritten_content
            ):
                raise ProviderResponseError()

    @classmethod
    def validate_section_requirements(
        cls,
        request: RewriteOfferRequest,
        rewritten_content: str,
    ) -> None:
        """
        Require important verified facts for selected sections.
        """

        required_facts_by_section = {
            "salutation": [
                request.facts.candidateName,
            ],
            "subject": [
                request.facts.position,
            ],
            "introduction": [
                request.facts.position,
                request.facts.companyName,
            ],
            "closing": [
                request.facts.companyName,
            ],
        }

        required_facts = required_facts_by_section.get(
            request.section,
            [],
        )

        for fact in required_facts:
            if not cls.contains_text(
                fact,
                rewritten_content,
            ):
                raise ProviderResponseError()

    @classmethod
    def validate_rewritten_content(
        cls,
        request: RewriteOfferRequest,
        rewritten_content: str,
    ) -> None:
        """
        Apply all safety checks to rewritten provider content.
        """

        cls.validate_numeric_facts(
            current_content=request.currentContent,
            rewritten_content=rewritten_content,
        )

        cls.validate_existing_verified_facts(
            request=request,
            rewritten_content=rewritten_content,
        )

        cls.validate_section_requirements(
            request=request,
            rewritten_content=rewritten_content,
        )

    def rewrite(
        self,
        request: RewriteOfferRequest,
    ) -> RewriteOfferSuccessResponse:
        """
        Build the rewrite prompt, call the provider, validate its
        response, and create the API response.
        """

        messages = prompt_service.build_rewrite_messages(
            request
        )

        provider = self.provider_factory()

        provider_result = provider.generate_json(messages)

        try:
            provider_content = (
                RewriteProviderContent.model_validate(
                    provider_result.content
                )
            )
        except ValidationError as error:
            raise ProviderResponseError() from error

        rewritten_content = (
            provider_content.rewrittenContent
        )

        self.validate_rewritten_content(
            request=request,
            rewritten_content=rewritten_content,
        )

        return RewriteOfferSuccessResponse(
            requestId=request.requestId,
            status="SUCCESS",
            provider=provider_result.provider,
            modelName=provider_result.model_name,
            section=request.section,
            rewrittenContent=rewritten_content,
            receivedAt=datetime.now().astimezone(),
        )


ai_offer_rewrite_service = AiOfferRewriteService()