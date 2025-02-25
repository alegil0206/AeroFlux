package com.brianzolilecchesi.drone_identification.model.persistency.operationcategory;

import com.brianzolilecchesi.drone_identification.dto.OperationCategoryDTO;

public class OperationCategoryFactory {
		
	OperationCategoryFactory() {
	}
	
	public OperationCategory createOperationCategory(final OperationCategoryDTO operationCategoryDTO) {
		assert operationCategoryDTO != null;
		return new OperationCategory(
				operationCategoryDTO.getName().toUpperCase(),
				operationCategoryDTO.getDescription()
				);		
	}
	
	public OperationCategoryDTO createOperationCategoryDTO(final OperationCategory operationCategory) {
		assert operationCategory != null;
		return new OperationCategoryDTO(
				operationCategory.getName(), 
				operationCategory.getDescription()
				);
	}
}