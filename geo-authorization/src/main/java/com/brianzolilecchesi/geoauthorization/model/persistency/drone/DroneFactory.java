package com.brianzolilecchesi.geoauthorization.model.persistency.drone;

import com.brianzolilecchesi.geoauthorization.dto.PublishedDroneDTO;

public class DroneFactory {
	
	DroneFactory() {
	}
	
	public Drone createDrone(final PublishedDroneDTO droneDTO) {
		assert droneDTO != null;
		return new Drone(droneDTO.getId(), droneDTO.getOperationCategory());
				
	}
}