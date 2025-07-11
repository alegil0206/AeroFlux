package com.aeroflux.geoauthorization.exception.authorization;

import com.aeroflux.geoauthorization.exception.NotFoundException;

public class AuthorizationNotFoundException extends NotFoundException {
	
	private static final long serialVersionUID = 1L;
	
	public AuthorizationNotFoundException() {
		super();
	}
	
	public AuthorizationNotFoundException(Long id) {
		super(String.format("Authorization with id %s not found", id));
	}
}