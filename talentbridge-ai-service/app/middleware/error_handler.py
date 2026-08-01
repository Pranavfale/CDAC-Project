from typing import Any

from fastapi import Request, status
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse

from app.providers.provider_exceptions import (
    AiProviderError,
    ProviderAuthenticationError,
    ProviderConfigurationError,
    ProviderRateLimitError,
    ProviderTimeoutError,
)
from app.schemas.response_schemas import AiErrorResponse


def build_field_path(location: tuple[Any, ...]) -> str:
    """
    Convert Pydantic's error location into a readable field path.

    Example:
    ('body', 'offer', 'offeredCtc')
    becomes:
    offer.offeredCtc
    """

    parts = [
        str(part)
        for part in location
        if part != "body"
    ]

    return ".".join(parts)


async def request_validation_exception_handler(
    request: Request,
    exception: RequestValidationError,
) -> JSONResponse:
    """
    Convert FastAPI's default 422 validation response into the
    controlled TalentBridge AI validation-error format.
    """

    request_id = None

    if isinstance(exception.body, dict):
        request_id = exception.body.get("requestId")

    missing_fields: list[str] = []

    for error in exception.errors():
        if error.get("type") == "missing":
            field_path = build_field_path(
                error.get("loc", tuple())
            )

            if field_path:
                missing_fields.append(field_path)

    error_response = AiErrorResponse(
        requestId=request_id,
        status="FAILED",
        errorCode="AI_REQUEST_VALIDATION_FAILED",
        message="Required AI request data is missing or invalid.",
        missingFields=missing_fields,
        retryable=False,
    )

    return JSONResponse(
        status_code=status.HTTP_400_BAD_REQUEST,
        content=error_response.model_dump(
            mode="json",
            exclude_none=True,
        ),
    )


def get_provider_error_status(
    exception: AiProviderError,
) -> int:
    """
    Map controlled provider errors to HTTP statuses.
    """

    if isinstance(exception, ProviderTimeoutError):
        return status.HTTP_504_GATEWAY_TIMEOUT

    if isinstance(
        exception,
        (
            ProviderConfigurationError,
            ProviderAuthenticationError,
            ProviderRateLimitError,
        ),
    ):
        return status.HTTP_503_SERVICE_UNAVAILABLE

    return status.HTTP_502_BAD_GATEWAY


async def ai_provider_exception_handler(
    request: Request,
    exception: AiProviderError,
) -> JSONResponse:
    """
    Convert provider failures into safe TalentBridge responses.
    """

    request_id = getattr(
        request.state,
        "ai_request_id",
        None,
    )

    error_response = AiErrorResponse(
        requestId=request_id,
        status="FAILED",
        errorCode=exception.error_code,
        message=str(exception),
        missingFields=[],
        retryable=exception.retryable,
    )

    return JSONResponse(
        status_code=get_provider_error_status(exception),
        content=error_response.model_dump(
            mode="json",
            exclude_none=True,
        ),
    )