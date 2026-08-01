from typing import Any

from app.providers.base_provider import (
    AiProvider,
    ProviderResult,
)
from app.providers.provider_exceptions import ProviderResponseError
from app.schemas.request_schemas import GenerateOfferRequest
from app.services.ai_offer_generation_service import (
    AiOfferGenerationService,
)


class ValidFakeProvider(AiProvider):
    """
    Return a valid deterministic provider response.
    """

    def generate_json(
        self,
        messages: list,
    ) -> ProviderResult:
        return ProviderResult(
            provider="fake",
            model_name="fake-model-v1",
            content={
                "documentTitle": "Offer of Employment",
                "subject": (
                    "Offer for the Position of Java Developer"
                ),
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
                    "Please accept this offer through TalentBridge "
                    "on or before 10 August 2026."
                ),
                "closing": (
                    "Sincerely,\n"
                    "Human Resources\n"
                    "ABC Technologies"
                ),
            },
        )


class InvalidFakeProvider(AiProvider):
    """
    Return an incomplete provider response.
    """

    def generate_json(
        self,
        messages: list,
    ) -> ProviderResult:
        return ProviderResult(
            provider="fake",
            model_name="fake-model-v1",
            content={
                "documentTitle": "Offer of Employment",
            },
        )


def create_request() -> GenerateOfferRequest:
    """
    Create a valid TalentBridge generation request.
    """

    payload: dict[str, Any] = {
        "requestId": "AI-OFF-SERVICE-TEST-1",
        "correlationId": "corr-service-test-1",
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
    }

    return GenerateOfferRequest.model_validate(payload)


def test_valid_provider_response() -> None:
    """
    Verify that valid provider output is accepted.
    """

    service = AiOfferGenerationService(
        provider_factory=lambda: ValidFakeProvider()
    )

    response = service.generate(create_request())

    assert response.status == "SUCCESS"
    assert response.provider == "fake"
    assert response.modelName == "fake-model-v1"
    assert (
        response.content.positionDetails.position
        == "Java Developer"
    )

    print("=" * 70)
    print("VALID PROVIDER RESPONSE TEST PASSED")
    print("=" * 70)
    print(response.model_dump_json(indent=2))


def test_invalid_provider_response() -> None:
    """
    Verify that incomplete provider output is rejected.
    """

    service = AiOfferGenerationService(
        provider_factory=lambda: InvalidFakeProvider()
    )

    print("\n")
    print("=" * 70)
    print("INVALID PROVIDER RESPONSE TEST")
    print("=" * 70)

    try:
        service.generate(create_request())

        raise AssertionError(
            "Invalid provider response was accepted."
        )

    except ProviderResponseError as error:
        print("Invalid response correctly rejected.")
        print(f"Error code: {error.error_code}")


if __name__ == "__main__":
    test_valid_provider_response()
    test_invalid_provider_response()