package com.aeroflux.geoauthorization.exception.authorization.forbidden;

import com.aeroflux.geoauthorization.exception.ForbiddenException;

public class ForbiddenAuthorizationRequestException extends ForbiddenException {
	
	private static final long serialVersionUID = 1L;

	public ForbiddenAuthorizationRequestException(String msg) {
		super(msg);
	}
}