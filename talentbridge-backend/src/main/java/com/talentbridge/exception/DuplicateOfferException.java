package com.talentbridge.exception;

public class DuplicateOfferException
    extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateOfferException(String message) {
        super(message);
    }
}