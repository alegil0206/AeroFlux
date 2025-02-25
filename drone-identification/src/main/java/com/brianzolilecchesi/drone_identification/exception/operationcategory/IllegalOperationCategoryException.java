package com.brianzolilecchesi.drone_identification.exception.operationcategory;

import com.brianzolilecchesi.drone_identification.dto.DroneDTO;
import com.brianzolilecchesi.drone_identification.exception.BadRequestException;

public class IllegalOperationCategoryException extends BadRequestException {
	
	private static final long serialVersionUID = 1L;
	
	public IllegalOperationCategoryException(final DroneDTO droneDTO) {
		super(String.format(
				"Illegal drone: operation category '%s' is not valid.",
				droneDTO.getOperationCategory()
				));
	}
	
	public IllegalOperationCategoryException(String message) {
		super("Illegal operation category: " + message);
	}
}