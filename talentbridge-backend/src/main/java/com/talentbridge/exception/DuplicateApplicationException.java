package com.talentbridge.exception;

/**
 * Thrown when a candidate attempts to apply to the same vacancy twice.
 */
public class DuplicateApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateApplicationException(String message) {
        super(message);
    }
}