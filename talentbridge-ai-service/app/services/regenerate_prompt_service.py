import json

from app.prompts.regenerate_prompt import (
    REGENERATE_SYSTEM_PROMPT,
)
from app.schemas.regenerate_schemas import (
    RegenerateOfferRequest,
)
from app.services.prompt_service import (
    PromptMessage,
    prompt_service,
)


class RegeneratePromptService:
    """
    Builds controlled provider messages for complete offer
    regeneration.
    """

    def build_regeneration_messages(
        self,
        request: RegenerateOfferRequest,
    ) -> list[PromptMessage]:
        """
        Build system and user messages using verified facts,
        previous content, and an optional HR instruction.
        """

        verified_facts = prompt_service.build_verified_facts(
            request
        )

        verified_facts_json = json.dumps(
            verified_facts,
            ensure_ascii=False,
            indent=2,
        )

        previous_content_json = (
            request.previousContent.model_dump_json(
                indent=2,
            )
        )

        instruction = request.instruction

        if instruction is None:
            instruction = (
                "Create a professionally worded alternative draft "
                "while preserving every verified fact."
            )

        user_message = (
            "Create one complete replacement offer-letter draft.\n\n"
            "VERIFIED_FACTS_JSON_START\n"
            f"{verified_facts_json}\n"
            "VERIFIED_FACTS_JSON_END\n\n"
            "PREVIOUS_CONTENT_JSON_START\n"
            f"{previous_content_json}\n"
            "PREVIOUS_CONTENT_JSON_END\n\n"
            "HR_INSTRUCTION_START\n"
            f"{instruction}\n"
            "HR_INSTRUCTION_END"
        )

        return [
            {
                "role": "system",
                "content": REGENERATE_SYSTEM_PROMPT,
            },
            {
                "role": "user",
                "content": user_message,
            },
        ]


regenerate_prompt_service = RegeneratePromptService()