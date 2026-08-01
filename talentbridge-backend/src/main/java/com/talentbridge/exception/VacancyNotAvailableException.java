package com.talentbridge.exception;

/**
 * Thrown when a vacancy exists but is not currently accepting applications.
 */
public class VacancyNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public VacancyNotAvailableException(String message) {
        super(message);
    }
}