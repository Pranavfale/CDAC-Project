from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import Any

from app.services.prompt_service import PromptMessage


@dataclass(frozen=True)
class ProviderResult:
    """
    Provider-independent result returned after JSON generation.
    """

    provider: str
    model_name: str
    content: dict[str, Any]


class AiProvider(ABC):
    """
    Common interface implemented by every supported AI provider.

    Application services depend on this interface instead of
    depending directly on the Groq SDK.
    """

    @abstractmethod
    def generate_json(
        self,
        messages: list[PromptMessage],
    ) -> ProviderResult:
        """
        Generate and return one parsed JSON object.
        """

        raise NotImplementedError