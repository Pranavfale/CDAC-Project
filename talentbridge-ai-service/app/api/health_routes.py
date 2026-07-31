import os
from datetime import datetime

from fastapi import APIRouter

from app.schemas.response_schemas import HealthResponse


router = APIRouter(
    tags=["Health"]
)


def get_configured_port() -> int:
    """
    Read the service port from the environment.

    If the value is missing or invalid, use the documented
    TalentBridge AI-service port 8000.
    """

    raw_port = os.getenv("PORT", "8000")

    try:
        return int(raw_port)
    except ValueError:
        return 8000


def is_provider_configured() -> bool:
    """
    Return True only when a non-empty Groq API key is configured.

    The actual key is never returned in the health response.
    """

    api_key = os.getenv("GROQ_API_KEY", "")
    return bool(api_key.strip())


@router.get(
    "/internal/health",
    response_model=HealthResponse,
    summary="Check AI service health"
)
def get_health() -> HealthResponse:
    """
    Return a safe health response for internal service checks.
    """

    return HealthResponse(
        service="talentbridge-ai-service",
        status="UP",
        port=get_configured_port(),
        providerConfigured=is_provider_configured(),
        timestamp=datetime.now().astimezone()
    )