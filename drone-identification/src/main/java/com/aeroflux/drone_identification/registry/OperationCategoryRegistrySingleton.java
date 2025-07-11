package com.aeroflux.drone_identification.registry;

public class OperationCategoryRegistrySingleton {
	
	private static Registry instance;
	
	private OperationCategoryRegistrySingleton() {
		
	}
	
	public static Registry getInstance() {
		if (instance == null) {
			instance =  new Registry();
		}
		return instance;
	}
}