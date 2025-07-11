package com.aeroflux.geoawareness.exception.geozone.polygonal;

import com.aeroflux.geoawareness.exception.geozone.IllegalGeozoneException;
import com.aeroflux.geoawareness.util.Constants;

public class IllegalPolygonalGeozoneLatitudeException extends IllegalGeozoneException {

	private static final long serialVersionUID = 1L;
	
	public IllegalPolygonalGeozoneLatitudeException(final double latitude) {
		super(String.format(
				"Illegal polygonal geozone: latitude of coordinates must in the range %s but was %f",
				Constants.LATITUDE_RANGE.toString(),
				latitude
				));
	}
}