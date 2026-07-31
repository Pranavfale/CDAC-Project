from dotenv import load_dotenv
from fastapi import FastAPI
from fastapi.exceptions import RequestValidationError

from app.api.health_routes import router as health_router
from app.api.offer_routes import router as offer_router
from app.middleware.error_handler import (
    request_validation_exception_handler,
)
from app.middleware.internal_api_key import (
    internal_api_key_middleware,
)


load_dotenv()


app = FastAPI(
    title="TalentBridge AI Service",
    description=(
        "Internal AI microservice for controlled "
        "offer-letter content generation."
    ),
    version="0.3.0",
    docs_url="/docs",
    redoc_url="/redoc",
)


app.middleware("http")(internal_api_key_middleware)


app.add_exception_handler(
    RequestValidationError,
    request_validation_exception_handler,
)


app.include_router(health_router)
app.include_router(offer_router)