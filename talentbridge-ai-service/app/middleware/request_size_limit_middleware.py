import json
import logging
import os

from datetime import datetime
from typing import Any

from dotenv import load_dotenv
from starlette.responses import JSONResponse

from app.middleware.observability_rate_limit_middleware import (
    normalize_correlation_id,
)


load_dotenv()

logger = logging.getLogger(
    "talentbridge.ai.request_size"
)

DEFAULT_MAX_BODY_BYTES = 65_536
PROTECTED_API_PREFIX = "/internal/api/v1"


class RequestBodyTooLargeError(Exception):
    """
    Raised when the request body exceeds the configured limit.
    """


def read_max_body_bytes() -> int:
    """
    Read the maximum request size from the environment.

    Invalid or non-positive values use the safe default.
    """

    raw_value = os.getenv(
        "AI_MAX_REQUEST_BODY_BYTES",
        str(DEFAULT_MAX_BODY_BYTES),
    )

    try:
        parsed_value = int(raw_value)
    except ValueError:
        logger.warning(
            "Invalid AI_MAX_REQUEST_BODY_BYTES value. "
            "Using default=%s.",
            DEFAULT_MAX_BODY_BYTES,
        )
        return DEFAULT_MAX_BODY_BYTES

    if parsed_value <= 0:
        logger.warning(
            "AI_MAX_REQUEST_BODY_BYTES must be positive. "
            "Using default=%s.",
            DEFAULT_MAX_BODY_BYTES,
        )
        return DEFAULT_MAX_BODY_BYTES

    return parsed_value


class RequestSizeLimitMiddleware:
    """
    Reject oversized requests before endpoint processing.

    The middleware checks Content-Length when available and also
    counts streamed request-body bytes.
    """

    def __init__(
        self,
        app: Any,
        max_body_bytes: int | None = None,
        protected_prefix: str = PROTECTED_API_PREFIX,
    ) -> None:
        self.app = app

        self.max_body_bytes = (
            max_body_bytes
            if max_body_bytes is not None
            else read_max_body_bytes()
        )

        if self.max_body_bytes <= 0:
            raise ValueError(
                "max_body_bytes must be greater than zero."
            )

        self.protected_prefix = protected_prefix

    async def __call__(
        self,
        scope: dict,
        receive: Any,
        send: Any,
    ) -> None:
        """
        Process one ASGI request.
        """

        if scope["type"] != "http":
            await self.app(scope, receive, send)
            return

        path = scope.get("path", "")

        if not path.startswith(self.protected_prefix):
            await self.app(scope, receive, send)
            return

        correlation_id = self._get_correlation_id(
            scope
        )

        content_length = self._get_content_length(
            scope
        )

        if (
            content_length is not None
            and content_length > self.max_body_bytes
        ):
            await self._send_too_large_response(
                scope=scope,
                receive=receive,
                send=send,
                correlation_id=correlation_id,
            )
            return

        received_bytes = 0

        async def limited_receive() -> dict:
            nonlocal received_bytes

            message = await receive()

            if message["type"] == "http.request":
                body = message.get("body", b"")

                received_bytes += len(body)

                if received_bytes > self.max_body_bytes:
                    raise RequestBodyTooLargeError()

            return message

        try:
            await self.app(
                scope,
                limited_receive,
                send,
            )

        except RequestBodyTooLargeError:
            await self._send_too_large_response(
                scope=scope,
                receive=receive,
                send=send,
                correlation_id=correlation_id,
            )

    @staticmethod
    def _get_content_length(
        scope: dict,
    ) -> int | None:
        """
        Read and validate the Content-Length header.
        """

        for header_name, header_value in scope.get(
            "headers",
            [],
        ):
            if header_name.lower() == b"content-length":
                try:
                    value = int(
                        header_value.decode("latin-1")
                    )
                except (ValueError, UnicodeDecodeError):
                    return None

                return max(value, 0)

        return None

    @staticmethod
    def _get_correlation_id(
        scope: dict,
    ) -> str:
        """
        Read a safe correlation ID from the request headers.
        """

        supplied_value: str | None = None

        for header_name, header_value in scope.get(
            "headers",
            [],
        ):
            if (
                header_name.lower()
                == b"x-correlation-id"
            ):
                try:
                    supplied_value = (
                        header_value.decode("latin-1")
                    )
                except UnicodeDecodeError:
                    supplied_value = None

                break

        return normalize_correlation_id(
            supplied_value
        )

    async def _send_too_large_response(
        self,
        scope: dict,
        receive: Any,
        send: Any,
        correlation_id: str,
    ) -> None:
        """
        Return a controlled HTTP 413 response.
        """

        logger.warning(
            "ai_request_body_rejected "
            "method=%s path=%s correlation_id=%s "
            "max_body_bytes=%s",
            scope.get("method", "-"),
            scope.get("path", "-"),
            correlation_id,
            self.max_body_bytes,
        )

        response = JSONResponse(
            status_code=413,
            content={
                "status": "ERROR",
                "errorCode": (
                    "AI_REQUEST_BODY_TOO_LARGE"
                ),
                "message": (
                    "Request body exceeds the "
                    "allowed size."
                ),
                "correlationId": correlation_id,
                "maxBodyBytes": self.max_body_bytes,
                "receivedAt": (
                    datetime.now()
                    .astimezone()
                    .isoformat()
                ),
            },
            headers={
                "X-Correlation-ID": correlation_id,
            },
        )

        await response(
            scope,
            receive,
            send,
        )