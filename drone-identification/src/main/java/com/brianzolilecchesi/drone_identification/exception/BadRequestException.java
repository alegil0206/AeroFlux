package com.brianzolilecchesi.drone_identification.exception;

public class BadRequestException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public BadRequestException(final String message) {
		super(message);
	}
}