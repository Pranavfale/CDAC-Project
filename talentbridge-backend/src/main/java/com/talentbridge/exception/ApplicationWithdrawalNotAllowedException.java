package com.talentbridge.exception;

/**
 * Thrown when an application cannot be moved to WITHDRAWN from its
 * current status.
 */
public class ApplicationWithdrawalNotAllowedException
        extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ApplicationWithdrawalNotAllowedException(
            String message) {

        super(message);
    }
}