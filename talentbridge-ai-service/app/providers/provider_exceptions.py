class AiProviderError(Exception):
    """
    Base class for controlled AI-provider errors.
    """

    def __init__(
        self,
        message: str,
        error_code: str,
        retryable: bool,
    ) -> None:
        super().__init__(message)

        self.error_code = error_code
        self.retryable = retryable


class ProviderConfigurationError(AiProviderError):
    """
    Raised when provider configuration is missing or invalid.
    """

    def __init__(
        self,
        message: str = "AI provider configuration is invalid.",
    ) -> None:
        super().__init__(
            message=message,
            error_code="AI_PROVIDER_CONFIGURATION_ERROR",
            retryable=False,
        )


class ProviderAuthenticationError(AiProviderError):
    """
    Raised when the provider rejects its API credentials.
    """

    def __init__(self) -> None:
        super().__init__(
            message="AI provider authentication failed.",
            error_code="AI_PROVIDER_AUTHENTICATION_FAILED",
            retryable=False,
        )


class ProviderTimeoutError(AiProviderError):
    """
    Raised when the provider request exceeds its timeout.
    """

    def __init__(self) -> None:
        super().__init__(
            message="The AI provider request timed out.",
            error_code="AI_PROVIDER_TIMEOUT",
            retryable=True,
        )


class ProviderRateLimitError(AiProviderError):
    """
    Raised when the provider applies rate limiting.
    """

    def __init__(self) -> None:
        super().__init__(
            message="The AI provider rate limit was reached.",
            error_code="AI_PROVIDER_RATE_LIMIT",
            retryable=True,
        )


class ProviderConnectionError(AiProviderError):
    """
    Raised when the service cannot connect to the provider.
    """

    def __init__(self) -> None:
        super().__init__(
            message="The AI provider could not be reached.",
            error_code="AI_PROVIDER_CONNECTION_ERROR",
            retryable=True,
        )


class ProviderApiError(AiProviderError):
    """
    Raised for other unsuccessful provider responses.
    """

    def __init__(
        self,
        retryable: bool,
    ) -> None:
        super().__init__(
            message="The AI provider returned an unsuccessful response.",
            error_code="AI_PROVIDER_ERROR",
            retryable=retryable,
        )


class ProviderResponseError(AiProviderError):
    """
    Raised when provider output is empty or not a JSON object.
    """

    def __init__(self) -> None:
        super().__init__(
            message="The AI provider returned an invalid response.",
            error_code="AI_PROVIDER_RESPONSE_INVALID",
            retryable=False,
        )