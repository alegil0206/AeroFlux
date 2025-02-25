package com.brianzolilecchesi.geoawareness.exception.geozone.polygonal;

import com.brianzolilecchesi.geoawareness.exception.geozone.IllegalGeozoneException;
import com.brianzolilecchesi.geoawareness.util.Constants;

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