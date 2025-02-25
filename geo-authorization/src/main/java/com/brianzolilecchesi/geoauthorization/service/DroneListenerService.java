package com.brianzolilecchesi.geoauthorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.brianzolilecchesi.geoauthorization.config.DroneJmsConfig;
import com.brianzolilecchesi.geoauthorization.dto.PublishedDroneDTO;
import com.brianzolilecchesi.geoauthorization.exception.NotFoundException;
import com.brianzolilecchesi.geoauthorization.model.persistency.drone.Drone;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DroneListenerService {
	
	private final AuthorizationService authorizationService;
	private final DroneService droneService;
	@SuppressWarnings("unused")
	private final DroneJmsConfig jmsConfig;
	
	private final ObjectMapper objectMapper;
	
	@Autowired
	public DroneListenerService(
			final AuthorizationService authorizationService,
			final DroneService droneService, 
			final DroneJmsConfig jmsConfig,
			final ObjectMapper objectMapper
			) {
		this.authorizationService = authorizationService;
		this.droneService = droneService;
		this.jmsConfig = jmsConfig;
		this.objectMapper = objectMapper;
	}
	
	private PublishedDroneDTO parseDroneDTO(String json) throws JsonMappingException, JsonProcessingException {
        PublishedDroneDTO DroneDTO = objectMapper.readValue(json, PublishedDroneDTO.class);
		
		assert DroneDTO != null;
		assert DroneDTO.getId() != null;
		assert DroneDTO.getOperationCategory() != null;
		
        return DroneDTO;
	}
	
	@JmsListener(destination = "${new_drone_topic}")
    public void onNewDroneMessage(String json) {
		PublishedDroneDTO DroneDTO = null;
		try {
			DroneDTO = parseDroneDTO(json);
		} catch (Exception e) {
			System.err.println(String.format("Error parsing new published drone: %s", e.getMessage()));
			return;
		}
		
		droneService.save(DroneDTO);
    }
	
	@JmsListener(destination = "${updated_drone_topic}")
    public void onUpdatedDroneMessage(String json) {
		PublishedDroneDTO DroneDTO = null;
		try {
			DroneDTO = parseDroneDTO(json);
		} catch (Exception e) {
			System.err.println(String.format("Error parsing updated published drone: %s", e.getMessage()));
			return;
		}
		
		try {
			droneService.update(DroneDTO);
		} catch (NotFoundException e) {
			System.err.println(String.format("Error updating published drone: %s", e.getMessage()));
		}
    }
	
	@JmsListener(destination = "${deleted_drone_topic}")
    public void onDeletedDroneMessage(String json) {
		PublishedDroneDTO DroneDTO = null;
		try {
			DroneDTO = parseDroneDTO(json);
		} catch (Exception e) {
			System.err.println(String.format("Error parsing deleted published drone: %s", e.getMessage()));
			return;
		}
		
		Drone drone = null;
		try {
			drone = droneService.getDroneById(DroneDTO.getId());
		} catch (NotFoundException e) {
			System.err.println(String.format("Error deleting published drone: %s", e.getMessage()));
		}
		
		authorizationService.deleteByDrone(drone);
		droneService.delete(drone);
    }
}