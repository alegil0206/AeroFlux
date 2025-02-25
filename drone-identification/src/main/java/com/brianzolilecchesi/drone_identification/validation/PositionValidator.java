package com.brianzolilecchesi.drone_identification.validation;

import com.brianzolilecchesi.drone_identification.dto.PositionDTO;
import com.brianzolilecchesi.drone_identification.exception.position.IllegalAltitudeException;
import com.brianzolilecchesi.drone_identification.exception.position.IllegalLatitudeException;
import com.brianzolilecchesi.drone_identification.exception.position.IllegalLongitudeException;
import com.brianzolilecchesi.drone_identification.exception.position.IllegalPositionException;

class PositionValidator {
	
	private Range longitudeRange;
	private Range latitudeRange;
	private Range altitudeRange;
	
	PositionValidator(final Range longitudeRange, final Range latitudeRange, final Range altitudeRange) {
        setLongitudeRange(longitudeRange);
        setLatitudeRange(latitudeRange);
        setAltitudeRange(altitudeRange);
	}
	
	Range getLongitudeRange() {
		return longitudeRange;
	}
	
	void setLongitudeRange(Range longitudeRange) {
		this.longitudeRange = longitudeRange;
	}
	
	Range getLatitudeRange() {
		return latitudeRange;
	}
	
	void setLatitudeRange(Range latitudeRange) {
		this.latitudeRange = latitudeRange;
	}
	
	Range getAltitudeRange() {
		return altitudeRange;
	}
	
	void setAltitudeRange(Range heightRange) {
		this.altitudeRange = heightRange;
	}
	
	void isValid(
			final double longitude, 
			final double latitude, 
			final double altitude
			) throws IllegalPositionException {
		
		if (!longitudeRange.isWithinRange(longitude))
			throw new IllegalLongitudeException(longitudeRange, longitude);
		
		if (!latitudeRange.isWithinRange(latitude))
			throw new IllegalLatitudeException(latitudeRange, latitude);
		
		if (!altitudeRange.isWithinRange(altitude))
			throw new IllegalAltitudeException(altitudeRange, altitude);
	}
	
	void isValid(final PositionDTO position) throws IllegalPositionException {
		if (position == null)
			throw new IllegalPositionException("position not specified");
		
        isValid(position.getLongitude(), position.getLatitude(), position.getAltitude());
	}
	
	@Override
	public String toString() {
		return String.format(
				"PositionValidator[longitudeRange=%s, latitudeRange=%s, altitudeRange=%s]", 
				longitudeRange,
				latitudeRange, 
				altitudeRange
				);
	}
}