from pydantic import ValidationError

from app.schemas.regenerate_schemas import (
    RegenerateOfferRequest,
)
from app.services.regenerate_prompt_service import (
    regenerate_prompt_service,
)


def create_valid_request() -> RegenerateOfferRequest:
    """
    Create a valid regeneration request.
    """

    payload = {
        "requestId": "AI-OFF-101-REGENERATE-1",
        "correlationId": "corr-regenerate-1",
        "promptVersion": "offer-v1",
        "candidate": {
            "name": "Rahul Sharma",
            "email": "rahul@example.com",
        },
        "position": {
            "title": "Java Developer",
            "department": "Software Development",
            "employmentType": "FULL_TIME",
            "workLocation": "Pune",
            "workMode": "HYBRID",
        },
        "offer": {
            "offeredCtc": "INR 8,00,000 per annum",
            "joiningDate": "2026-08-20",
            "expiryDate": "2026-08-10",
            "benefits": [
                "Health insurance",
            ],
            "additionalTerms": [
                (
                    "Employment is subject to successful "
                    "document verification."
                ),
            ],
        },
        "company": {
            "name": "ABC Technologies",
            "address": "Pune, Maharashtra",
        },
        "previousContent": {
            "documentTitle": "Offer of Employment",
            "subject": (
                "Offer for the Position of Java Developer"
            ),
            "salutation": "Dear Rahul Sharma,",
            "introduction": (
                "We are pleased to offer you the Java Developer "
                "position at ABC Technologies."
            ),
            "positionDetails": {
                "position": "Java Developer",
                "department": "Software Development",
                "employmentType": "FULL_TIME",
                "workLocation": "Pune",
                "workMode": "HYBRID",
            },
            "compensationSection": (
                "Your approved compensation is "
                "INR 8,00,000 per annum."
            ),
            "joiningSection": (
                "Your joining date is 20 August 2026."
            ),
            "termsAndConditions": [
                (
                    "Employment is subject to successful "
                    "document verification."
                ),
                "Health insurance",
            ],
            "acceptanceSection": (
                "Accept through TalentBridge on or before "
                "10 August 2026."
            ),
            "closing": (
                "Sincerely,\n"
                "Human Resources\n"
                "ABC Technologies"
            ),
        },
        "instruction": (
            "Make the complete offer more concise and formal."
        ),
    }

    return RegenerateOfferRequest.model_validate(payload)


def test_regeneration_messages() -> None:
    """
    Verify controlled regeneration messages.
    """

    request = create_valid_request()

    messages = (
        regenerate_prompt_service
        .build_regeneration_messages(request)
    )

    assert len(messages) == 2
    assert messages[0]["role"] == "system"
    assert messages[1]["role"] == "user"

    system_content = messages[0]["content"]
    user_content = messages[1]["content"]

    normalized_system = " ".join(
        system_content.split()
    ).lower()

    assert (
        "complete replacement offer-letter draft"
        in normalized_system
    )

    assert "VERIFIED_FACTS_JSON_START" in user_content
    assert "PREVIOUS_CONTENT_JSON_START" in user_content
    assert "HR_INSTRUCTION_START" in user_content

    assert "Rahul Sharma" in user_content
    assert "Java Developer" in user_content
    assert "ABC Technologies" in user_content
    assert "INR 8,00,000 per annum" in user_content

    assert request.instruction in user_content

    # Internal identifiers and candidate email must not be sent
    # to the external provider.
    assert request.requestId not in user_content
    assert request.correlationId not in user_content
    assert str(request.candidate.email) not in user_content

    print("=" * 70)
    print("REGENERATION PROMPT TEST PASSED")
    print("=" * 70)

    print("\nSYSTEM MESSAGE:\n")
    print(system_content)

    print("\nUSER MESSAGE:\n")
    print(user_content)


def test_missing_previous_content() -> None:
    """
    Verify previous content is required.
    """

    payload = create_valid_request().model_dump(
        mode="json"
    )

    del payload["previousContent"]

    print("\n")
    print("=" * 70)
    print("MISSING PREVIOUS CONTENT TEST")
    print("=" * 70)

    try:
        RegenerateOfferRequest.model_validate(payload)

        raise AssertionError(
            "Missing previous content was accepted."
        )

    except ValidationError as error:
        print("Missing previous content correctly rejected.")
        print(error)


def test_blank_instruction() -> None:
    """
    Verify a supplied blank instruction is rejected.
    """

    payload = create_valid_request().model_dump(
        mode="json"
    )

    payload["instruction"] = "   "

    print("\n")
    print("=" * 70)
    print("BLANK INSTRUCTION TEST")
    print("=" * 70)

    try:
        RegenerateOfferRequest.model_validate(payload)

        raise AssertionError(
            "Blank regeneration instruction was accepted."
        )

    except ValidationError as error:
        print("Blank instruction correctly rejected.")
        print(error)


if __name__ == "__main__":
    test_regeneration_messages()
    test_missing_previous_content()
    test_blank_instruction()