package com.aeroflux.geoawareness.exception.geozone;

public class IllegalGeozoneCategoryException extends IllegalGeozoneException {

	private static final long serialVersionUID = 1L;

	public IllegalGeozoneCategoryException(final String category) {
		super(String.format(
				"Illegal geozone: the category %s is not valid",
				category
				));
	}
	
	public IllegalGeozoneCategoryException() {
		super("Illegal geozone: no category specified");
	}
}