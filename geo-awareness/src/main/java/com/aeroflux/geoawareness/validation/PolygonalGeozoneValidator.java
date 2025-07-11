package com.aeroflux.geoawareness.validation;

import com.aeroflux.geoawareness.dto.GeozoneDTO;
import com.aeroflux.geoawareness.dto.PolygonalGeozoneDTO;
import com.aeroflux.geoawareness.exception.geozone.IllegalGeozoneException;
import com.aeroflux.geoawareness.exception.geozone.polygonal.IllegalPolygonalGeozoneCoordinatesException;
import com.aeroflux.geoawareness.exception.geozone.polygonal.IllegalPolygonalGeozoneLatitudeException;
import com.aeroflux.geoawareness.exception.geozone.polygonal.IllegalPolygonalGeozoneLongitudeException;

public class PolygonalGeozoneValidator extends GeozoneValidator {
		
	PolygonalGeozoneValidator(
			final Range longitudeRange, 
			final Range latitudeRange, 
			final Range altitudeRange
			) {
		
		super(longitudeRange, latitudeRange, altitudeRange);
	}
			
	public boolean handles(GeozoneDTO geozone) {
		return geozone instanceof PolygonalGeozoneDTO;
	}
	
	private void validateCoordinates(final PolygonalGeozoneDTO geozone) throws IllegalPolygonalGeozoneLatitudeException, IllegalPolygonalGeozoneLongitudeException {
		assert geozone != null;
		assert geozone.getCoordinates() != null;
		assert geozone.getCoordinates().size() > 2;
		
		for (double[] coord : geozone.getCoordinates()) {
			if (!getLongitudeRange().isWithinRange(coord[0])) {
				throw new IllegalPolygonalGeozoneLongitudeException(coord[0]);
			}
			if (!getLatitudeRange().isWithinRange(coord[1])) {
				throw new IllegalPolygonalGeozoneLatitudeException(coord[1]);
			}
		}
	}
	
	@Override
	public void validate(final GeozoneDTO geozone) throws IllegalGeozoneException {
		super.validate(geozone);
		assert geozone != null;
		
		if (!(geozone instanceof PolygonalGeozoneDTO)) {
			throw new IllegalGeozoneException(geozone);
		}
		
		PolygonalGeozoneDTO polygonalGeozone = (PolygonalGeozoneDTO) geozone;
		if (polygonalGeozone.getCoordinates() == null || polygonalGeozone.getCoordinates().size() < 3) {
			throw new IllegalPolygonalGeozoneCoordinatesException();
		}
		
		validateCoordinates(polygonalGeozone);
	}
	
	@Override
	public String toString() {
		return String.format(
				"GeozoneValidator[%s]",
				super.toString()
				);
	}
}