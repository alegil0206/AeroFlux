package com.aeroflux.geoawareness.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aeroflux.geoawareness.exception.NotFoundException;
import com.aeroflux.geoawareness.model.persistency.Category;
import com.aeroflux.geoawareness.model.repository.CategoryRepository;

@Service
public class CategoryService {
	
	private final CategoryRepository repository;
	
	public CategoryService(final CategoryRepository repository) {
		this.repository = repository;
	}
	
	public List<Category> findAll() {
		return repository.findAll();
	}
	
	public Category findByName(String name) throws NotFoundException {
		return repository.findByName(name.toUpperCase()).orElseThrow(() -> new NotFoundException("Category not found: " + name));
	}
}