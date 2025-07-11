package com.aeroflux.geoawareness.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aeroflux.geoawareness.exception.NotFoundException;
import com.aeroflux.geoawareness.model.persistency.Category;
import com.aeroflux.geoawareness.service.CategoryService;

@RestController
@RequestMapping(CategoryController.CATEGORY_BASE_URL)
public class CategoryController {
	
	public static final String CATEGORY_BASE_URL = GeozoneController.GEOZONE_BASE_URL +  "/category";
	
	private final CategoryService service;
	
	@Autowired
	public CategoryController(final CategoryService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<List<Category>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<Category> findByName(@PathVariable String name) throws NotFoundException {
		assert name != null;
		return ResponseEntity.ok(service.findByName(name));
	}
}