package com.brianzolilecchesi.geoauthorization.model.persistency.drone;

public class DroneFactorySingleton {
	
	private static DroneFactory instance;
	
	private DroneFactorySingleton() {
	}
	
	public static DroneFactory getInstance() {
		if (instance == null) {
			instance = new DroneFactory();
		}
		return instance;
	}	
}