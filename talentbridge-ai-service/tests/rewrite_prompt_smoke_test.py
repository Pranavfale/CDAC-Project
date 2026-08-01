from pydantic import ValidationError

from app.schemas.request_schemas import RewriteOfferRequest
from app.services.prompt_service import prompt_service


def create_valid_request() -> RewriteOfferRequest:
    """
    Create a valid rewrite request for prompt testing.
    """

    payload = {
        "requestId": "AI-OFF-101-REWRITE-1",
        "correlationId": "corr-7f84f1",
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


def test_valid_rewrite_messages() -> None:
    """
    Verify that controlled rewrite messages are created correctly.
    """

    request = create_valid_request()

    messages = prompt_service.build_rewrite_messages(
        request
    )

    assert len(messages) == 2
    assert messages[0]["role"] == "system"
    assert messages[1]["role"] == "user"

    system_content = messages[0]["content"]
    user_content = messages[1]["content"]

    normalized_system_content = " ".join(
        system_content.split()
    ).lower()

    assert (
        "rewrite one existing offer-letter section"
        in normalized_system_content
    )

    assert "introduction" in user_content
    assert request.currentContent in user_content
    assert request.instruction in user_content

    assert "Rahul Sharma" in user_content
    assert "Java Developer" in user_content
    assert "ABC Technologies" in user_content

    # Internal identifiers are not required by the provider.
    assert request.requestId not in user_content
    assert request.correlationId not in user_content

    print("=" * 70)
    print("REWRITE PROMPT TEST PASSED")
    print("=" * 70)

    print("\nSYSTEM MESSAGE:\n")
    print(system_content)

    print("\nUSER MESSAGE:\n")
    print(user_content)


def test_invalid_section() -> None:
    """
    Verify that unsupported rewrite sections are rejected.
    """

    payload = {
        "requestId": "AI-OFF-101-REWRITE-2",
        "correlationId": "corr-7f84f2",
        "section": "candidateRanking",
        "currentContent": "Current content.",
        "instruction": "Improve this section.",
        "facts": {
            "candidateName": "Rahul Sharma",
            "position": "Java Developer",
            "companyName": "ABC Technologies",
        },
    }

    print("\n")
    print("=" * 70)
    print("INVALID SECTION TEST")
    print("=" * 70)

    try:
        RewriteOfferRequest.model_validate(payload)

        raise AssertionError(
            "Unsupported rewrite section was accepted."
        )

    except ValidationError as error:
        print("Unsupported section correctly rejected.")
        print(error)


def test_blank_instruction() -> None:
    """
    Verify that a blank rewrite instruction is rejected.
    """

    payload = {
        "requestId": "AI-OFF-101-REWRITE-3",
        "correlationId": "corr-7f84f3",
        "section": "introduction",
        "currentContent": "Current content.",
        "instruction": "   ",
        "facts": {
            "candidateName": "Rahul Sharma",
            "position": "Java Developer",
            "companyName": "ABC Technologies",
        },
    }

    print("\n")
    print("=" * 70)
    print("BLANK INSTRUCTION TEST")
    print("=" * 70)

    try:
        RewriteOfferRequest.model_validate(payload)

        raise AssertionError(
            "Blank instruction was accepted."
        )

    except ValidationError as error:
        print("Blank instruction correctly rejected.")
        print(error)


if __name__ == "__main__":
    test_valid_rewrite_messages()
    test_invalid_section()
    test_blank_instruction()