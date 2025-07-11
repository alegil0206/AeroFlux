package com.aeroflux.geoawareness.controller;

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

import com.aeroflux.geoawareness.dto.GeozoneDTO;
import com.aeroflux.geoawareness.dto.PublishedGeozoneDTO;
import com.aeroflux.geoawareness.exception.BadRequestException;
import com.aeroflux.geoawareness.exception.ForbiddenException;
import com.aeroflux.geoawareness.exception.NotFoundException;
import com.aeroflux.geoawareness.service.GeozoneService;
import com.aeroflux.geoawareness.service.PublisherService;

@RestController
@RequestMapping(GeozoneController.GEOZONE_BASE_URL)
public class GeozoneController {
	
	public static final String GEOZONE_BASE_URL = "/geozone";
	
	private final GeozoneService geozoneService;
	private final PublisherService publisherService;

	@Autowired
	public GeozoneController(final GeozoneService geozoneService, final PublisherService publisherService) {
		this.geozoneService = geozoneService;
		this.publisherService = publisherService;
	}

	@GetMapping
	public ResponseEntity<List<GeozoneDTO>> getAll() {
		return ResponseEntity.ok(geozoneService.getAll());
	}
	
	@PostMapping
	public ResponseEntity<GeozoneDTO> save(@RequestBody final GeozoneDTO geozoneDTO) throws BadRequestException, NotFoundException {
		GeozoneDTO newGeozoneDTO = geozoneService.save(geozoneDTO);		
		URI location = URI.create(String.format(
				"%s/%s",
				GEOZONE_BASE_URL,
				newGeozoneDTO.getId()
				));
		
		publisherService.publishNewGeozone(new PublishedGeozoneDTO(
				newGeozoneDTO.getId(), 
				newGeozoneDTO.getCategory(), 
				newGeozoneDTO.getStatus()
				));
		return ResponseEntity.created(location).body(newGeozoneDTO);
	}
	
	@DeleteMapping
		public ResponseEntity<Void> deleteAll() {
			List<GeozoneDTO> geozoneDTOs = geozoneService.getAll();
			geozoneService.deleteAll();
			
			for (GeozoneDTO geozoneDTO : geozoneDTOs) {
				publisherService.publishDeletedGeozone(new PublishedGeozoneDTO(
						geozoneDTO.getId(), 
						geozoneDTO.getCategory(), 
						geozoneDTO.getStatus())
						);
			}
			
			return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<GeozoneDTO> getById(@PathVariable final String id) throws NotFoundException {
		assert id != null;
		return ResponseEntity.ok(geozoneService.getById(id));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<GeozoneDTO> update(@PathVariable final String id, @RequestBody final GeozoneDTO geozoneDTO)
			throws BadRequestException, NotFoundException, ForbiddenException {
		
		assert id != null;
		geozoneDTO.setId(id);
		GeozoneDTO updatedGeozoneDTO = geozoneService.update(geozoneDTO);
		
		publisherService.publishUpdatedGeozone(new PublishedGeozoneDTO(
				updatedGeozoneDTO.getId(), 
				updatedGeozoneDTO.getCategory(), 
				updatedGeozoneDTO.getStatus()
				));
		return ResponseEntity.ok(updatedGeozoneDTO);
	}

	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable final String id) throws NotFoundException {
		assert id != null;
		GeozoneDTO deletedGeozoneDTO = geozoneService.deleteById(id);
        
		publisherService.publishDeletedGeozone(new PublishedGeozoneDTO(
				deletedGeozoneDTO.getId(), 
				deletedGeozoneDTO.getCategory(), 
				deletedGeozoneDTO.getStatus()
				));
        return ResponseEntity.noContent().build();
	}
}
