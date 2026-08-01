import logging
import math
import os
import re
import time
import uuid

from collections import defaultdict, deque
from collections.abc import Callable
from dataclasses import dataclass
from datetime import datetime
from threading import Lock

from dotenv import load_dotenv
from fastapi import Request
from starlette.middleware.base import (
    BaseHTTPMiddleware,
    RequestResponseEndpoint,
)
from starlette.responses import JSONResponse, Response


load_dotenv()

logger = logging.getLogger("talentbridge.ai.requests")

PROTECTED_API_PREFIX = "/internal/api/v1"

CORRELATION_ID_PATTERN = re.compile(
    r"^[A-Za-z0-9._:-]{1,128}$"
)


def normalize_correlation_id(
    supplied_value: str | None,
) -> str:
    """
    Accept a safe correlation ID or generate a new one.

    Restricting the characters and length prevents log injection.
    """

    if (
        supplied_value
        and CORRELATION_ID_PATTERN.fullmatch(
            supplied_value
        )
    ):
        return supplied_value

    return str(uuid.uuid4())


def read_positive_integer(
    environment_name: str,
    default_value: int,
) -> int:
    """
    Read a positive integer from the environment.

    Invalid values fall back to the safe default.
    """

    raw_value = os.getenv(
        environment_name,
        str(default_value),
    )

    try:
        parsed_value = int(raw_value)
    except ValueError:
        logger.warning(
            "Invalid integer configuration for %s. "
            "Using default value %s.",
            environment_name,
            default_value,
        )
        return default_value

    if parsed_value <= 0:
        logger.warning(
            "Non-positive configuration for %s. "
            "Using default value %s.",
            environment_name,
            default_value,
        )
        return default_value

    return parsed_value


@dataclass(frozen=True)
class RateLimitDecision:
    """
    Result returned by the in-memory rate limiter.
    """

    allowed: bool
    remaining: int
    retry_after_seconds: int


class InMemoryFixedWindowRateLimiter:
    """
    Thread-safe in-memory request limiter.

    This implementation is suitable for the current single-instance
    TalentBridge AI service. A shared Redis-backed limiter would be
    required when multiple AI-service instances are deployed.
    """

    def __init__(
        self,
        max_requests: int,
        window_seconds: int,
        clock: Callable[[], float] = time.monotonic,
    ) -> None:
        if max_requests <= 0:
            raise ValueError(
                "max_requests must be greater than zero."
            )

        if window_seconds <= 0:
            raise ValueError(
                "window_seconds must be greater than zero."
            )

        self.max_requests = max_requests
        self.window_seconds = window_seconds
        self.clock = clock

        self._requests: dict[str, deque[float]] = (
            defaultdict(deque)
        )
        self._lock = Lock()

    def check(
        self,
        client_key: str,
    ) -> RateLimitDecision:
        """
        Check whether the client can make another request.
        """

        current_time = self.clock()
        window_start = (
            current_time - self.window_seconds
        )

        with self._lock:
            timestamps = self._requests[client_key]

            while (
                timestamps
                and timestamps[0] <= window_start
            ):
                timestamps.popleft()

            if len(timestamps) >= self.max_requests:
                oldest_request = timestamps[0]

                retry_after = math.ceil(
                    self.window_seconds
                    - (current_time - oldest_request)
                )

                return RateLimitDecision(
                    allowed=False,
                    remaining=0,
                    retry_after_seconds=max(
                        retry_after,
                        1,
                    ),
                )

            timestamps.append(current_time)

            return RateLimitDecision(
                allowed=True,
                remaining=(
                    self.max_requests
                    - len(timestamps)
                ),
                retry_after_seconds=0,
            )


