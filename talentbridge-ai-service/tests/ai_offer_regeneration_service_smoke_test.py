from app.providers.base_provider import (
    AiProvider,
    ProviderResult,
)
from app.providers.provider_exceptions import (
    ProviderResponseError,
)
from app.schemas.regenerate_schemas import (
    RegenerateOfferRequest,
)
from app.services.ai_offer_regeneration_service import (
    AiOfferRegenerationService,
)


class ValidRegenerationProvider(AiProvider):
    """
    Return valid deterministic replacement content.
    """

    def generate_json(
        self,
        messages: list,
    ) -> ProviderResult:
        return ProviderResult(
            provider="fake",
            model_name="fake-regenerate-model-v1",
            content={
                "documentTitle": "Offer of Employment",
                "subject": (
                    "Employment Offer for Java Developer"
                ),
                "salutation": "Dear Rahul Sharma,",
                "introduction": (
                    "ABC Technologies is pleased to offer you "
                    "the Java Developer position."
                ),
                "positionDetails": {
                    "position": "Java Developer",
                    "department": "Software Development",
                    "employmentType": "FULL_TIME",
                    "workLocation": "Pune",
                    "workMode": "HYBRID",
                },
                "compensationSection": (
                    "The approved compensation for this "
                    "position is INR 8,00,000 per annum."
                ),
                "joiningSection": (
                    "Your approved joining date is "
                    "20 August 2026."
                ),
                "termsAndConditions": [
                    (
                        "Employment is subject to successful "
                        "document verification."
                    ),
                    "Health insurance",
                ],
                "acceptanceSection": (
                    "Please confirm acceptance through "
                    "TalentBridge on or before "
                    "10 August 2026."
                ),
                "closing": (
                    "Sincerely,\n"
                    "Human Resources\n"
                    "ABC Technologies"
                ),
            },
        )


class InvalidSchemaProvider(AiProvider):
    """
    Return incomplete provider content.
    """

    def generate_json(
        self,
        messages: list,
    ) -> ProviderResult:
        return ProviderResult(
            provider="fake",
            model_name="fake-regenerate-model-v1",
            content={
                "documentTitle": "Offer of Employment",
            },
        )


class ChangedPositionProvider(AiProvider):
    """
    Return content containing an altered approved position.
    """

    def generate_json(
        self,
        messages: list,
    ) -> ProviderResult:
        return ProviderResult(
            provider="fake",
            model_name="fake-regenerate-model-v1",
            content={
                "documentTitle": "Offer of Employment",
                "subject": (
                    "Employment Offer for Senior Java Developer"
                ),
                "salutation": "Dear Rahul Sharma,",
                "introduction": (
                    "ABC Technologies is pleased to offer you "
                    "the Senior Java Developer position."
                ),
                "positionDetails": {
                    "position": "Senior Java Developer",
                    "department": "Software Development",
                    "employmentType": "FULL_TIME",
                    "workLocation": "Pune",
                    "workMode": "HYBRID",
                },
                "compensationSection": (
                    "The approved compensation is "
                    "INR 8,00,000 per annum."
                ),
                "joiningSection": (
                    "Your approved joining date is "
                    "20 August 2026."
                ),
                "termsAndConditions": [
                    (
                        "Employment is subject to successful "
                        "document verification."
                    ),
                    "Health insurance",
                ],
                "acceptanceSection": (
                    "Please accept through TalentBridge "
                    "on or before 10 August 2026."
                ),
                "closing": (
                    "Sincerely,\n"
                    "Human Resources\n"
                    "ABC Technologies"
                ),
            },
        )


def create_request() -> RegenerateOfferRequest:
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


def test_valid_regeneration() -> None:
    """
    Verify valid replacement content is accepted.
    """

    service = AiOfferRegenerationService(
        provider_factory=(
            lambda: ValidRegenerationProvider()
        )
    )

    response = service.regenerate(create_request())

    assert response.status == "SUCCESS"
    assert response.provider == "fake"
    assert response.modelName == (
        "fake-regenerate-model-v1"
    )
    assert response.content.positionDetails.position == (
        "Java Developer"
    )

    print("=" * 70)
    print("VALID REGENERATION TEST PASSED")
    print("=" * 70)
    print(response.model_dump_json(indent=2))


def test_invalid_schema_rejected() -> None:
    """
    Verify incomplete provider output is rejected.
    """

    service = AiOfferRegenerationService(
        provider_factory=(
            lambda: InvalidSchemaProvider()
        )
    )

    print("\n")
    print("=" * 70)
    print("INVALID REGENERATION SCHEMA TEST")
    print("=" * 70)

    try:
        service.regenerate(create_request())

        raise AssertionError(
            "Invalid regeneration content was accepted."
        )

    except ProviderResponseError as error:
        print("Invalid schema correctly rejected.")
        print(f"Error code: {error.error_code}")


def test_changed_position_rejected() -> None:
    """
    Verify altered approved position facts are rejected.
    """

    service = AiOfferRegenerationService(
        provider_factory=(
            lambda: ChangedPositionProvider()
        )
    )

    print("\n")
    print("=" * 70)
    print("CHANGED POSITION TEST")
    print("=" * 70)

    try:
        service.regenerate(create_request())

        raise AssertionError(
            "Changed position was accepted."
        )

    except ProviderResponseError as error:
        print("Changed position correctly rejected.")
        print(f"Error code: {error.error_code}")


if __name__ == "__main__":
    test_valid_regeneration()
    test_invalid_schema_rejected()
    test_changed_position_rejected()