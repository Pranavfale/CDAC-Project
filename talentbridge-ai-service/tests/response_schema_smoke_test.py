from datetime import datetime

from pydantic import ValidationError

from app.schemas.response_schemas import (
    AiErrorResponse,
    GenerateOfferSuccessResponse,
)


def test_valid_success_response() -> None:
    """
    Verify that a complete structured offer response is accepted.
    """

    valid_response = {
        "requestId": "AI-OFF-101-GEN-3",
        "status": "SUCCESS",
        "provider": "groq",
        "modelName": "configured-model",
        "promptVersion": "offer-v1",
        "content": {
            "documentTitle": "Offer of Employment",
            "subject": "Offer for the Position of Java Developer",
            "salutation": "Dear Rahul Sharma,",
            "introduction": (
                "We are pleased to offer you the position "
                "of Java Developer at ABC Technologies."
            ),
            "positionDetails": {
                "position": "Java Developer",
                "department": "Software Development",
                "employmentType": "FULL_TIME",
                "workLocation": "Pune",
                "workMode": "HYBRID",
            },
            "compensationSection": (
                "Your annual cost to company will be "
                "INR 8,00,000 per annum."
            ),
            "joiningSection": (
                "Your proposed joining date is 20 August 2026."
            ),
            "termsAndConditions": [
                (
                    "This offer is subject to verification "
                    "of the documents supplied by you."
                ),
                (
                    "You must accept this offer on or before "
                    "10 August 2026."
                ),
            ],
            "acceptanceSection": (
                "Please confirm your acceptance through "
                "the TalentBridge candidate portal."
            ),
            "closing": (
                "Sincerely,\nHuman Resources\nABC Technologies"
            ),
        },
        "missingFields": [],
        "receivedAt": datetime.now().astimezone().isoformat(),
    }

    response = GenerateOfferSuccessResponse.model_validate(
        valid_response
    )

    print("=" * 60)
    print("VALID SUCCESS RESPONSE")
    print("=" * 60)
    print(response.model_dump_json(indent=2))


def test_invalid_success_response() -> None:
    """
    Verify that incomplete or unsupported AI output is rejected.
    """

    invalid_response = {
        "requestId": "AI-OFF-101-GEN-4",
        "status": "SUCCESS",
        "provider": "groq",
        "modelName": "configured-model",
        "promptVersion": "offer-v1",
        "content": {
            "documentTitle": "Offer of Employment",
            "subject": "Offer for Java Developer",
            "salutation": "Dear Rahul Sharma,",
            "introduction": "We are pleased to offer you the role.",
            "positionDetails": {
                "position": "Java Developer",
                "department": "Software Development",
                "employmentType": "PERMANENT",
                "workLocation": "Pune",
                "workMode": "HYBRID",
            },
            "compensationSection": "",
            "joiningSection": "Joining details.",
            "termsAndConditions": [],
            "acceptanceSection": "Accept through TalentBridge.",
            "closing": "Human Resources",
            "candidateRanking": 1,
        },
        "missingFields": [],
        "receivedAt": datetime.now().astimezone().isoformat(),
    }

    print("\n")
    print("=" * 60)
    print("INVALID SUCCESS RESPONSE")
    print("=" * 60)

    try:
        GenerateOfferSuccessResponse.model_validate(
            invalid_response
        )
        print("ERROR: Invalid response was unexpectedly accepted.")
    except ValidationError as error:
        print("Invalid response correctly rejected.")
        print(error)


def test_error_response() -> None:
    """
    Verify the controlled AI error format.
    """

    error_response = AiErrorResponse(
        requestId="AI-OFF-101-GEN-5",
        status="FAILED",
        errorCode="AI_PROVIDER_TIMEOUT",
        message=(
            "The AI provider did not respond within "
            "the configured timeout."
        ),
        missingFields=[],
        retryable=True,
    )

    print("\n")
    print("=" * 60)
    print("CONTROLLED ERROR RESPONSE")
    print("=" * 60)
    print(error_response.model_dump_json(indent=2))


if __name__ == "__main__":
    test_valid_success_response()
    test_invalid_success_response()
    test_error_response()