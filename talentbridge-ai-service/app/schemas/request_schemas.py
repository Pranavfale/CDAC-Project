from datetime import date
from enum import Enum
from typing import Literal

from pydantic import (
    BaseModel,
    ConfigDict,
    EmailStr,
    Field,
)


class ApiRequestModel(BaseModel):
    """
    Base model for requests received by the TalentBridge AI service.

    Unknown fields are rejected, and surrounding whitespace is removed
    from string values.
    """

    model_config = ConfigDict(
        extra="forbid",
        str_strip_whitespace=True,
    )


class EmploymentType(str, Enum):
    """
    Employment types supported by TalentBridge.
    """

    FULL_TIME = "FULL_TIME"
    PART_TIME = "PART_TIME"
    INTERNSHIP = "INTERNSHIP"
    CONTRACT = "CONTRACT"


class WorkMode(str, Enum):
    """
    Work modes supported by TalentBridge.
    """

    ONSITE = "ONSITE"
    HYBRID = "HYBRID"
    REMOTE = "REMOTE"


class CandidateData(ApiRequestModel):
    """
    Candidate facts supplied by the Spring Boot backend.
    """

    name: str = Field(
        min_length=1,
        max_length=200,
        description="Full name of the selected candidate.",
    )

    email: EmailStr = Field(
        description="Validated email address of the candidate.",
    )


class PositionData(ApiRequestModel):
    """
    Position and vacancy facts supplied by Spring Boot.
    """

    title: str = Field(
        min_length=1,
        max_length=200,
        description="Position offered to the candidate.",
    )

    department: str = Field(
        min_length=1,
        max_length=200,
        description="Department associated with the position.",
    )

    employmentType: EmploymentType = Field(
        description="Approved employment type.",
    )

    workLocation: str = Field(
        min_length=1,
        max_length=300,
        description="Approved work location.",
    )

    workMode: WorkMode = Field(
        description="Approved work mode.",
    )


class OfferData(ApiRequestModel):
    """
    Approved offer facts supplied by Spring Boot.

    The AI service uses these facts only for drafting. It must not
    calculate or modify them.
    """

    offeredCtc: str = Field(
        min_length=1,
        max_length=300,
        description=(
            "Approved compensation text, including the currency "
            "and payment period where applicable."
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
        description="Approved benefits supplied by Spring Boot.",
    )

    additionalTerms: list[str] = Field(
        default_factory=list,
        description="Approved additional employment terms.",
    )


class CompanyData(ApiRequestModel):
    """
    Company facts used when drafting the offer letter.
    """

    name: str = Field(
        min_length=1,
        max_length=300,
        description="Company name.",
    )

    address: str = Field(
        min_length=1,
        max_length=1000,
        description="Company address.",
    )


class GenerateOfferRequest(ApiRequestModel):
    """
    Complete request received by the offer-generation endpoint.
    """

    requestId: str = Field(
        min_length=1,
        max_length=200,
        description="Unique AI request identifier created by Spring Boot.",
    )

    correlationId: str = Field(
        min_length=1,
        max_length=200,
        description=(
            "Correlation identifier shared across TalentBridge services."
        ),
    )

    promptVersion: str = Field(
        min_length=1,
        max_length=100,
        description="Controlled offer-generation prompt version.",
    )

    candidate: CandidateData

    position: PositionData

    offer: OfferData

    company: CompanyData


class RewriteFacts(ApiRequestModel):
    """
    Verified facts available while rewriting one offer section.
    """

    candidateName: str = Field(
        min_length=1,
        max_length=200,
        description="Verified candidate name.",
    )

    position: str = Field(
        min_length=1,
        max_length=200,
        description="Verified position title.",
    )

    companyName: str = Field(
        min_length=1,
        max_length=300,
        description="Verified company name.",
    )


RewriteSection = Literal[
    "documentTitle",
    "subject",
    "salutation",
    "introduction",
    "compensationSection",
    "joiningSection",
    "termsAndConditions",
    "acceptanceSection",
    "closing",
]


class RewriteOfferRequest(ApiRequestModel):
    """
    Request to rewrite one existing offer-document section.
    """

    requestId: str = Field(
        min_length=1,
        max_length=200,
        description="Unique rewrite request identifier.",
    )

    correlationId: str = Field(
        min_length=1,
        max_length=200,
        description="Correlation identifier supplied by Spring Boot.",
    )

    section: RewriteSection = Field(
        description="Offer-document section that must be rewritten.",
    )

    currentContent: str = Field(
        min_length=1,
        max_length=10000,
        description="Current content of the selected offer section.",
    )

    instruction: str = Field(
        min_length=1,
        max_length=1000,
        description="HR instruction describing the required rewrite.",
    )

    facts: RewriteFacts