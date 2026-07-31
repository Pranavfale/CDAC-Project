from dotenv import load_dotenv
from fastapi import FastAPI

from app.api.health_routes import router as health_router


# Load variables from the local .env file before the routes are used.
load_dotenv()


app = FastAPI(
    title="TalentBridge AI Service",
    description=(
        "Internal AI microservice for controlled "
        "offer-letter content generation."
    ),
    version="0.1.0",
    docs_url="/docs",
    redoc_url="/redoc"
)


app.include_router(health_router)