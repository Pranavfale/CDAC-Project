from enum import Enum

from pydantic import BaseModel, ConfigDict, Field


class OfferDocumentModel(BaseModel):
    """
    Base class for structured offer-document models.

    Unknown properties are rejected so that unexpected AI output
    cannot silently enter the TalentBridge workflow.
    """

    model_config = ConfigDict(
        extra="forbid",
        str_strip_whitespace=True,
    )


class EmploymentType(str, Enum):
    """
    Employment types allowed by TalentBridge.
    """

    FULL_TIME = "FULL_TIME"
    PART_TIME = "PART_TIME"
    INTERNSHIP = "INTERNSHIP"
    CONTRACT = "CONTRACT"


class WorkMode(str, Enum):
    """
    Work modes allowed by TalentBridge.
    """

    ONSITE = "ONSITE"
    HYBRID = "HYBRID"
    REMOTE = "REMOTE"


class PositionDetails(OfferDocumentModel):
    """
    Position facts included in the generated offer document.
    """

    position: str = Field(
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


class OfferDocumentContent(OfferDocumentModel):
    """
    Complete structured content generated for an offer letter.

    This content remains a draft until HR explicitly approves it
    through the Spring Boot backend.
    """

    documentTitle: str = Field(
        min_length=1,
        description="Title displayed on the offer document.",
    )

    subject: str = Field(
        min_length=1,
        description="Subject of the offer letter.",
    )

    salutation: str = Field(
        min_length=1,
        description="Greeting addressed to the candidate.",
    )

    introduction: str = Field(
        min_length=1,
        description="Professional introductory paragraph.",
    )

    positionDetails: PositionDetails

    compensationSection: str = Field(
        min_length=1,
        description="Compensation wording using supplied offer facts only.",
    )

    joiningSection: str = Field(
        min_length=1,
        description="Joining-date wording using supplied facts only.",
    )

    termsAndConditions: list[str] = Field(
        description="Approved terms and conditions.",
    )

    acceptanceSection: str = Field(
        min_length=1,
        description="Instructions explaining how to accept the offer.",
    )

    closing: str = Field(
        min_length=1,
        description="Professional closing section.",
    )