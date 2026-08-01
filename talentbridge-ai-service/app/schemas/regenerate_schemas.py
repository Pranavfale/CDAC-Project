from pydantic import Field

from app.schemas.offer_document_schema import OfferDocumentContent
from app.schemas.request_schemas import GenerateOfferRequest


class RegenerateOfferRequest(GenerateOfferRequest):
    """
    Request for creating a complete replacement offer draft.

    The request contains the authoritative verified facts,
    the previous structured draft, and an optional HR instruction.
    """

    previousContent: OfferDocumentContent = Field(
        description=(
            "Previously generated structured offer content."
        ),
    )

    instruction: str | None = Field(
        default=None,
        min_length=1,
        max_length=1000,
        description=(
            "Optional HR instruction for regenerating the "
            "complete draft."
        ),
    )