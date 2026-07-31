from datetime import datetime
from typing import Literal

from pydantic import BaseModel, Field


class HealthResponse(BaseModel):
    """
    Structured response returned by the AI service health endpoint.
    """

    service: str = Field(
        description="Internal name of the running service."
    )

    status: Literal["UP"] = Field(
        description="Current health status of the service."
    )

    port: int = Field(
        gt=0,
        le=65535,
        description="Port on which the service is configured to run."
    )

    providerConfigured: bool = Field(
        description="Whether an AI-provider API key is configured."
    )

    timestamp: datetime = Field(
        description="Current server date and time."
    )