package com.talentbridge.exception;

/**
 * Thrown when the requested vacancy does not exist.
 */
public class VacancyNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public VacancyNotFoundException(String message) {
        super(message);
    }
}