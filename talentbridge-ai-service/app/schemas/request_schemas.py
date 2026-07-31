from datetime import date
from enum import Enum

from pydantic import BaseModel, ConfigDict, EmailStr, Field


class ApiRequestModel(BaseModel):
    """
    Base class for AI-service request models.

    All request models reject unexpected JSON properties and
    automatically remove surrounding whitespace from strings.
    """

    model_config = ConfigDict(
        extra="forbid",
        str_strip_whitespace=True,
    )


class EmploymentType(str, Enum):
    """
    Employment types permitted by TalentBridge.
    """

    FULL_TIME = "FULL_TIME"
    PART_TIME = "PART_TIME"
    INTERNSHIP = "INTERNSHIP"
    CONTRACT = "CONTRACT"


class WorkMode(str, Enum):
    """
    Work modes permitted by TalentBridge.
    """

    ONSITE = "ONSITE"
    HYBRID = "HYBRID"
    REMOTE = "REMOTE"


class CandidateData(ApiRequestModel):
    """
    Candidate facts supplied by Spring Boot.

    The AI service does not load candidate information from MySQL.
    """

    name: str = Field(
        min_length=1,
        description="Full name of the selected candidate.",
    )

    email: EmailStr = Field(
        description="Validated email address of the candidate.",
    )


class PositionData(ApiRequestModel):
    """
    Vacancy and position facts supplied by Spring Boot.
    """

    title: str = Field(
        min_length=1,
        description="Position offered to the candidate.",
    )

    department: str = Field(
        min_length=1,
        description="Department associated with the position.",
    )

    employmentType: EmploymentType = Field(
        description="Approved employment type.",
    )

    workLocation: str = Field(
        min_length=1,
        description="Approved work location.",
    )

    workMode: WorkMode = Field(
        description="Approved work mode.",
    )


class OfferData(ApiRequestModel):
    """
    Approved offer facts supplied by Spring Boot.

    The Python service uses these values only for document wording.
    It does not decide or modify them.
    """

    offeredCtc: str = Field(
        min_length=1,
        description=(
            "Approved compensation text supplied by Spring Boot, "
            "including currency or period where required."
        ),
    )

    joiningDate: date = Field(
        description="Approved candidate joining date.",
    )

    expiryDate: date = Field(
        description="Approved offer expiry date.",
    )

    benefits: list[str] = Field(
        default_factory=list,
        description="Approved benefits; may be empty.",
    )

    additionalTerms: list[str] = Field(
        default_factory=list,
        description="Approved additional terms; may be empty.",
    )


class CompanyData(ApiRequestModel):
    """
    Company information used in the offer letter.
    """

    name: str = Field(
        min_length=1,
        description="Company name.",
    )

    address: str = Field(
        min_length=1,
        description="Company address.",
    )


class GenerateOfferRequest(ApiRequestModel):
    """
    Complete request received by the offer-generation endpoint.
    """

    requestId: str = Field(
        min_length=1,
        description="Unique AI request identifier created by Spring Boot.",
    )

    correlationId: str = Field(
        min_length=1,
        description=(
            "Correlation identifier shared across the gateway, "
            "Spring Boot, and internal services."
        ),
    )

    promptVersion: str = Field(
        min_length=1,
        description="Version of the controlled offer prompt.",
    )

    candidate: CandidateData

    position: PositionData

    offer: OfferData

    company: CompanyData