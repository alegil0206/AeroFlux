package com.brianzolilecchesi.geoawareness.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brianzolilecchesi.geoawareness.dto.GeozoneTypeDTO;
import com.brianzolilecchesi.geoawareness.exception.NotFoundException;
import com.brianzolilecchesi.geoawareness.model.persistency.Type;
import com.brianzolilecchesi.geoawareness.service.TypeService;

@RestController
@RequestMapping(TypeController.TYPE_BASE_URL)
public class TypeController {
	
	public static final String TYPE_BASE_URL = GeozoneController.GEOZONE_BASE_URL + "/type";
	
	private final TypeService service;
	
	@Autowired
	public TypeController(final TypeService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<List<Type>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<GeozoneTypeDTO> findByName(@PathVariable String name) throws NotFoundException {
		assert name != null;
		return ResponseEntity.ok(service.getTypeByName(name));
	}
}