package com.brianzolilecchesi.geoauthorization.exception.authorization.forbidden;

public class GeozoneInactiveException extends ForbiddenAuthorizationRequestException {
	
	private static final long serialVersionUID = 1L;

	public GeozoneInactiveException(String message) {
		super(message);
	}	
}