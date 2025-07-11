package com.aeroflux.geoawareness.exception.geozone.circular;

import com.aeroflux.geoawareness.dto.CircularGeozoneDTO;
import com.aeroflux.geoawareness.exception.geozone.IllegalGeozoneException;
import com.aeroflux.geoawareness.util.Constants;

public class IllegalCircularGeozoneLatitudeException extends IllegalGeozoneException {

	private static final long serialVersionUID = 1L;
	
	public IllegalCircularGeozoneLatitudeException(final CircularGeozoneDTO geozoneDTO) {
		super(String.format(
				"Illegal circular geozone: latitude must in the range %s but was %f",
				Constants.LATITUDE_RANGE.toString(),
				geozoneDTO.getLatitude()
				));
	}
}