from fastapi import APIRouter, Request, status

from app.schemas.request_schemas import GenerateOfferRequest
from app.schemas.response_schemas import (
    GenerateOfferSuccessResponse,
)
from app.services.ai_offer_generation_service import (
    ai_offer_generation_service,
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
    Generate structured offer content through the configured provider.

    This endpoint is protected by X-AI-Service-Key middleware.
    """

    http_request.state.ai_request_id = payload.requestId

    return ai_offer_generation_service.generate(payload)