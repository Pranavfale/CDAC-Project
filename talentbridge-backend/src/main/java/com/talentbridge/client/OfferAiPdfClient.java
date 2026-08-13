package com.talentbridge.client;

import com.talentbridge.dto.ai.request.GenerateAiPdfRequest;
import com.talentbridge.dto.ai.response.GenerateAiPdfResponse;
import com.talentbridge.exception.AiPdfGenerationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Component
public class OfferAiPdfClient {

    private final RestClient restClient;

    public OfferAiPdfClient(
            @Value("${talentbridge.ai.base-url}") String baseUrl
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public GenerateAiPdfResponse generatePdf(
            GenerateAiPdfRequest request
    ) {
        try {
            GenerateAiPdfResponse response = restClient
                    .post()
                    .uri("/api/offers/generate-ai-pdf")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(GenerateAiPdfResponse.class);

            if (response == null) {
                throw new AiPdfGenerationException(
                        "AI PDF service returned an empty response."
                );
            }

            if (!"SUCCESS".equalsIgnoreCase(response.status())) {
                throw new AiPdfGenerationException(
                        response.message() != null
                                ? response.message()
                                : "AI PDF generation failed."
                );
            }

            return response;

        } catch (RestClientResponseException exception) {
            throw new AiPdfGenerationException(
                    "AI PDF service returned HTTP "
                            + exception.getStatusCode().value(),
                    exception
            );

        } catch (RestClientException exception) {
            throw new AiPdfGenerationException(
                    "Unable to connect to AI PDF service.",
                    exception
            );
        }
    }
}