package com.talentbridge.exception;

public class InactiveUserException extends RuntimeException {

	public InactiveUserException(String message) {
		super(message);
	}
}