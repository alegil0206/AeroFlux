package com.aeroflux.geoauthorization.exception;

public class ForbiddenException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public ForbiddenException(String message) {
		super(message);
	}
}