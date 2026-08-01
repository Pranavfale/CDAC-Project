from fastapi import FastAPI
from fastapi.exceptions import RequestValidationError

from app.api.health_routes import router as health_router
from app.api.offer_routes import router as offer_router

from app.middleware.error_handler import (
    ai_provider_exception_handler,
    request_validation_exception_handler,
)
from app.middleware.internal_api_key import (
    InternalApiKeyMiddleware,
)
from app.middleware.observability_rate_limit_middleware import (
    ObservabilityRateLimitMiddleware,
)
from app.middleware.request_size_limit_middleware import (
    RequestSizeLimitMiddleware,
)

from app.providers.provider_exceptions import (
    AiProviderError,
)


app = FastAPI(
    title="TalentBridge AI Service",
    description=(
        "Internal AI microservice for controlled offer-letter "
        "generation, rewriting, and regeneration."
    ),
    version="0.7.0",
    docs_url="/docs",
    redoc_url="/redoc",
    openapi_url="/openapi.json",
)


# ------------------------------------------------------------------
# Middleware
#
# Registration order is important. Starlette runs the last added
# middleware as the outermost request layer.
# ------------------------------------------------------------------

app.add_middleware(
    InternalApiKeyMiddleware
)

app.add_middleware(
    RequestSizeLimitMiddleware
)

app.add_middleware(
    ObservabilityRateLimitMiddleware
)


# ------------------------------------------------------------------
# Exception handlers
# ------------------------------------------------------------------

app.add_exception_handler(
    RequestValidationError,
    request_validation_exception_handler,
)

app.add_exception_handler(
    AiProviderError,
    ai_provider_exception_handler,
)


# ------------------------------------------------------------------
# Routers
# ------------------------------------------------------------------

app.include_router(health_router)
app.include_router(offer_router)