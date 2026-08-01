from collections.abc import Callable
from datetime import date, datetime

from pydantic import ValidationError

from app.providers.base_provider import AiProvider
from app.providers.provider_exceptions import ProviderResponseError
from app.providers.provider_factory import create_ai_provider
from app.schemas.offer_document_schema import OfferDocumentContent
from app.schemas.request_schemas import GenerateOfferRequest
from app.schemas.response_schemas import GenerateOfferSuccessResponse
from app.services.prompt_service import prompt_service


class AiOfferGenerationService:
    """
    Generates structured offer-letter content using the configured
    AI provider.

    The provider response is validated before being returned to
    Spring Boot.
    """

    def __init__(
        self,
        provider_factory: Callable[[], AiProvider] = create_ai_provider,
    ) -> None:
        self.provider_factory = provider_factory

    @staticmethod
    def normalize_text(value: str) -> str:
        """
        Normalize whitespace and letter case for safe comparisons.
        """

        return " ".join(value.split()).casefold()

    @classmethod
    def contains_text(
        cls,
        expected: str,
        actual: str,
    ) -> bool:
        """
        Check whether an approved fact is present in generated text.
        """

        return cls.normalize_text(expected) in cls.normalize_text(actual)

    @staticmethod
    def date_variants(value: date) -> list[str]:
        """
        Return acceptable representations of an approved date.
        """

        return [
            value.isoformat(),
            value.strftime("%d %B %Y"),
            value.strftime("%B %d, %Y"),
        ]

    @classmethod
    def contains_date(
        cls,
        approved_date: date,
        generated_text: str,
    ) -> bool:
        """
        Check whether generated text contains the approved date.
        """

        return any(
            cls.contains_text(date_value, generated_text)
            for date_value in cls.date_variants(approved_date)
        )

    @classmethod
    def validate_position_facts(
        cls,
        request: GenerateOfferRequest,
        content: OfferDocumentContent,
    ) -> None:
        """
        Ensure the provider did not modify approved position facts.
        """

        generated = content.positionDetails
        approved = request.position

        facts_match = all(
            [
                cls.normalize_text(generated.position)
                == cls.normalize_text(approved.title),
                cls.normalize_text(generated.department)
                == cls.normalize_text(approved.department),
                generated.employmentType.value
                == approved.employmentType.value,
                cls.normalize_text(generated.workLocation)
                == cls.normalize_text(approved.workLocation),
                generated.workMode.value == approved.workMode.value,
            ]
        )

        if not facts_match:
            raise ProviderResponseError()

    @classmethod
    def validate_document_facts(
        cls,
        request: GenerateOfferRequest,
        content: OfferDocumentContent,
    ) -> None:
        """
        Ensure critical approved facts remain present in the document.
        """

        if not cls.contains_text(
            request.candidate.name,
            content.salutation,
        ):
            raise ProviderResponseError()

        if not cls.contains_text(
            request.position.title,
            content.subject,
        ):
            raise ProviderResponseError()

        if not cls.contains_text(
            request.position.title,
            content.introduction,
        ):
            raise ProviderResponseError()

        if not cls.contains_text(
            request.company.name,
            content.introduction,
        ):
            raise ProviderResponseError()

        if not cls.contains_text(
            request.offer.offeredCtc,
            content.compensationSection,
        ):
            raise ProviderResponseError()

        if not cls.contains_date(
            request.offer.joiningDate,
            content.joiningSection,
        ):
            raise ProviderResponseError()

        if not cls.contains_date(
            request.offer.expiryDate,
            content.acceptanceSection,
        ):
            raise ProviderResponseError()

        if not cls.contains_text(
            request.company.name,
            content.closing,
        ):
            raise ProviderResponseError()

    @classmethod
    def validate_terms(
        cls,
        request: GenerateOfferRequest,
        content: OfferDocumentContent,
    ) -> None:
        """
        Reject benefits or terms that were not supplied by Spring Boot.
        """

        approved_terms = [
            *request.offer.additionalTerms,
            *request.offer.benefits,
        ]

        generated_terms = content.termsAndConditions

        if not approved_terms and generated_terms:
            raise ProviderResponseError()

        for generated_term in generated_terms:
            matches_approved_term = any(
                cls.contains_text(
                    approved_term,
                    generated_term,
                )
                for approved_term in approved_terms
            )

            if not matches_approved_term:
                raise ProviderResponseError()

        for approved_term in approved_terms:
            appears_in_output = any(
                cls.contains_text(
                    approved_term,
                    generated_term,
                )
                for generated_term in generated_terms
            )

            if not appears_in_output:
                raise ProviderResponseError()

    def generate(
        self,
        request: GenerateOfferRequest,
    ) -> GenerateOfferSuccessResponse:
        """
        Generate, parse, validate, and return offer content.
        """

        messages = prompt_service.build_generation_messages(
            request
        )

        provider = self.provider_factory()

        provider_result = provider.generate_json(messages)

        try:
            content = OfferDocumentContent.model_validate(
                provider_result.content
            )
        except ValidationError as error:
            raise ProviderResponseError() from error

        self.validate_position_facts(request, content)
        self.validate_document_facts(request, content)
        self.validate_terms(request, content)

        return GenerateOfferSuccessResponse(
            requestId=request.requestId,
            status="SUCCESS",
            provider=provider_result.provider,
            modelName=provider_result.model_name,
            promptVersion=request.promptVersion,
            content=content,
            missingFields=[],
            receivedAt=datetime.now().astimezone(),
        )


ai_offer_generation_service = AiOfferGenerationService()