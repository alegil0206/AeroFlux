package com.brianzolilecchesi.geoawareness.exception.geozone.polygonal;

import com.brianzolilecchesi.geoawareness.exception.geozone.IllegalGeozoneException;

public class IllegalPolygonalGeozoneCoordinatesException extends IllegalGeozoneException {
	
	private static final long serialVersionUID = 1L;

	public IllegalPolygonalGeozoneCoordinatesException() {
		super("Illegal polygonal geozone: no coordinates provided");
	}
}