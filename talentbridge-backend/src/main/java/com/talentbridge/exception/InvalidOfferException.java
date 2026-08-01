package com.talentbridge.exception;

public class InvalidOfferException
    extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidOfferException(String message) {
        super(message);
    }
}