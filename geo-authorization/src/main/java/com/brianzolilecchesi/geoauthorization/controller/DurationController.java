package com.brianzolilecchesi.geoauthorization.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brianzolilecchesi.geoauthorization.dto.DurationDTO;
import com.brianzolilecchesi.geoauthorization.exception.NotFoundException;
import com.brianzolilecchesi.geoauthorization.service.DurationService;

@RestController
@RequestMapping(DurationController.DURATION_BASE_URL)
public class DurationController {
	
	public static final String DURATION_BASE_URL = AuthorizationController.AUTHORIZATION_BASE_URI + "/duration";
	
	private final DurationService service;
	
	@Autowired
	public DurationController(final DurationService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<List<DurationDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<DurationDTO> findByName(@PathVariable String name) throws NotFoundException {
		assert name != null;
		return ResponseEntity.ok(service.findByName(name));
	}
}