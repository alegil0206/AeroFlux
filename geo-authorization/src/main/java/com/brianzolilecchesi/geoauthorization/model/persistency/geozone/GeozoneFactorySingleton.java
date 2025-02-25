package com.brianzolilecchesi.geoauthorization.model.persistency.geozone;

public class GeozoneFactorySingleton {
	
	private static GeozoneFactory instance;
	
	private GeozoneFactorySingleton() {
	}
	
	public static GeozoneFactory getInstance() {
		if (instance == null) {
			instance = new GeozoneFactory();
		}
		return instance;
	}	
}