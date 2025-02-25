package com.brianzolilecchesi.geoauthorization.exception.authorization.forbidden;

import com.brianzolilecchesi.geoauthorization.exception.ForbiddenException;

public class ForbiddenAuthorizationRequestException extends ForbiddenException {
	
	private static final long serialVersionUID = 1L;

	public ForbiddenAuthorizationRequestException(String msg) {
		super(msg);
	}
}