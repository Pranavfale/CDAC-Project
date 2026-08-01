package com.talentbridge.exception;

/**
 * Thrown when a candidate attempts to create more than one profile.
 */
public class DuplicateCandidateProfileException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateCandidateProfileException(String message) {
        super(message);
    }
}