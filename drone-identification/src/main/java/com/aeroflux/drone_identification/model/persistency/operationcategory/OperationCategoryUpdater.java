package com.aeroflux.drone_identification.model.persistency.operationcategory;

import com.aeroflux.drone_identification.dto.OperationCategoryDTO;

public class OperationCategoryUpdater {
	
	OperationCategoryUpdater() {
	}
	
	public void updateOperationCategory(
			final OperationCategory operationCategory, 
			final OperationCategoryDTO operationCategoryDTO
			) {
		
		assert operationCategory != null;
		assert operationCategoryDTO != null;
		assert operationCategory.getName().equalsIgnoreCase(operationCategoryDTO.getName());
		
		operationCategory.setDescription(operationCategoryDTO.getDescription());
	}
}