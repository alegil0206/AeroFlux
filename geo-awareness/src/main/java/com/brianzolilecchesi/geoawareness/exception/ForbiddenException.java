package com.brianzolilecchesi.geoawareness.exception;

public class ForbiddenException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ForbiddenException() {
		super();
	}

	public ForbiddenException(final String message) {
		super(message);
	}
}