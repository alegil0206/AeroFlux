package com.brianzolilecchesi.geoawareness.validation;

import java.util.Comparator;
import java.util.List;

import com.brianzolilecchesi.geoawareness.dto.GeozoneDTO;
import com.brianzolilecchesi.geoawareness.exception.geozone.IllegalGeozoneAltitudeException;
import com.brianzolilecchesi.geoawareness.exception.geozone.IllegalGeozoneCategoryException;
import com.brianzolilecchesi.geoawareness.exception.geozone.IllegalGeozoneException;
import com.brianzolilecchesi.geoawareness.exception.geozone.IllegalGeozoneStatusException;
import com.brianzolilecchesi.geoawareness.registry.RegistryFacadeSingleton;
import com.brianzolilecchesi.geoawareness.registry.RegistryMap;

public abstract class GeozoneValidator {
	
	private Range longitudeRange;
	private Range latitudeRange;
	private Range altitudeRange;
	
	public GeozoneValidator(
			final Range longitudeRange, 
			final Range latitudeRange, 
			final Range altitudeRange
			) {
		
        this.longitudeRange = longitudeRange;
        this.latitudeRange = latitudeRange;
        this.altitudeRange = altitudeRange;
	}
	
	public Range getLongitudeRange() {
		return longitudeRange;
	}
	
	public void setLongitudeRange(final Range longitudeRange) {
        this.longitudeRange = longitudeRange;
    }
	
	public Range getLatitudeRange() {
		return latitudeRange;
	}
	
	public void setLatitudeRange(final Range latitudeRange) {
		this.latitudeRange = latitudeRange;
	}
	
	public Range getAltitudeRange() {
		return altitudeRange;
	}
	
	public void setAltitudeRange(final Range altitudeRange) {
		this.altitudeRange = altitudeRange;
	}
	
	public abstract boolean handles(final GeozoneDTO geozone);
	
	protected void validate(final GeozoneDTO geozone) throws IllegalGeozoneException {
		if (geozone == null) {
			throw new IllegalGeozoneException(geozone);
		}
        
		validateCategory(geozone);
        validateStatus(geozone);
        validateAltitude(geozone);
	}
	
	private void validateCategory(final GeozoneDTO geozone) throws IllegalGeozoneCategoryException {
		assert geozone != null;
		
		if (geozone.getCategory() == null) {
			throw new IllegalGeozoneCategoryException();
		}
		
		String category = geozone.getCategory().toUpperCase();
		if (!RegistryFacadeSingleton.getInstance().getCategoryRegistry().contains(category)) {
			throw new IllegalGeozoneCategoryException(geozone.getCategory());
		}
		
		geozone.setCategory(category);
	}
	
	private void validateStatus(final GeozoneDTO geozone) throws IllegalGeozoneStatusException {
		assert geozone != null;
		
		if (geozone.getStatus() == null) {
			throw new IllegalGeozoneStatusException();
		}
		
		String status = geozone.getStatus().toUpperCase();
		if (!RegistryFacadeSingleton.getInstance().getStatusRegistry().contains(status)) {
			throw new IllegalGeozoneStatusException(geozone.getStatus());
		}
		
		geozone.setStatus(status);
	}
	
	private void validateAltitude(final GeozoneDTO geozone) throws IllegalGeozoneAltitudeException {
		assert geozone != null;
		
		if (geozone.getAltitudeLevel() == null && geozone.getAltitude() == null) {
			throw new IllegalGeozoneAltitudeException();
		}
		
		RegistryMap<Integer> registry = RegistryFacadeSingleton.getInstance().getAltitudeRegistry();
		
		if (geozone.getAltitudeLevel() != null) {
			String altitudeLevel = geozone.getAltitudeLevel().toUpperCase();
			if (!registry.contains(altitudeLevel)) {
				throw new IllegalGeozoneAltitudeException(altitudeLevel);
			}
			geozone.setAltitudeLevel(altitudeLevel);
			geozone.setAltitude(registry.get(altitudeLevel).doubleValue());
		} else {
			double altitude = geozone.getAltitude();
			if (!altitudeRange.isWithinRange(altitude))
				throw new IllegalGeozoneAltitudeException(altitude);
			
			List<Integer> altitudeValues = registry.getValues();
			altitudeValues.sort(Comparator.naturalOrder());
			int i = 0;
			while (i < altitudeValues.size() && altitudeValues.get(i) < altitude) {
				++i;
			}
			geozone.setAltitudeLevel(registry.getKey(altitudeValues.get(i)));
			geozone.setAltitude(registry.get(geozone.getAltitudeLevel()).doubleValue());
		}		
	}
	
	@Override
	public String toString() {
		return String.format(
				"GeozoneValidator[longitudeRange=%s, latitudeRange=%s, altitudeRange=%s]",
				longitudeRange, 
				latitudeRange, 
				altitudeRange
				);
	}
}