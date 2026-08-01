package com.talentbridge.exception;

/**
 * Thrown when an uploaded resume does not satisfy validation rules.
 */
public class InvalidResumeFileException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidResumeFileException(String message) {
        super(message);
    }
}