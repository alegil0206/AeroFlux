package com.brianzolilecchesi.drone_identification.model.persistency.drone;

public class DroneFacadeSingleton {
	
	private static DroneFacade instance;
	
	private DroneFacadeSingleton() {
	}
	
	public static DroneFacade getInstance() {
		if (instance == null) {
			instance = new DroneFacade(new DroneFactory(), new DroneUpdater());
		}

		return instance;
	}
}