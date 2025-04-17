package com.brianzolilecchesi.geoawareness.exception.geozone;

import com.brianzolilecchesi.geoawareness.util.Constants;

public class IllegalGeozoneAltitudeException extends IllegalGeozoneException {

	private static final long serialVersionUID = 1L;
	
	public IllegalGeozoneAltitudeException(final double altitude) {
		super(String.format(
				"Illegal geozone: altitude must in the range %s but was %f",
				Constants.ALTITUDE_RANGE.toString(),
				altitude
				));
	}
	
	public IllegalGeozoneAltitudeException(String heightLevel) {
		super(String.format(
				"Illegal geozone: the altitude level %s is not valid",
				heightLevel
				));
	}

	public IllegalGeozoneAltitudeException(final double altitudeLimitInferior, final double altitudeLimitSuperior) {
		super(String.format(
				"Illegal geozone: altitude limit inferior %f must be greater than altitude limit superior %f",
				altitudeLimitInferior,
				altitudeLimitSuperior
				));
	}
	
	public IllegalGeozoneAltitudeException() {
		super("Illegal geozone: altitude not provided");
	}
}