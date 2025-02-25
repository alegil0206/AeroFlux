package com.brianzolilecchesi.drone_identification.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brianzolilecchesi.drone_identification.dto.DroneDTO;
import com.brianzolilecchesi.drone_identification.exception.drone.IllegalDroneException;
import com.brianzolilecchesi.drone_identification.exception.operationcategory.IllegalOperationCategoryException;
import com.brianzolilecchesi.drone_identification.exception.position.IllegalPositionException;

@Component
public class DroneValidator {
	
	private static final Range LONGITUDE_RANGE = new Range(-180, 180);
	private static final Range LATITUDE_RANGE = new Range(-90, 90);
	private static final Range ALTITUDE_RANGE = new Range(0, 120);
	
	private final PositionValidator positionValidator;
	private final OperationCategoryValidator operationCategoryValidator;
	
	@Autowired
	public DroneValidator(
			final OperationCategoryValidator operationCategoryValidator
			) {
		this.positionValidator = new PositionValidator(
				LONGITUDE_RANGE,
				LATITUDE_RANGE,
				ALTITUDE_RANGE
                );
		this.operationCategoryValidator = operationCategoryValidator;
	}
			
	public void validate(final DroneDTO droneDTO) throws IllegalDroneException, IllegalOperationCategoryException, IllegalPositionException {
		if (droneDTO == null) {
			throw new IllegalDroneException("Illegal drone: no drone provided");
		}
		
		try {
			positionValidator.isValid(droneDTO.getSource());
		} catch (IllegalPositionException e) {
			throw new IllegalPositionException("Source", e.getMessage());
		}
		
		try {
			positionValidator.isValid(droneDTO.getDestination());
		} catch (IllegalPositionException e) {
			throw new IllegalPositionException("Destination", e.getMessage());
		}
		
		operationCategoryValidator.validate(droneDTO);
	}	
}