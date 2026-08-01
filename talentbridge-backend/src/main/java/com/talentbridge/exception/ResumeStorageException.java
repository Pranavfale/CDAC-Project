package com.talentbridge.exception;

/**
 * Thrown when the backend cannot store or delete a resume file.
 */
public class ResumeStorageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResumeStorageException(String message) {
        super(message);
    }

    public ResumeStorageException(
            String message,
            Throwable cause) {

        super(message, cause);
    }
}