package com.brianzolilecchesi.geoawareness.exception.geozone;

import com.brianzolilecchesi.geoawareness.exception.ForbiddenException;

public class ForbiddenGeozoneUpdateException extends ForbiddenException {
	
	private static final long serialVersionUID = 1L;

	public ForbiddenGeozoneUpdateException() {
		super();
	}

	public ForbiddenGeozoneUpdateException(final String message) {
		super(message);
	}
}