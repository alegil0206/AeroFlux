package com.aeroflux.geoawareness.exception.geozone;

import com.aeroflux.geoawareness.dto.GeozoneDTO;
import com.aeroflux.geoawareness.exception.BadRequestException;

public class IllegalGeozoneException extends BadRequestException {
	
	private static final long serialVersionUID = 1L;
	
	public IllegalGeozoneException() {
		super("Illegal geozone: no geozone provided");
	}
	
	public IllegalGeozoneException(final String message) {
		super(message);
	}

	public IllegalGeozoneException(final GeozoneDTO geozoneDTO) {
		super("Illegal geozone: " + geozoneDTO.toString());
	}
}