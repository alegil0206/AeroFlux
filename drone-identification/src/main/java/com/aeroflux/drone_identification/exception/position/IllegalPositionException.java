package com.aeroflux.drone_identification.exception.position;

import com.aeroflux.drone_identification.exception.BadRequestException;

public class IllegalPositionException extends BadRequestException {

	private static final long serialVersionUID = 1L;

	public IllegalPositionException(String message) {
		super(message);
	}
	
	public IllegalPositionException(String positionName, String message) {
		super(String.format("Illegal %s: %s", positionName, message));
	}
}