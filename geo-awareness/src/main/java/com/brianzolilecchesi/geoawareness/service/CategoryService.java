package com.brianzolilecchesi.geoawareness.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.brianzolilecchesi.geoawareness.exception.NotFoundException;
import com.brianzolilecchesi.geoawareness.model.persistency.Category;
import com.brianzolilecchesi.geoawareness.model.repository.CategoryRepository;

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