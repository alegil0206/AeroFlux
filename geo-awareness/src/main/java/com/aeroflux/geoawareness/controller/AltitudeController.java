package com.aeroflux.geoawareness.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aeroflux.geoawareness.dto.AltitudeDTO;
import com.aeroflux.geoawareness.exception.NotFoundException;
import com.aeroflux.geoawareness.service.AltitudeService;

@RestController
@RequestMapping(AltitudeController.ALTITUDE_BASE_URL)
public class AltitudeController {
	
	public static final String ALTITUDE_BASE_URL = GeozoneController.GEOZONE_BASE_URL + "/altitude";
	
	private final AltitudeService service;
	
	@Autowired
	public AltitudeController(final AltitudeService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<List<AltitudeDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<AltitudeDTO> findByName(@PathVariable String name) throws NotFoundException {
		assert name != null;
		return ResponseEntity.ok(service.findByName(name));
	}
}