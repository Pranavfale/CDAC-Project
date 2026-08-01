package com.talentbridge.exception;

/**
 * Thrown when a candidate has not uploaded a resume or the stored file
 * no longer exists.
 */
public class ResumeNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResumeNotFoundException(String message) {
        super(message);
    }
}