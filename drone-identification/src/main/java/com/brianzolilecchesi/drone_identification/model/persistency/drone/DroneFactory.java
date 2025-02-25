package com.brianzolilecchesi.drone_identification.model.persistency.drone;

import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.brianzolilecchesi.drone_identification.dto.DroneDTO;
import com.brianzolilecchesi.drone_identification.dto.PositionDTO;

public class DroneFactory {
		
	DroneFactory() {
	}
	
	public Drone createDrone(final DroneDTO droneDTO) {
		assert droneDTO != null;
		
		Drone drone = new Drone(
				droneDTO.getName(), 
                droneDTO.getModel(), 
                droneDTO.getOwner(), 
                droneDTO.getOperationCategory(), 
                LocalDateTime.now(),
                new GeoJsonPoint(
                		droneDTO.getSource().getLongitude(),
                		droneDTO.getSource().getLatitude()
                		),
                new GeoJsonPoint(
						droneDTO.getDestination().getLongitude(), 
						droneDTO.getDestination().getLatitude()
						)
                );
		
		return drone;
	}
	
	public DroneDTO createDroneDTO(final Drone drone) {
		DroneDTO droneDTO = new DroneDTO(
				drone.getId(), 
				drone.getName(), 
				drone.getModel(), 
				drone.getOwner(),
				drone.getOperationCategory(), 
				drone.getPlanDefinitionTimestamp().toString(),
				new PositionDTO(
						drone.getSource().getX(),
						drone.getSource().getY() 
						),
				new PositionDTO(
						drone.getDestination().getX(), 
						drone.getDestination().getY() 
						)
				);

		return droneDTO;
	}
}