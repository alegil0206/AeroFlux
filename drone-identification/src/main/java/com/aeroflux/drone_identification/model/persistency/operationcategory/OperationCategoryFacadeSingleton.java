package com.aeroflux.drone_identification.model.persistency.operationcategory;

public class OperationCategoryFacadeSingleton {
	
	private static OperationCategoryFacade instance;
	
	private OperationCategoryFacadeSingleton() {
	}
	
	public static OperationCategoryFacade getInstance() {
		if (instance == null) {
			instance = new OperationCategoryFacade(new OperationCategoryFactory(), new OperationCategoryUpdater());
		}

		return instance;
	}
}