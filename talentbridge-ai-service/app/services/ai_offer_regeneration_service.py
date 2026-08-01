from collections.abc import Callable
from datetime import datetime

from pydantic import ValidationError

from app.providers.base_provider import AiProvider
from app.providers.provider_exceptions import (
    ProviderResponseError,
)
from app.providers.provider_factory import create_ai_provider
from app.schemas.offer_document_schema import (
    OfferDocumentContent,
)
from app.schemas.regenerate_schemas import (
    RegenerateOfferRequest,
)
from app.schemas.response_schemas import (
    GenerateOfferSuccessResponse,
)
from app.services.ai_offer_generation_service import (
    AiOfferGenerationService,
)
from app.services.regenerate_prompt_service import (
    regenerate_prompt_service,
)


class AiOfferRegenerationService:
    """
    Generates a complete replacement offer draft using the
    configured AI provider.

    Provider output is validated against the structured offer
    schema and against the approved facts supplied by Spring Boot.
    """

    def __init__(
        self,
        provider_factory: Callable[[], AiProvider] = (
            create_ai_provider
        ),
    ) -> None:
        self.provider_factory = provider_factory

    def regenerate(
        self,
        request: RegenerateOfferRequest,
    ) -> GenerateOfferSuccessResponse:
        """
        Build the controlled prompt, call the provider, validate
        the response, and return the structured result.
        """

        messages = (
            regenerate_prompt_service
            .build_regeneration_messages(request)
        )

        provider = self.provider_factory()

        provider_result = provider.generate_json(messages)

        try:
            content = OfferDocumentContent.model_validate(
                provider_result.content
            )
        except ValidationError as error:
            raise ProviderResponseError() from error

        # Reuse the fact-preservation checks already used for
        # complete offer generation.
        AiOfferGenerationService.validate_position_facts(
            request=request,
            content=content,
        )

        AiOfferGenerationService.validate_document_facts(
            request=request,
            content=content,
        )

        AiOfferGenerationService.validate_terms(
            request=request,
            content=content,
        )

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


ai_offer_regeneration_service = (
    AiOfferRegenerationService()
)