package com.aeroflux.geoawareness.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aeroflux.geoawareness.exception.NotFoundException;
import com.aeroflux.geoawareness.model.persistency.Status;
import com.aeroflux.geoawareness.model.repository.StatusRepository;

@Service
public class StatusService {
	
	private final StatusRepository repository;
	
	public StatusService(final StatusRepository repository) {
		this.repository = repository;
	}
	
	public List<Status> findAll() {
		return repository.findAll();
	}
	
	public Status findByName(String name) throws NotFoundException {
		return repository.findByName(name.toUpperCase()).orElseThrow(() -> new NotFoundException("Status not found: " + name));
	}
}