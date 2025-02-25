package com.brianzolilecchesi.geoawareness.exception.geozone;

public class IllegalGeozoneStatusException extends IllegalGeozoneException {

	private static final long serialVersionUID = 1L;

	public IllegalGeozoneStatusException(final String status) {
		super(String.format(
				"Illegal geozone: the status %s is not valid",
				status
				));
	}
	
	public IllegalGeozoneStatusException() {
		super("Illegal geozone: no status provided");
	}
}