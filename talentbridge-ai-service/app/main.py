from dotenv import load_dotenv
from fastapi import FastAPI

from app.api.health_routes import router as health_router
from app.middleware.internal_api_key import internal_api_key_middleware


# Load variables from the local .env file before requests are handled.
load_dotenv()


app = FastAPI(
    title="TalentBridge AI Service",
    description=(
        "Internal AI microservice for controlled "
        "offer-letter content generation."
    ),
    version="0.2.0",
    docs_url="/docs",
    redoc_url="/redoc",
)


# Run internal service-key validation before protected routes.
app.middleware("http")(internal_api_key_middleware)


app.include_router(health_router)