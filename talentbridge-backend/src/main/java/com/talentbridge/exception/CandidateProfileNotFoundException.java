package com.talentbridge.exception;

/**
 * Thrown when the authenticated candidate does not have a profile.
 */
public class CandidateProfileNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CandidateProfileNotFoundException(String message) {
        super(message);
    }
}