import hmac
import logging
import os

from datetime import datetime

from dotenv import load_dotenv
from fastapi import FastAPI, Request
from fastapi.exceptions import RequestValidationError
from starlette.middleware.base import (
    BaseHTTPMiddleware,
    RequestResponseEndpoint,
)
from starlette.responses import JSONResponse, Response

from app.api.health_routes import router as health_router
from app.api.offer_routes import router as offer_router
from app.middleware.observability_rate_limit_middleware import (
    ObservabilityRateLimitMiddleware,
)


load_dotenv()


logging.basicConfig(
    level=logging.INFO,
    format=(
        "%(asctime)s %(levelname)s "
        "%(name)s %(message)s"
    ),
)


logger = logging.getLogger(
    "talentbridge.ai"
)


INTERNAL_API_PREFIX = "/internal/api/v1"
INTERNAL_API_KEY_HEADER = "X-AI-Service-Key"


class InternalApiKeyMiddleware(
    BaseHTTPMiddleware
):
    """
    Protect internal AI endpoints using a shared service key.

    The health endpoint and API documentation remain available
    without the internal key.
    """

    async def dispatch(
        self,
        request: Request,
        call_next: RequestResponseEndpoint,
    ) -> Response:
        """
        Validate the internal API key for protected routes.
        """

        if not request.url.path.startswith(
            INTERNAL_API_PREFIX
        ):
            return await call_next(request)

        configured_key = os.getenv(
            "AI_INTERNAL_API_KEY",
            "",
        ).strip()

        correlation_id = getattr(
            request.state,
            "correlation_id",
            None,
        )

        if not configured_key:
            return JSONResponse(
                status_code=503,
                content={
                    "status": "ERROR",
                    "errorCode": (
                        "AI_INTERNAL_API_KEY_NOT_CONFIGURED"
                    ),
                    "message": (
                        "The internal AI service key "
                        "is not configured."
                    ),
                    "correlationId": correlation_id,
                    "receivedAt": (
                        datetime.now()
                        .astimezone()
                        .isoformat()
                    ),
                },
            )

        supplied_key = request.headers.get(
            INTERNAL_API_KEY_HEADER,
            "",
        )

        key_is_valid = hmac.compare_digest(
            supplied_key,
            configured_key,
        )

        if not key_is_valid:
            return JSONResponse(
                status_code=401,
                content={
                    "status": "ERROR",
                    "errorCode": (
                        "INTERNAL_API_KEY_INVALID"
                    ),
                    "message": (
                        "A valid internal AI service "
                        "key is required."
                    ),
                    "correlationId": correlation_id,
                    "receivedAt": (
                        datetime.now()
                        .astimezone()
                        .isoformat()
                    ),
                },
            )

        return await call_next(request)


app = FastAPI(
    title="TalentBridge AI Service",
    description=(
        "Internal TalentBridge service for generating, "
        "rewriting, and regenerating structured offer-letter "
        "content."
    ),
    version="1.0.0",
    docs_url="/docs",
    redoc_url="/redoc",
    openapi_url="/openapi.json",
)


# Add authentication first.
#
# Starlette makes the middleware added last the outer layer.
# Therefore, observability is added afterward so correlation IDs,
# rate-limit headers, and safe logs also apply to authentication
# failures.
app.add_middleware(
    InternalApiKeyMiddleware
)

app.add_middleware(
    ObservabilityRateLimitMiddleware
)


app.include_router(
    health_router
)

app.include_router(
    offer_router
)


@app.exception_handler(
    RequestValidationError
)
async def request_validation_exception_handler(
    request: Request,
    exception: RequestValidationError,
) -> JSONResponse:
    """
    Convert Pydantic and FastAPI request validation failures
    into a controlled error response.
    """

    correlation_id = getattr(
        request.state,
        "correlation_id",
        None,
    )

    request_id = getattr(
        request.state,
        "ai_request_id",
        None,
    )

    validation_errors = []

    for error in exception.errors():
        location = ".".join(
            str(value)
            for value in error.get(
                "loc",
                [],
            )
            if value != "body"
        )

        validation_errors.append(
            {
                "field": location or "request",
                "message": error.get(
                    "msg",
                    "Invalid value.",
                ),
                "type": error.get(
                    "type",
                    "validation_error",
                ),
            }
        )

    return JSONResponse(
        status_code=400,
        content={
            "requestId": request_id,
            "status": "ERROR",
            "errorCode": (
                "AI_REQUEST_VALIDATION_FAILED"
            ),
            "message": (
                "The AI service request is invalid."
            ),
            "validationErrors": validation_errors,
            "correlationId": correlation_id,
            "receivedAt": (
                datetime.now()
                .astimezone()
                .isoformat()
            ),
        },
    )


@app.exception_handler(
    Exception
)
async def general_exception_handler(
    request: Request,
    exception: Exception,
) -> JSONResponse:
    """
    Convert controlled application exceptions into JSON and
    prevent internal implementation details from being exposed.
    """

    correlation_id = getattr(
        request.state,
        "correlation_id",
        None,
    )

    request_id = getattr(
        request.state,
        "ai_request_id",
        None,
    )

    error_code = getattr(
        exception,
        "error_code",
        "AI_INTERNAL_ERROR",
    )

    configured_status_code = getattr(
        exception,
        "status_code",
        500,
    )

    if not isinstance(
        configured_status_code,
        int,
    ):
        configured_status_code = 500

    safe_message = getattr(
        exception,
        "message",
        None,
    )

    if not safe_message:
        if configured_status_code < 500:
            safe_message = str(exception)
        else:
            safe_message = (
                "The AI service could not complete "
                "the request."
            )

    logger.exception(
        "ai_unhandled_exception "
        "method=%s path=%s status=%s "
        "correlation_id=%s request_id=%s "
        "error_type=%s",
        request.method,
        request.url.path,
        configured_status_code,
        correlation_id,
        request_id,
        type(exception).__name__,
    )

    return JSONResponse(
        status_code=configured_status_code,
        content={
            "requestId": request_id,
            "status": "ERROR",
            "errorCode": error_code,
            "message": safe_message,
            "correlationId": correlation_id,
            "receivedAt": (
                datetime.now()
                .astimezone()
                .isoformat()
            ),
        },
    )


@app.get(
    "/",
    tags=["Service Information"],
)
def service_information() -> dict[str, str]:
    """
    Return basic service information.
    """

    return {
        "service": "TalentBridge AI Service",
        "status": "RUNNING",
        "documentation": "/docs",
        "health": "/internal/health",
    }