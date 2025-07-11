package com.aeroflux.geoawareness.exception.geozone.circular;

import com.aeroflux.geoawareness.dto.CircularGeozoneDTO;
import com.aeroflux.geoawareness.exception.geozone.IllegalGeozoneException;
import com.aeroflux.geoawareness.util.Constants;

public class IllegalCircularGeozoneLongitudeException extends IllegalGeozoneException {

	private static final long serialVersionUID = 1L;

	public IllegalCircularGeozoneLongitudeException(final CircularGeozoneDTO geozoneDTO) {
		super(String.format(
				"Illegal circular geozone: longitude must in the range %s but was %f",
				Constants.LONGITUDE_RANGE.toString(),
				geozoneDTO.getLongitude()
				));
	}
}