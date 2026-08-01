import hmac
import os

from datetime import datetime

from dotenv import load_dotenv
from fastapi import Request
from starlette.middleware.base import (
    BaseHTTPMiddleware,
    RequestResponseEndpoint,
)
from starlette.responses import JSONResponse, Response


load_dotenv()


PROTECTED_API_PREFIX = "/internal/api/v1"
INTERNAL_API_KEY_HEADER = "X-AI-Service-Key"


class InternalApiKeyMiddleware(BaseHTTPMiddleware):
    """
    Protect internal AI endpoints using a shared service key.

    The health endpoint and documentation routes remain available
    without the internal API key.
    """

    def __init__(
        self,
        app,
        protected_prefix: str = PROTECTED_API_PREFIX,
    ) -> None:
        super().__init__(app)

        self.protected_prefix = protected_prefix

    async def dispatch(
        self,
        request: Request,
        call_next: RequestResponseEndpoint,
    ) -> Response:
        """
        Validate the internal API key before processing protected
        requests.
        """

        if not request.url.path.startswith(
            self.protected_prefix
        ):
            return await call_next(request)

        configured_key = (
            os.getenv("AI_INTERNAL_API_KEY")
            or os.getenv("INTERNAL_API_KEY")
        )

        if not configured_key:
            return JSONResponse(
                status_code=503,
                content={
                    "status": "ERROR",
                    "errorCode": (
                        "INTERNAL_API_KEY_NOT_CONFIGURED"
                    ),
                    "message": (
                        "The internal AI service key is "
                        "not configured."
                    ),
                    "receivedAt": (
                        datetime.now()
                        .astimezone()
                        .isoformat()
                    ),
                },
            )

        supplied_key = request.headers.get(
            INTERNAL_API_KEY_HEADER
        )

        if (
            supplied_key is None
            or not hmac.compare_digest(
                supplied_key,
                configured_key,
            )
        ):
            return JSONResponse(
                status_code=401,
                content={
                    "status": "ERROR",
                    "errorCode": (
                        "INTERNAL_API_KEY_INVALID"
                    ),
                    "message": (
                        "A valid internal AI service key "
                        "is required."
                    ),
                    "receivedAt": (
                        datetime.now()
                        .astimezone()
                        .isoformat()
                    ),
                },
                headers={
                    "WWW-Authenticate": (
                        INTERNAL_API_KEY_HEADER
                    ),
                },
            )

        return await call_next(request)