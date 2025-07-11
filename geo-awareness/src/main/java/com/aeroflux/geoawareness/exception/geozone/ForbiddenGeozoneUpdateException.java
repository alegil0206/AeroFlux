package com.aeroflux.geoawareness.exception.geozone;

import com.aeroflux.geoawareness.exception.ForbiddenException;

public class ForbiddenGeozoneUpdateException extends ForbiddenException {
	
	private static final long serialVersionUID = 1L;

	public ForbiddenGeozoneUpdateException() {
		super();
	}

	public ForbiddenGeozoneUpdateException(final String message) {
		super(message);
	}
}