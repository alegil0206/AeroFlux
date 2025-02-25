package com.brianzolilecchesi.geoawareness.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brianzolilecchesi.geoawareness.exception.NotFoundException;
import com.brianzolilecchesi.geoawareness.model.persistency.Status;
import com.brianzolilecchesi.geoawareness.service.StatusService;

@RestController
@RequestMapping(StatusController.STATUS_BASE_URL)
public class StatusController {
	
	public static final String STATUS_BASE_URL = GeozoneController.GEOZONE_BASE_URL + "/status";
	
	private final StatusService service;
	
	@Autowired
	public StatusController(final StatusService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<List<Status>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<Status> findByName(@PathVariable String name) throws NotFoundException {
		assert name != null;
		return ResponseEntity.ok(service.findByName(name));
	}
}