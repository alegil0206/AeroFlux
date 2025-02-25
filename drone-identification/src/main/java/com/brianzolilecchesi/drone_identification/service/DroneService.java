package com.brianzolilecchesi.drone_identification.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.brianzolilecchesi.drone_identification.dto.DroneDTO;
import com.brianzolilecchesi.drone_identification.exception.drone.DroneNotFoundException;
import com.brianzolilecchesi.drone_identification.exception.drone.IllegalDroneException;
import com.brianzolilecchesi.drone_identification.exception.operationcategory.IllegalOperationCategoryException;
import com.brianzolilecchesi.drone_identification.exception.operationcategory.OperationCategoryNotFoundException;
import com.brianzolilecchesi.drone_identification.exception.position.IllegalPositionException;
import com.brianzolilecchesi.drone_identification.model.persistency.drone.Drone;
import com.brianzolilecchesi.drone_identification.model.persistency.drone.DroneFacadeSingleton;
import com.brianzolilecchesi.drone_identification.model.repository.DroneRepository;
import com.brianzolilecchesi.drone_identification.validation.DroneValidator;

@Component
public class DroneService {
	
	private final DroneRepository droneRepository;
	private final DroneValidator droneValidator;
	
	@Autowired
	public DroneService(final DroneRepository droneRepository, final DroneValidator droneValidator) {
		this.droneRepository = droneRepository;
		this.droneValidator = droneValidator;
	}
	
	public List<DroneDTO> getAll() {
		List<DroneDTO> dronesDTO = new ArrayList<>();
		
		droneRepository.findAll().forEach(drone -> { 
			dronesDTO.add(DroneFacadeSingleton
					.getInstance()
					.getDroneFactory()
					.createDroneDTO(drone)
					);
		});
		
		return dronesDTO;
	}
	
	private Drone getDroneById(final DroneDTO droneDTO) throws DroneNotFoundException {
		return getDroneById(droneDTO.getId());
	}
	
	private Drone getDroneById(final String id) throws DroneNotFoundException {
		return droneRepository.findById(id).orElseThrow(() -> new DroneNotFoundException());
	}
	
	public DroneDTO getById(final String id) throws DroneNotFoundException {
		return DroneFacadeSingleton
				.getInstance() 
				.getDroneFactory()
				.createDroneDTO(getDroneById(id));
	}
	
	public DroneDTO update(final DroneDTO droneDTO) throws IllegalDroneException, DroneNotFoundException, IllegalPositionException, IllegalOperationCategoryException, OperationCategoryNotFoundException {
		droneValidator.validate(droneDTO);
		
		Drone drone = getDroneById(droneDTO);
		DroneFacadeSingleton.getInstance().getDroneUpdater().updateDrone(drone, droneDTO);
		droneRepository.save(drone);

		return DroneFacadeSingleton
				.getInstance()
                .getDroneFactory()
                .createDroneDTO(drone);
	}
	
	public DroneDTO save(final DroneDTO droneDTO) throws IllegalDroneException, IllegalPositionException, IllegalOperationCategoryException {
		droneValidator.validate(droneDTO);
		
		Drone drone = droneRepository.save(
				DroneFacadeSingleton
				.getInstance()
				.getDroneFactory()
				.createDrone(droneDTO));
		return DroneFacadeSingleton.getInstance().getDroneFactory().createDroneDTO(drone);
	}
	
	public DroneDTO deleteById(final String id) throws DroneNotFoundException {
		DroneDTO droneDTO = getById(id);
		droneRepository.deleteById(id);
		
		return droneDTO;
	}
	
	public void deleteAll() {
		droneRepository.deleteAll();
	}
}