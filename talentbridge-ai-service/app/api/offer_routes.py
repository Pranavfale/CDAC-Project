from fastapi import APIRouter, status

from app.schemas.request_schemas import GenerateOfferRequest
from app.schemas.response_schemas import (
    GenerateOfferSuccessResponse,
)
from app.services.offer_generation_service import (
    mock_offer_generation_service,
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
    request: GenerateOfferRequest,
) -> GenerateOfferSuccessResponse:
    """
    Generate structured mock offer-letter content.

    The endpoint is protected by X-AI-Service-Key middleware.
    """

    return mock_offer_generation_service.generate(request)