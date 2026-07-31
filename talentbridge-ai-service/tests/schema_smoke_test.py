from pydantic import ValidationError

from app.schemas.request_schemas import GenerateOfferRequest


def test_valid_request() -> None:
    """
    Create a valid TalentBridge generation request.
    """

    valid_payload = {
        "requestId": "AI-OFF-101-GEN-1",
        "correlationId": "corr-7f84f1",
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
            "benefits": [],
            "additionalTerms": [],
        },
        "company": {
            "name": "ABC Technologies",
            "address": "Pune, Maharashtra",
        },
    }

    request = GenerateOfferRequest.model_validate(valid_payload)

    print("=" * 60)
    print("VALID REQUEST")
    print("=" * 60)
    print(request.model_dump_json(indent=2))


def test_invalid_request() -> None:
    """
    Verify that missing required offer data is rejected.
    """

    invalid_payload = {
        "requestId": "AI-OFF-101-GEN-2",
        "correlationId": "corr-7f84f2",
        "promptVersion": "offer-v1",
        "candidate": {
            "name": "Rahul Sharma",
            "email": "invalid-email",
        },
        "position": {
            "title": "Java Developer",
            "department": "Software Development",
            "employmentType": "PERMANENT",
            "workLocation": "Pune",
            "workMode": "HYBRID",
        },
        "offer": {
            "joiningDate": "2026-08-20",
            "expiryDate": "2026-08-10",
            "benefits": [],
            "additionalTerms": [],
        },
        "company": {
            "name": "ABC Technologies",
            "address": "Pune, Maharashtra",
        },
    }

    print("\n")
    print("=" * 60)
    print("INVALID REQUEST")
    print("=" * 60)

    try:
        GenerateOfferRequest.model_validate(invalid_payload)
        print("ERROR: Invalid payload was unexpectedly accepted.")
    except ValidationError as error:
        print("Invalid payload correctly rejected.")
        print(error)


if __name__ == "__main__":
    test_valid_request()
    test_invalid_request()