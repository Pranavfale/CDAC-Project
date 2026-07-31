OFFER_PROMPT_V1 = "offer-v1"


SUPPORTED_OFFER_PROMPT_VERSIONS: frozenset[str] = frozenset(
    {
        OFFER_PROMPT_V1,
    }
)


def is_supported_offer_prompt_version(
    prompt_version: str,
) -> bool:
    """
    Return True when the supplied offer prompt version is supported.
    """

    return prompt_version in SUPPORTED_OFFER_PROMPT_VERSIONS