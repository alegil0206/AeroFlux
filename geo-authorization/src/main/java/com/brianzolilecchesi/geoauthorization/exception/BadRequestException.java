package com.brianzolilecchesi.geoauthorization.exception;

public class BadRequestException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public BadRequestException(final String message) {
		super(message);
	}
}