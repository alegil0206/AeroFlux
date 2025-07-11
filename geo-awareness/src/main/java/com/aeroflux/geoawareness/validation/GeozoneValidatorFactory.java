package com.aeroflux.geoawareness.validation;

import com.aeroflux.geoawareness.util.Constants;

class GeozoneValidatorFactory {
	
	private GeozoneValidatorFactory() {
	}
	
	static CircularGeozoneValidator createCircularGeozoneValidator() {
		return new CircularGeozoneValidator(
				Constants.LONGITUDE_RANGE, 
                Constants.LATITUDE_RANGE, 
                Constants.RADIUS_RANGE, 
                Constants.ALTITUDE_RANGE
                );
	}
	
	static PolygonalGeozoneValidator createPolygonalGeozoneValidator() {
		return new PolygonalGeozoneValidator(
				Constants.LONGITUDE_RANGE, 
                Constants.LATITUDE_RANGE, 
                Constants.ALTITUDE_RANGE
                );
	}
}