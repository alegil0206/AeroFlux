package com.brianzolilecchesi.drone_identification.exception.position;

import com.brianzolilecchesi.drone_identification.validation.Range;

public class IllegalAltitudeException extends IllegalPositionException {

	private static final long serialVersionUID = 1L;
	
	public IllegalAltitudeException(Range range, double altitude) {
		super(String.format(
				"altitude must be in the range %s but was %f",
				range,
				altitude
				));
	}
}