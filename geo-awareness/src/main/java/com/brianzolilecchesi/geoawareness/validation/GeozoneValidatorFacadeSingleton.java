package com.brianzolilecchesi.geoawareness.validation;

public class GeozoneValidatorFacadeSingleton {
	
	private static GeozoneValidatorFacade instance;
	
	private GeozoneValidatorFacadeSingleton() {
	}
	
	public static GeozoneValidatorFacade getInstance() {
		if (instance == null) {
			instance = new GeozoneValidatorFacade(
					GeozoneValidatorFactory.createCircularGeozoneValidator(),
					GeozoneValidatorFactory.createPolygonalGeozoneValidator()
					);
		}

		return instance;
	}
}