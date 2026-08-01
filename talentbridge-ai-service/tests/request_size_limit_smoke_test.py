from fastapi import FastAPI, Request
from fastapi.testclient import TestClient

from app.middleware.request_size_limit_middleware import (
    RequestSizeLimitMiddleware,
)


def create_test_client() -> TestClient:
    """
    Create a small application for deterministic middleware tests.
    """

    app = FastAPI()

    app.add_middleware(
        RequestSizeLimitMiddleware,
        max_body_bytes=16,
    )

    @app.post("/internal/api/v1/test")
    async def protected_test(
        request: Request,
    ) -> dict:
        body = await request.body()

        return {
            "status": "SUCCESS",
            "receivedBytes": len(body),
        }

    @app.post("/public/test")
    async def public_test(
        request: Request,
    ) -> dict:
        body = await request.body()

        return {
            "status": "SUCCESS",
            "receivedBytes": len(body),
        }

    return TestClient(app)


def test_request_within_limit() -> None:
    """
    Verify a small protected request is accepted.
    """

    client = create_test_client()

    response = client.post(
        "/internal/api/v1/test",
        content=b"12345678",
        headers={
            "Content-Type": (
                "application/octet-stream"
            ),
            "X-Correlation-ID": (
                "corr-request-size-1"
            ),
        },
    )

    assert response.status_code == 200

    body = response.json()

    assert body["status"] == "SUCCESS"
    assert body["receivedBytes"] == 8

    print("=" * 70)
    print("REQUEST WITHIN LIMIT TEST PASSED")
    print("=" * 70)


def test_oversized_request_rejected() -> None:
    """
    Verify an oversized protected request is rejected.
    """

    client = create_test_client()

    response = client.post(
        "/internal/api/v1/test",
        content=b"x" * 17,
        headers={
            "Content-Type": (
                "application/octet-stream"
            ),
            "X-Correlation-ID": (
                "corr-request-size-2"
            ),
        },
    )

    assert response.status_code == 413

    body = response.json()

    assert body["status"] == "ERROR"
    assert body["errorCode"] == (
        "AI_REQUEST_BODY_TOO_LARGE"
    )
    assert body["correlationId"] == (
        "corr-request-size-2"
    )
    assert body["maxBodyBytes"] == 16

    assert response.headers[
        "X-Correlation-ID"
    ] == "corr-request-size-2"

    print("=" * 70)
    print("OVERSIZED REQUEST TEST PASSED")
    print("=" * 70)


def test_public_request_not_limited() -> None:
    """
    Verify the internal API limit does not affect public routes.
    """

    client = create_test_client()

    response = client.post(
        "/public/test",
        content=b"x" * 100,
    )

    assert response.status_code == 200
    assert response.json()["receivedBytes"] == 100

    print("=" * 70)
    print("PUBLIC ROUTE TEST PASSED")
    print("=" * 70)


if __name__ == "__main__":
    test_request_within_limit()
    test_oversized_request_rejected()
    test_public_request_not_limited()