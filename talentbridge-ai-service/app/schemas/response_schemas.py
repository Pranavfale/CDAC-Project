from datetime import datetime
from typing import Literal

from pydantic import BaseModel, ConfigDict, Field

from app.schemas.offer_document_schema import OfferDocumentContent


class ApiResponseModel(BaseModel):
    """
    Base class for responses returned by the AI service.
    """

    model_config = ConfigDict(
        extra="forbid",
        str_strip_whitespace=True,
    )


class HealthResponse(ApiResponseModel):
    """
    Structured response returned by the AI service health endpoint.
    """

    service: str = Field(
        min_length=1,
        description="Internal name of the running service.",
    )

    status: Literal["UP"] = Field(
        description="Current health status of the service.",
    )

    port: int = Field(
        gt=0,
        le=65535,
        description="Port on which the service is running.",
    )

    providerConfigured: bool = Field(
        description="Whether an AI provider key is configured.",
    )

    timestamp: datetime = Field(
        description="Current server date and time.",
    )


class GenerateOfferSuccessResponse(ApiResponseModel):
    """
    Successful structured response returned by offer generation.
    """

    requestId: str = Field(
        min_length=1,
        description="AI request identifier supplied by Spring Boot.",
    )

    status: Literal["SUCCESS"] = Field(
        description="Successful AI generation status.",
    )

    provider: str = Field(
        min_length=1,
        description="Configured AI provider.",
    )

    modelName: str = Field(
        min_length=1,
        description="Provider model used for generation.",
    )

    promptVersion: str = Field(
        min_length=1,
        description="Controlled prompt version used for generation.",
    )

    content: OfferDocumentContent

    missingFields: list[str] = Field(
        default_factory=list,
        description=(
            "Required business fields that were unavailable. "
            "The AI must not guess these values."
        ),
    )

    receivedAt: datetime = Field(
        description="Date and time when the response was produced.",
    )


class AiErrorResponse(ApiResponseModel):
    """
    Controlled error response returned by the AI service.
    """

    requestId: str | None = Field(
        default=None,
        description="AI request identifier when available.",
    )

    status: Literal["FAILED"] = Field(
        description="Failed request status.",
    )

    errorCode: str = Field(
        min_length=1,
        description="Stable machine-readable error code.",
    )

    message: str = Field(
        min_length=1,
        description="Safe user-readable error message.",
    )

    missingFields: list[str] = Field(
        default_factory=list,
        description="Missing required business fields.",
    )

    retryable: bool = Field(
        default=False,
        description="Whether Spring Boot may retry the operation.",
    )