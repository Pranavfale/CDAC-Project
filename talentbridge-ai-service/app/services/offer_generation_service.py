from datetime import date, datetime

from app.schemas.request_schemas import GenerateOfferRequest
from app.schemas.response_schemas import GenerateOfferSuccessResponse


class MockOfferGenerationService:
    """
    Builds deterministic structured offer content from supplied facts.

    This temporary implementation does not call an AI provider.
    It allows the API contract, security, validation, and Spring Boot
    integration to be tested before Groq integration is added.
    """

    @staticmethod
    def format_date(value: date) -> str:
        """
        Convert an ISO date into a readable offer-letter date.
        """

        return value.strftime("%d %B %Y")

    @staticmethod
    def build_terms(request: GenerateOfferRequest) -> list[str]:
        """
        Include only terms and benefits supplied in the request.

        No policy, benefit, or business condition is invented.
        """

        terms = list(request.offer.additionalTerms)

        for benefit in request.offer.benefits:
            terms.append(f"Approved benefit: {benefit}")

        return terms

    def generate(
        self,
        request: GenerateOfferRequest,
    ) -> GenerateOfferSuccessResponse:
        """
        Build a valid mock offer-generation response.
        """

        candidate_name = request.candidate.name
        position_title = request.position.title
        company_name = request.company.name

        joining_date = self.format_date(
            request.offer.joiningDate
        )

        expiry_date = self.format_date(
            request.offer.expiryDate
        )

        return GenerateOfferSuccessResponse(
            requestId=request.requestId,
            status="SUCCESS",
            provider="mock",
            modelName="mock-offer-generator-v1",
            promptVersion=request.promptVersion,
            content={
                "documentTitle": "Offer of Employment",
                "subject": (
                    f"Offer for the Position of {position_title}"
                ),
                "salutation": f"Dear {candidate_name},",
                "introduction": (
                    f"We are pleased to offer you the position of "
                    f"{position_title} at {company_name}."
                ),
                "positionDetails": {
                    "position": position_title,
                    "department": request.position.department,
                    "employmentType": (
                        request.position.employmentType.value
                    ),
                    "workLocation": (
                        request.position.workLocation
                    ),
                    "workMode": request.position.workMode.value,
                },
                "compensationSection": (
                    f"Your approved compensation will be "
                    f"{request.offer.offeredCtc}."
                ),
                "joiningSection": (
                    f"Your proposed joining date is {joining_date}."
                ),
                "termsAndConditions": self.build_terms(request),
                "acceptanceSection": (
                    f"Please confirm your acceptance through the "
                    f"TalentBridge candidate portal on or before "
                    f"{expiry_date}."
                ),
                "closing": (
                    f"Sincerely,\n"
                    f"Human Resources\n"
                    f"{company_name}"
                ),
            },
            missingFields=[],
            receivedAt=datetime.now().astimezone(),
        )


mock_offer_generation_service = MockOfferGenerationService()