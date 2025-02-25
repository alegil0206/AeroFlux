package com.brianzolilecchesi.drone_identification.model.persistency.operationcategory;

import com.brianzolilecchesi.drone_identification.dto.OperationCategoryDTO;

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