class ObservabilityRateLimitMiddleware(
    BaseHTTPMiddleware
):
    """
    Adds correlation IDs, safe request logs, and rate limiting.

    Request bodies, authorization values, internal API keys,
    provider keys, and candidate details are never logged.
    """

    def __init__(
        self,
        app,
        protected_prefix: str = PROTECTED_API_PREFIX,
    ) -> None:
        super().__init__(app)

        self.protected_prefix = protected_prefix

        self.max_requests = read_positive_integer(
            "AI_RATE_LIMIT_REQUESTS",
            30,
        )

        self.window_seconds = read_positive_integer(
            "AI_RATE_LIMIT_WINDOW_SECONDS",
            60,
        )

        self.rate_limiter = (
            InMemoryFixedWindowRateLimiter(
                max_requests=self.max_requests,
                window_seconds=self.window_seconds,
            )
        )

    async def dispatch(
        self,
        request: Request,
        call_next: RequestResponseEndpoint,
    ) -> Response:
        """
        Process one incoming request.
        """

        correlation_id = normalize_correlation_id(
            request.headers.get(
                "X-Correlation-ID"
            )
        )

        request.state.correlation_id = correlation_id

        started_at = time.perf_counter()
        rate_limit_decision: (
            RateLimitDecision | None
        ) = None

        if request.url.path.startswith(
            self.protected_prefix
        ):
            client_host = (
                request.client.host
                if request.client
                else "unknown-client"
            )

            rate_limit_decision = (
                self.rate_limiter.check(
                    client_host
                )
            )

            if not rate_limit_decision.allowed:
                response = JSONResponse(
                    status_code=429,
                    content={
                        "status": "ERROR",
                        "errorCode": (
                            "AI_RATE_LIMIT_EXCEEDED"
                        ),
                        "message": (
                            "Too many requests. "
                            "Try again later."
                        ),
                        "correlationId": correlation_id,
                        "receivedAt": (
                            datetime.now()
                            .astimezone()
                            .isoformat()
                        ),
                    },
                )

                response.headers[
                    "X-Correlation-ID"
                ] = correlation_id

                response.headers[
                    "Retry-After"
                ] = str(
                    rate_limit_decision
                    .retry_after_seconds
                )

                response.headers[
                    "X-RateLimit-Limit"
                ] = str(self.max_requests)

                response.headers[
                    "X-RateLimit-Remaining"
                ] = "0"

                self._log_request(
                    request=request,
                    status_code=429,
                    correlation_id=correlation_id,
                    started_at=started_at,
                )

                return response

        try:
            response = await call_next(request)

        except Exception:
            duration_ms = (
                time.perf_counter() - started_at
            ) * 1000

            logger.exception(
                "ai_request_failed "
                "method=%s path=%s "
                "status=500 correlation_id=%s "
                "request_id=%s duration_ms=%.2f",
                request.method,
                request.url.path,
                correlation_id,
                getattr(
                    request.state,
                    "ai_request_id",
                    "-",
                ),
                duration_ms,
            )

            raise

        response.headers[
            "X-Correlation-ID"
        ] = correlation_id

        if rate_limit_decision is not None:
            response.headers[
                "X-RateLimit-Limit"
            ] = str(self.max_requests)

            response.headers[
                "X-RateLimit-Remaining"
            ] = str(
                rate_limit_decision.remaining
            )

        self._log_request(
            request=request,
            status_code=response.status_code,
            correlation_id=correlation_id,
            started_at=started_at,
        )

        return response

    @staticmethod
    def _log_request(
        request: Request,
        status_code: int,
        correlation_id: str,
        started_at: float,
    ) -> None:
        """
        Write one safe metadata-only request log.
        """

        duration_ms = (
            time.perf_counter() - started_at
        ) * 1000

        logger.info(
            "ai_request "
            "method=%s path=%s status=%s "
            "correlation_id=%s request_id=%s "
            "duration_ms=%.2f",
            request.method,
            request.url.path,
            status_code,
            correlation_id,
            getattr(
                request.state,
                "ai_request_id",
                "-",
            ),
            duration_ms,
        )