from app.middleware.observability_rate_limit_middleware import (
    InMemoryFixedWindowRateLimiter,
    normalize_correlation_id,
)


def test_rate_limit() -> None:
    """
    Verify requests are blocked after the configured limit.
    """

    current_time = [100.0]

    limiter = InMemoryFixedWindowRateLimiter(
        max_requests=2,
        window_seconds=10,
        clock=lambda: current_time[0],
    )

    first = limiter.check("spring-boot-service")
    second = limiter.check("spring-boot-service")
    third = limiter.check("spring-boot-service")

    assert first.allowed is True
    assert first.remaining == 1

    assert second.allowed is True
    assert second.remaining == 0

    assert third.allowed is False
    assert third.remaining == 0
    assert third.retry_after_seconds == 10

    current_time[0] = 110.1

    fourth = limiter.check("spring-boot-service")

    assert fourth.allowed is True
    assert fourth.remaining == 1

    print("=" * 70)
    print("RATE LIMIT TEST PASSED")
    print("=" * 70)


def test_safe_correlation_id() -> None:
    """
    Verify valid correlation IDs are retained and unsafe IDs
    are replaced.
    """

    valid_value = "corr-offer-101"

    assert (
        normalize_correlation_id(valid_value)
        == valid_value
    )

    unsafe_value = (
        "corr-value\nFAKE_LOG_ENTRY=secret"
    )

    generated_value = normalize_correlation_id(
        unsafe_value
    )

    assert generated_value != unsafe_value
    assert "\n" not in generated_value
    assert len(generated_value) <= 128

    missing_value = normalize_correlation_id(
        None
    )

    assert missing_value
    assert len(missing_value) <= 128

    print("=" * 70)
    print("CORRELATION ID TEST PASSED")
    print("=" * 70)


if __name__ == "__main__":
    test_rate_limit()
    test_safe_correlation_id()