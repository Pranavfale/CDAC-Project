from datetime import datetime
from typing import Literal

from pydantic import BaseModel, ConfigDict, Field

from app.schemas.offer_document_schema import OfferDocumentContent


class ApiResponseModel(BaseModel):
    """
    Base model for responses returned by the TalentBridge AI service.
    """

    model_config = ConfigDict(
        extra="forbid",
        str_strip_whitespace=True,
    )


class HealthResponse(ApiResponseModel):
    """
    Response returned by the AI-service health endpoint.
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
        description="Whether an AI-provider API key is configured.",
    )

    timestamp: datetime = Field(
        description="Current server date and time.",
    )


class GenerateOfferSuccessResponse(ApiResponseModel):
    """
    Successful response returned by the offer-generation endpoint.
    """

    requestId: str = Field(
        min_length=1,
        description="AI request identifier supplied by Spring Boot.",
    )

    status: Literal["SUCCESS"] = Field(
        description="Successful AI-generation status.",
    )

    provider: str = Field(
        min_length=1,
        description="AI provider used for generation.",
    )

    modelName: str = Field(
        min_length=1,
        description="Provider model used for generation.",
    )

    promptVersion: str = Field(
        min_length=1,
        description="Controlled prompt version used for generation.",
    )

    content: OfferDocumentContent = Field(
        description="Validated structured offer-document content.",
    )

    missingFields: list[str] = Field(
        default_factory=list,
        description=(
            "Required business fields that were unavailable. "
            "The AI service must not guess these values."
        ),
    )

    receivedAt: datetime = Field(
        description="Time when the generation response was created.",
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
        description="Safe error message.",
    )

    missingFields: list[str] = Field(
        default_factory=list,
        description="Missing required business fields.",
    )

    retryable: bool = Field(
        default=False,
        description="Whether Spring Boot may retry the operation.",
    )


class RewriteProviderContent(ApiResponseModel):
    """
    JSON content expected directly from the AI provider
    for a section-rewrite operation.
    """

    rewrittenContent: str = Field(
        min_length=1,
        max_length=10000,
        description="Rewritten content for the requested section.",
    )


class RewriteOfferSuccessResponse(ApiResponseModel):
    """
    Successful response returned by the offer-rewrite endpoint.
    """

    requestId: str = Field(
        min_length=1,
        description="Rewrite request identifier.",
    )

    status: Literal["SUCCESS"] = Field(
        description="Successful rewrite status.",
    )

    provider: str = Field(
        min_length=1,
        description="AI provider used for rewriting.",
    )

    modelName: str = Field(
        min_length=1,
        description="Provider model used for rewriting.",
    )

    section: str = Field(
        min_length=1,
        description="Offer-document section that was rewritten.",
    )

    rewrittenContent: str = Field(
        min_length=1,
        max_length=10000,
        description="Validated rewritten section content.",
    )

    receivedAt: datetime = Field(
        description="Time when the rewrite response was created.",
    )