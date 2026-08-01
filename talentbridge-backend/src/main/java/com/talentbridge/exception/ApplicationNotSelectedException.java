package com.talentbridge.exception;

public class ApplicationNotSelectedException
    extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ApplicationNotSelectedException(String message) {
        super(message);
    }
}