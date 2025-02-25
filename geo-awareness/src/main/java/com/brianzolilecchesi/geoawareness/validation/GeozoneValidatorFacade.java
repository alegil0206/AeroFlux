package com.brianzolilecchesi.geoawareness.validation;

import com.brianzolilecchesi.geoawareness.dto.GeozoneDTO;
import com.brianzolilecchesi.geoawareness.exception.geozone.IllegalGeozoneException;

public class GeozoneValidatorFacade {
	
	private final CircularGeozoneValidator circularGeozoneValidator;
	private final PolygonalGeozoneValidator polygonalGeozoneValidator;
	
	GeozoneValidatorFacade(
			final CircularGeozoneValidator circularGeozoneValidator,
			final PolygonalGeozoneValidator polygonalGeozoneValidator
			) {
		
		this.circularGeozoneValidator = circularGeozoneValidator;
		this.polygonalGeozoneValidator = polygonalGeozoneValidator;
	}
	
	private GeozoneValidator getGeozoneValidator(final GeozoneDTO geozoneDTO) {
		if (circularGeozoneValidator.handles(geozoneDTO)) {
			return circularGeozoneValidator;
		} 
		if (polygonalGeozoneValidator.handles(geozoneDTO)) {
            return polygonalGeozoneValidator;
		}
			
		throw new RuntimeException("Invalid Geozone type: this should never happen!");
	}
	
	public void validate(GeozoneDTO geozoneDTO) throws IllegalGeozoneException {
		getGeozoneValidator(geozoneDTO).validate(geozoneDTO);
	}
}