package com.brianzolilecchesi.drone_identification.model.persistency.operationcategory;

public class OperationCategoryFacade {
	
	private final OperationCategoryFactory operationCategoryFactory;
	private final OperationCategoryUpdater operationCategoryUpdater;
	
	public OperationCategoryFacade(
			final OperationCategoryFactory operationCategoryFactory, 
			final OperationCategoryUpdater operationCategoryUpdater
			) {
		
		this.operationCategoryFactory = operationCategoryFactory;
		this.operationCategoryUpdater = operationCategoryUpdater;
	}
	
	public OperationCategoryFactory getOperationCategoryFactory() {
		return operationCategoryFactory;
	}
	
	public OperationCategoryUpdater getOperationCategoryUpdater() {
		return operationCategoryUpdater;
	}
}