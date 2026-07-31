from typing import Any

from fastapi import Request, status
from fastapi.exceptions import RequestValidationError
from fastapi.responses import JSONResponse

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

    missing_fields = []

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