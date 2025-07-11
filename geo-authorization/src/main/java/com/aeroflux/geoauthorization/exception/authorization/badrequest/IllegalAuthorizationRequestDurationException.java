package com.aeroflux.geoauthorization.exception.authorization.badrequest;

import com.aeroflux.geoauthorization.exception.BadRequestException;

public class IllegalAuthorizationRequestDurationException extends BadRequestException {

	private static final long serialVersionUID = 1L;
	
	public IllegalAuthorizationRequestDurationException(String duration) {
		super(String.format(
				"Illegal authorization request: duration %s is not valid",
				duration
				));
	}
	
	public IllegalAuthorizationRequestDurationException() {
		super("Illegal authorization request: no duration specified");
	}
}