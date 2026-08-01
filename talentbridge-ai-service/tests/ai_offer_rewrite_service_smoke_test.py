from app.providers.base_provider import (
    AiProvider,
    ProviderResult,
)
from app.providers.provider_exceptions import (
    ProviderResponseError,
)
from app.schemas.request_schemas import RewriteOfferRequest
from app.services.ai_offer_rewrite_service import (
    AiOfferRewriteService,
)


class ValidRewriteProvider(AiProvider):
    """
    Returns a valid deterministic rewrite result.
    """

    def generate_json(
        self,
        messages: list,
    ) -> ProviderResult:
        return ProviderResult(
            provider="fake",
            model_name="fake-rewrite-model-v1",
            content={
                "rewrittenContent": (
                    "ABC Technologies is pleased to offer you "
                    "the Java Developer position."
                )
            },
        )


class InventedFactProvider(AiProvider):
    """
    Adds an unsupported numeric employment condition.
    """

    def generate_json(
        self,
        messages: list,
    ) -> ProviderResult:
        return ProviderResult(
            provider="fake",
            model_name="fake-rewrite-model-v1",
            content={
                "rewrittenContent": (
                    "ABC Technologies is pleased to offer you "
                    "the Java Developer position with a "
                    "6-month probation period."
                )
            },
        )


class MissingFactProvider(AiProvider):
    """
    Removes required position and company facts.
    """

    def generate_json(
        self,
        messages: list,
    ) -> ProviderResult:
        return ProviderResult(
            provider="fake",
            model_name="fake-rewrite-model-v1",
            content={
                "rewrittenContent": (
                    "We are pleased to offer you this role."
                )
            },
        )


class InvalidSchemaProvider(AiProvider):
    """
    Returns an object that does not match the response schema.
    """

    def generate_json(
        self,
        messages: list,
    ) -> ProviderResult:
        return ProviderResult(
            provider="fake",
            model_name="fake-rewrite-model-v1",
            content={
                "answer": "Invalid provider structure."
            },
        )


def create_request() -> RewriteOfferRequest:
    """
    Create a valid rewrite request for testing.
    """

    payload = {
        "requestId": "AI-OFF-101-REWRITE-TEST-1",
        "correlationId": "corr-rewrite-test-1",
        "section": "introduction",
        "currentContent": (
            "We are pleased to offer you the Java Developer "
            "position at ABC Technologies."
        ),
        "instruction": (
            "Make the introduction concise and formal."
        ),
        "facts": {
            "candidateName": "Rahul Sharma",
            "position": "Java Developer",
            "companyName": "ABC Technologies",
        },
    }

    return RewriteOfferRequest.model_validate(payload)


def test_valid_rewrite() -> None:
    """
    Verify that valid rewritten content is accepted.
    """

    service = AiOfferRewriteService(
        provider_factory=lambda: ValidRewriteProvider()
    )

    response = service.rewrite(create_request())

    assert response.status == "SUCCESS"
    assert response.provider == "fake"
    assert response.modelName == (
        "fake-rewrite-model-v1"
    )
    assert response.section == "introduction"
    assert "Java Developer" in (
        response.rewrittenContent
    )
    assert "ABC Technologies" in (
        response.rewrittenContent
    )

    print("=" * 70)
    print("VALID REWRITE TEST PASSED")
    print("=" * 70)
    print(response.model_dump_json(indent=2))


def test_invented_fact_rejected() -> None:
    """
    Verify that an invented numeric fact is rejected.
    """

    service = AiOfferRewriteService(
        provider_factory=lambda: InventedFactProvider()
    )

    print("\n")
    print("=" * 70)
    print("INVENTED FACT TEST")
    print("=" * 70)

    try:
        service.rewrite(create_request())

        raise AssertionError(
            "Invented numeric fact was accepted."
        )

    except ProviderResponseError as error:
        print("Invented fact correctly rejected.")
        print(f"Error code: {error.error_code}")


def test_missing_fact_rejected() -> None:
    """
    Verify that required facts cannot be removed.
    """

    service = AiOfferRewriteService(
        provider_factory=lambda: MissingFactProvider()
    )

    print("\n")
    print("=" * 70)
    print("MISSING VERIFIED FACT TEST")
    print("=" * 70)

    try:
        service.rewrite(create_request())

        raise AssertionError(
            "Missing verified facts were accepted."
        )

    except ProviderResponseError as error:
        print("Missing facts correctly rejected.")
        print(f"Error code: {error.error_code}")


def test_invalid_schema_rejected() -> None:
    """
    Verify that invalid provider JSON structure is rejected.
    """

    service = AiOfferRewriteService(
        provider_factory=lambda: InvalidSchemaProvider()
    )

    print("\n")
    print("=" * 70)
    print("INVALID PROVIDER SCHEMA TEST")
    print("=" * 70)

    try:
        service.rewrite(create_request())

        raise AssertionError(
            "Invalid provider schema was accepted."
        )

    except ProviderResponseError as error:
        print("Invalid schema correctly rejected.")
        print(f"Error code: {error.error_code}")


if __name__ == "__main__":
    test_valid_rewrite()
    test_invented_fact_rejected()
    test_missing_fact_rejected()
    test_invalid_schema_rejected()