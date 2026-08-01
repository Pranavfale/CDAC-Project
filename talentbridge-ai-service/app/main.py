from dotenv import load_dotenv
from fastapi import FastAPI
from fastapi.exceptions import RequestValidationError

from app.api.health_routes import router as health_router
from app.api.offer_routes import router as offer_router
from app.middleware.error_handler import (
    ai_provider_exception_handler,
    request_validation_exception_handler,
)
from app.middleware.internal_api_key import (
    internal_api_key_middleware,
)
from app.providers.provider_exceptions import AiProviderError


# Load environment variables before handling requests.
load_dotenv()


app = FastAPI(
    title="TalentBridge AI Service",
    description=(
        "Internal AI microservice for controlled "
        "offer-letter content generation."
    ),
    version="0.4.0",
    docs_url="/docs",
    redoc_url="/redoc",
)


# Protect all routes under /internal/api/v1/**
# using the X-AI-Service-Key header.
app.middleware("http")(internal_api_key_middleware)


# Convert FastAPI/Pydantic request-validation errors
# into the controlled TalentBridge error response.
app.add_exception_handler(
    RequestValidationError,
    request_validation_exception_handler,
)


# Convert Groq/provider errors into safe TalentBridge responses.
app.add_exception_handler(
    AiProviderError,
    ai_provider_exception_handler,
)


# Register application routes.
app.include_router(health_router)
app.include_router(offer_router)