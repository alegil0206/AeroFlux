package com.aeroflux.drone_identification.exception.position;

import com.aeroflux.drone_identification.validation.Range;

public class IllegalLongitudeException extends IllegalPositionException {

	private static final long serialVersionUID = 1L;
	
	public IllegalLongitudeException(Range range, double longitude) {
		super(String.format(
				"longitude must be in the range %s but was %f",
				range,
				longitude
				));
	}
}