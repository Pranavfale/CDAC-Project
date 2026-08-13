package com.talentbridge.exception;

public class AiPdfGenerationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AiPdfGenerationException(String message) {
        super(message);
    }

    public AiPdfGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}