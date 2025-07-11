package com.aeroflux.drone_identification.exception.drone;

import com.aeroflux.drone_identification.exception.BadRequestException;

public class IllegalDroneException extends BadRequestException {
	
	private static final long serialVersionUID = 1L;

	public IllegalDroneException(String message) {
		super(message);
	}
	
	public IllegalDroneException() {
		this("Illegal drone");
	}
}