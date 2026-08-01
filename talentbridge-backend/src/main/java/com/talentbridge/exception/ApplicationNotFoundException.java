package com.talentbridge.exception;

/**
 * Thrown when an application cannot be found.
 */
public class ApplicationNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ApplicationNotFoundException(String message) {
        super(message);
    }
}