package com.aeroflux.drone_identification.validation;

import org.springframework.stereotype.Component;

import com.aeroflux.drone_identification.dto.DroneDTO;
import com.aeroflux.drone_identification.exception.operationcategory.IllegalOperationCategoryException;
import com.aeroflux.drone_identification.registry.OperationCategoryRegistrySingleton;

@Component
public class OperationCategoryValidator {
		
	public OperationCategoryValidator() {
	}
		
	public void validate(final DroneDTO droneDTO) throws IllegalOperationCategoryException {
		assert droneDTO != null;
			
		if (droneDTO.getOperationCategory() == null) {
			throw new IllegalOperationCategoryException("operation category not provided");
		}	
		
		String operationCategory = droneDTO.getOperationCategory().toUpperCase();
		if (!OperationCategoryRegistrySingleton.getInstance().contains(operationCategory)) {
			throw new IllegalOperationCategoryException(droneDTO);
		}
		
		droneDTO.setOperationCategory(operationCategory);
	}
}