package com.brianzolilecchesi.geoauthorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brianzolilecchesi.geoauthorization.dto.PublishedDroneDTO;
import com.brianzolilecchesi.geoauthorization.exception.NotFoundException;
import com.brianzolilecchesi.geoauthorization.model.persistency.drone.Drone;
import com.brianzolilecchesi.geoauthorization.model.persistency.drone.DroneFactorySingleton;
import com.brianzolilecchesi.geoauthorization.model.repository.DroneRepository;

import jakarta.transaction.Transactional;

@Service
public class DroneService {
	
	private final DroneRepository repository;
	
	@Autowired
	public DroneService(final DroneRepository repository) {
		this.repository = repository;
	}
	
	Drone getDroneById(final String droneId) throws NotFoundException {
		return repository.findById(droneId).orElseThrow(() -> new NotFoundException("Drone with id " + droneId + " not found"));
    }
    
	@Transactional
    public PublishedDroneDTO update(final PublishedDroneDTO DroneDTO) throws NotFoundException {
    	Drone Drone = getDroneById(DroneDTO.getId());
    	Drone.update(DroneDTO);
    	repository.save(Drone);
    	
    	return DroneDTO;
    }
    
	@Transactional
	public PublishedDroneDTO save(final PublishedDroneDTO DroneDTO) {
		Drone Drone = DroneFactorySingleton.getInstance().createDrone(DroneDTO);
		repository.save(Drone);
		return DroneDTO;
	}
	
	@Transactional
    public void delete(final Drone drone) {
        assert drone != null;
    	repository.delete(drone);
    }
    
	@Transactional
	public void deleteAll() {
		repository.deleteAll();
	}
}