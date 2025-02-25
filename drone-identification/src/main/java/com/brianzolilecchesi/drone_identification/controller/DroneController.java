package com.brianzolilecchesi.drone_identification.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brianzolilecchesi.drone_identification.dto.DroneDTO;
import com.brianzolilecchesi.drone_identification.dto.PublishedDroneDTO;
import com.brianzolilecchesi.drone_identification.exception.BadRequestException;
import com.brianzolilecchesi.drone_identification.exception.NotFoundException;
import com.brianzolilecchesi.drone_identification.service.DroneService;
import com.brianzolilecchesi.drone_identification.service.PublisherService;

@RestController
@RequestMapping(DroneController.DRONE_BASE_URI)
public class DroneController {
	
	public final static String DRONE_BASE_URI = "/drone";
	
	private final DroneService droneService;
	private final PublisherService publisherService;
	
	@Autowired
	public DroneController(final DroneService droneService, final PublisherService publisherService) {
		this.droneService = droneService;
		this.publisherService = publisherService;
	}
	
	@GetMapping
	public ResponseEntity<List<DroneDTO>> getDrones() {
		return ResponseEntity.ok().body(droneService.getAll());
	}
	
	@PostMapping
	public ResponseEntity<DroneDTO> save(@RequestBody final DroneDTO droneDTO) throws BadRequestException {
		DroneDTO newDroneDTO = droneService.save(droneDTO);	
		URI location = URI.create(String.format(
				"%s/%s",
				DRONE_BASE_URI,
				newDroneDTO.getId()));
		
		publisherService.publishNewDrone(new PublishedDroneDTO(
				newDroneDTO.getId(),
                newDroneDTO.getOperationCategory()
                ));
		return ResponseEntity.created(location).body(newDroneDTO);
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteAll() {
		droneService.getAll().forEach(droneDTO -> {
			publisherService.publishDeletedDrone(new PublishedDroneDTO(
					droneDTO.getId(), 
					droneDTO.getOperationCategory()
					));
		});
		droneService.deleteAll();
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DroneDTO> getById(@PathVariable final String id) throws NotFoundException {
		return ResponseEntity.ok().body(droneService.getById(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<DroneDTO> update(@PathVariable final String id, @RequestBody final DroneDTO droneDTO)
			throws BadRequestException, NotFoundException {
		
		droneDTO.setId(id);
		DroneDTO updatedDroneDTO = droneService.update(droneDTO);
		
		publisherService.publishUpdatedDrone(new PublishedDroneDTO(
				updatedDroneDTO.getId(),
				updatedDroneDTO.getOperationCategory()
                ));
		return ResponseEntity.ok(updatedDroneDTO);
	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable final String id) throws NotFoundException {
		DroneDTO droneDTO = droneService.deleteById(id);
		
		publisherService.publishDeletedDrone(new PublishedDroneDTO(
				droneDTO.getId(),
				droneDTO.getOperationCategory()
				));
        return ResponseEntity.noContent().build();
	}
}