package com.aeroflux.drone_identification.exception.position;

import com.aeroflux.drone_identification.validation.Range;

public class IllegalLatitudeException extends IllegalPositionException {

	private static final long serialVersionUID = 1L;
	
	public IllegalLatitudeException(Range range, double latitude) {
		super(String.format(
				"latitude must be in the range %s but was %f",
				range,
				latitude
				));
	}
}