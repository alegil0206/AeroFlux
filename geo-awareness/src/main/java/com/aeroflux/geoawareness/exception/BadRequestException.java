package com.aeroflux.geoawareness.exception;

public class BadRequestException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public BadRequestException(final String message) {
		super(message);
	}
}