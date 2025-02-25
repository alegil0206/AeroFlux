package com.brianzolilecchesi.geoawareness.exception.geozone.circular;

import com.brianzolilecchesi.geoawareness.dto.CircularGeozoneDTO;
import com.brianzolilecchesi.geoawareness.exception.geozone.IllegalGeozoneException;
import com.brianzolilecchesi.geoawareness.util.Constants;

public class IllegalCircularGeozoneRadiusException extends IllegalGeozoneException {

	private static final long serialVersionUID = 1L;
	
	public IllegalCircularGeozoneRadiusException(final CircularGeozoneDTO geozoneDTO) {
		super(String.format(
				"Illegal circular geozone: radius must in the range %s but was %f",
				Constants.RADIUS_RANGE.toString(),
				geozoneDTO.getRadius()
				));
	}
}