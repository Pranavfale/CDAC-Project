import hmac
import os
from collections.abc import Awaitable, Callable

from fastapi import Request, status
from fastapi.responses import JSONResponse, Response


INTERNAL_API_PREFIX = "/internal/api/v1/"
HEALTH_ENDPOINT = "/internal/health"
AI_SERVICE_KEY_HEADER = "X-AI-Service-Key"


def create_authentication_error() -> JSONResponse:
    """
    Create the controlled response returned when the caller does not
    provide the correct internal AI service key.
    """

    return JSONResponse(
        status_code=status.HTTP_401_UNAUTHORIZED,
        content={
            "status": "FAILED",
            "errorCode": "INTERNAL_API_KEY_INVALID",
            "message": "Internal service authentication failed.",
        },
    )


def create_configuration_error() -> JSONResponse:
    """
    Create a controlled response when the AI service itself has not
    been configured with an internal API key.
    """

    return JSONResponse(
        status_code=status.HTTP_503_SERVICE_UNAVAILABLE,
        content={
            "status": "FAILED",
            "errorCode": "AI_SERVICE_CONFIGURATION_ERROR",
            "message": "The AI service is not configured correctly.",
        },
    )


def keys_match(provided_key: str, expected_key: str) -> bool:
    """
    Compare the provided and expected keys without using a normal
    equality comparison.

    compare_digest reduces timing differences that could otherwise
    reveal information about a secret value.
    """

    if not provided_key or not expected_key:
        return False

    return hmac.compare_digest(
        provided_key.encode("utf-8"),
        expected_key.encode("utf-8"),
    )


async def internal_api_key_middleware(
    request: Request,
    call_next: Callable[[Request], Awaitable[Response]],
) -> Response:
    """
    Require X-AI-Service-Key for protected internal AI endpoints.

    The health endpoint remains available for local and Docker
    health monitoring. Swagger documentation also remains available
    during development.
    """

    request_path = request.url.path

    # Routes outside the protected internal AI API continue normally.
    if not request_path.startswith(INTERNAL_API_PREFIX):
        return await call_next(request)

    expected_key = os.getenv("AI_INTERNAL_API_KEY", "").strip()

    if not expected_key:
        return create_configuration_error()

    provided_key = request.headers.get(
        AI_SERVICE_KEY_HEADER,
        "",
    ).strip()

    if not keys_match(provided_key, expected_key):
        return create_authentication_error()

    return await call_next(request)