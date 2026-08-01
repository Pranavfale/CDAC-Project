from fastapi import APIRouter, Request, status

from app.schemas.request_schemas import (
    GenerateOfferRequest,
    RewriteOfferRequest,
)
from app.schemas.response_schemas import (
    GenerateOfferSuccessResponse,
    RewriteOfferSuccessResponse,
)
from app.services.ai_offer_generation_service import (
    ai_offer_generation_service,
)
from app.services.ai_offer_rewrite_service import (
    ai_offer_rewrite_service,
)


router = APIRouter(
    prefix="/internal/api/v1/offers",
    tags=["Internal Offer Generation"],
)


@router.post(
    "/generate",
    response_model=GenerateOfferSuccessResponse,
    status_code=status.HTTP_200_OK,
    summary="Generate structured offer content",
)
def generate_offer(
    payload: GenerateOfferRequest,
    http_request: Request,
) -> GenerateOfferSuccessResponse:
    """
    Generate complete structured offer content through the
    configured AI provider.
    """

    http_request.state.ai_request_id = payload.requestId

    return ai_offer_generation_service.generate(payload)


@router.post(
    "/rewrite",
    response_model=RewriteOfferSuccessResponse,
    status_code=status.HTTP_200_OK,
    summary="Rewrite one offer-document section",
)
def rewrite_offer_section(
    payload: RewriteOfferRequest,
    http_request: Request,
) -> RewriteOfferSuccessResponse:
    """
    Rewrite one selected offer-document section through the
    configured AI provider.

    This endpoint is protected by X-AI-Service-Key middleware.
    """

    http_request.state.ai_request_id = payload.requestId

    return ai_offer_rewrite_service.rewrite(payload)