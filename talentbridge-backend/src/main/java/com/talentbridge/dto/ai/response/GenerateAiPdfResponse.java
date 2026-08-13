package com.talentbridge.dto.ai.response;

public record GenerateAiPdfResponse(
        String status,
        String message,
        String aiContent,
        String fileName,
        String filePath
) {
}