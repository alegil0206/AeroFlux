package com.aeroflux.geoawareness.exception.geozone.polygonal;

import com.aeroflux.geoawareness.exception.geozone.IllegalGeozoneException;
import com.aeroflux.geoawareness.util.Constants;

public class IllegalPolygonalGeozoneLongitudeException extends IllegalGeozoneException {

	private static final long serialVersionUID = 1L;

	public IllegalPolygonalGeozoneLongitudeException(final double longitude) {
		super(String.format(
				"Illegal polygonal geozone: longitude of coordinates must in the range %s but was %f",
				Constants.LONGITUDE_RANGE.toString(),
				longitude
				));
	}
}