package com.aeroflux.drone_identification.model.persistency.drone;

public class DroneFacade {
	
	private final DroneFactory droneFactory;
	private final DroneUpdater droneUpdater;
	
	public DroneFacade(final DroneFactory droneFactory, final DroneUpdater droneUpdater) {
		this.droneFactory = droneFactory;
		this.droneUpdater = droneUpdater;
	}
	
	public DroneFactory getDroneFactory() {
		return droneFactory;
	}
	
	public DroneUpdater getDroneUpdater() {
		return droneUpdater;
	}
}