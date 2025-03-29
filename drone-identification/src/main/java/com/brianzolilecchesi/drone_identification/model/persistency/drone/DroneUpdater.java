package com.brianzolilecchesi.drone_identification.model.persistency.drone;

import java.time.LocalDateTime;

import com.brianzolilecchesi.drone_identification.dto.DroneDTO;

public class DroneUpdater {
	
	DroneUpdater() {
	}
	
	public void updateDrone(final Drone drone, final DroneDTO droneDTO) {
		assert drone != null;
		assert droneDTO != null;
		
		drone.setName(droneDTO.getName());
		drone.setModel(droneDTO.getModel());
		drone.setOwner(droneDTO.getOwner());
		drone.setBattery(droneDTO.getBattery());
		drone.setAdaptiveCapabilities(droneDTO.getAdaptiveCapabilities());
				
		if (!(
				droneDTO.getOperationCategory().equals(drone.getOperationCategory()) &&
				drone.isSameSource(droneDTO.getSource()) &&
				drone.isSameDestination(droneDTO.getDestination())
			)) {
			
			drone.setOperationCategory(droneDTO.getOperationCategory());
			drone.setSource(droneDTO.getSource());
			drone.setDestination(droneDTO.getDestination());
			
			drone.setPlanDefinitionTimestamp(LocalDateTime.now());
		}
	}
}