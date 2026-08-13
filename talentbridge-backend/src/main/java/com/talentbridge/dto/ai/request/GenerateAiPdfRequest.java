package com.talentbridge.dto.ai.request;

import java.util.List;

public record GenerateAiPdfRequest(
        String offerId,
        String candidateName,
        String jobTitle,
        String department,
        String salary,
        String joiningDate,
        String workLocation,
        String companyName,
        String companyAddress,
        String hrName,
        List<String> benefits,
        List<String> additionalTerms
) {
}