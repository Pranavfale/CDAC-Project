import json

from dotenv import load_dotenv

from app.providers.provider_exceptions import AiProviderError
from app.providers.provider_factory import create_ai_provider
from app.schemas.request_schemas import GenerateOfferRequest
from app.services.prompt_service import prompt_service

load_dotenv()

def create_test_request() -> GenerateOfferRequest:
    """
    Create a valid request for live Groq testing.
    """

    payload = {
        "requestId": "AI-OFF-GROQ-TEST-1",
        "correlationId": "corr-groq-test-1",
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


def run_live_groq_test() -> None:
    """
    Build the controlled prompt and send it to Groq.
    """

    request = create_test_request()

    messages = prompt_service.build_generation_messages(
        request
    )

    provider = create_ai_provider()

    result = provider.generate_json(messages)

    assert result.provider == "groq"
    assert result.model_name
    assert isinstance(result.content, dict)

    print("=" * 70)
    print("GROQ PROVIDER TEST PASSED")
    print("=" * 70)

    print(f"Provider: {result.provider}")
    print(f"Model: {result.model_name}")

    print("\nGenerated JSON:")
    print(
        json.dumps(
            result.content,
            indent=2,
            ensure_ascii=False,
        )
    )


if __name__ == "__main__":
    try:
        run_live_groq_test()

    except AiProviderError as error:
        print("=" * 70)
        print("GROQ PROVIDER TEST FAILED")
        print("=" * 70)

        print(f"Error code: {error.error_code}")
        print(f"Message: {error}")
        print(f"Retryable: {error.retryable}")

        raise SystemExit(1)