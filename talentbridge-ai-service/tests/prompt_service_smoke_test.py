from app.schemas.request_schemas import GenerateOfferRequest
from app.services.prompt_service import prompt_service


def create_request(
    prompt_version: str = "offer-v1",
) -> GenerateOfferRequest:
    """
    Create a valid generation request for prompt testing.
    """

    payload = {
        "requestId": "AI-OFF-101-GEN-1",
        "correlationId": "corr-7f84f1",
        "promptVersion": prompt_version,
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


def test_generation_messages() -> None:
    """
    Verify that controlled provider messages are created.
    """

    request = create_request()

    messages = prompt_service.build_generation_messages(
        request
    )

    assert len(messages) == 2

    assert messages[0]["role"] == "system"
    assert messages[1]["role"] == "user"

    system_message = messages[0]["content"]
    user_message = messages[1]["content"]

    assert "Use only the facts provided" in system_message
    assert "Never guess or invent" in system_message
    assert "Return exactly one valid JSON object" in system_message

    assert "Rahul Sharma" in user_message
    assert "Java Developer" in user_message
    assert "INR 8,00,000 per annum" in user_message

    # Information unnecessary for drafting must not be sent
    # to the external AI provider.
    assert request.candidate.email not in user_message
    assert request.requestId not in user_message
    assert request.correlationId not in user_message

    print("=" * 70)
    print("SYSTEM MESSAGE")
    print("=" * 70)
    print(system_message)

    print("\n")
    print("=" * 70)
    print("USER MESSAGE")
    print("=" * 70)
    print(user_message)

    print("\nPrompt message test passed.")


def test_unsupported_prompt_version() -> None:
    """
    Verify that unknown prompt versions are rejected.
    """

    request = create_request(
        prompt_version="offer-v99"
    )

    print("\n")
    print("=" * 70)
    print("UNSUPPORTED VERSION TEST")
    print("=" * 70)

    try:
        prompt_service.build_generation_messages(request)
        raise AssertionError(
            "Unsupported prompt version was accepted."
        )
    except ValueError as error:
        print("Unsupported version correctly rejected.")
        print(error)


if __name__ == "__main__":
    test_generation_messages()
    test_unsupported_prompt_version()