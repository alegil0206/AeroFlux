package com.aeroflux.geoawareness.validation;

import com.aeroflux.geoawareness.dto.CircularGeozoneDTO;
import com.aeroflux.geoawareness.dto.GeozoneDTO;
import com.aeroflux.geoawareness.exception.geozone.IllegalGeozoneException;
import com.aeroflux.geoawareness.exception.geozone.circular.IllegalCircularGeozoneLatitudeException;
import com.aeroflux.geoawareness.exception.geozone.circular.IllegalCircularGeozoneLongitudeException;
import com.aeroflux.geoawareness.exception.geozone.circular.IllegalCircularGeozoneRadiusException;

public class CircularGeozoneValidator extends GeozoneValidator {
	
	private Range radiusRange;
	
	public CircularGeozoneValidator(
			final Range longitudeRange, 
			final Range latitudeRange, 
			final Range radiusRange, 
			final Range altitudeRange
			) {
		
		super(longitudeRange, latitudeRange, altitudeRange);
		
        this.radiusRange = radiusRange;
	}
		
	public Range getRadiusRange() {
		return radiusRange;
	}
	
	public void setRadiusRange(final Range radiusRange) {
		this.radiusRange = radiusRange;
	}
	
	@Override
	public boolean handles(GeozoneDTO geozone) {
		return geozone instanceof CircularGeozoneDTO;
	}
	
	private void validateLongitude(final CircularGeozoneDTO geozone) throws IllegalCircularGeozoneLongitudeException {
		assert geozone != null;
		
		if (!getLongitudeRange().isWithinRange(geozone.getLongitude())) {
			throw new IllegalCircularGeozoneLongitudeException(geozone);
		}
	}
	
	private void validateLatitude(final CircularGeozoneDTO geozone) throws IllegalCircularGeozoneLatitudeException {
		assert geozone != null;
		
		if (!getLatitudeRange().isWithinRange(geozone.getLatitude())) {
			throw new IllegalCircularGeozoneLatitudeException(geozone);
		}
	}
	
	private void validateRadius(final CircularGeozoneDTO geozone) throws IllegalCircularGeozoneRadiusException {
		assert geozone != null;
		
		if (!radiusRange.isWithinRange(geozone.getRadius())) {
            throw new IllegalCircularGeozoneRadiusException(geozone);
        }
	}
		
	@Override
	public void validate(final GeozoneDTO geozone) throws IllegalGeozoneException {
		super.validate(geozone);
		assert geozone != null;
		
		if (!(geozone instanceof CircularGeozoneDTO)) {
			throw new IllegalGeozoneException(geozone);
		}		
		
		CircularGeozoneDTO circularGeozone = (CircularGeozoneDTO) geozone;
		validateLongitude(circularGeozone);
        validateLatitude(circularGeozone);
        validateRadius(circularGeozone);
	}
	
	@Override
	public String toString() {
		return String.format(
				"GeozoneValidator[%s, radiusRange=%s]",
				super.toString(),
				radiusRange 
				);
	}
}