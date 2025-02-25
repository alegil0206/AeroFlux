package com.brianzolilecchesi.geoawareness.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.brianzolilecchesi.geoawareness.dto.GeozoneTypeDTO;
import com.brianzolilecchesi.geoawareness.exception.NotFoundException;
import com.brianzolilecchesi.geoawareness.model.persistency.Type;
import com.brianzolilecchesi.geoawareness.model.repository.TypeRepository;

@Service
public class TypeService {
	
	private final TypeRepository repository;
	private final GeozoneService geozoneService;
	
	public TypeService(
			final TypeRepository repository,
			final GeozoneService geozoneService
			) {
		this.repository = repository;
		this.geozoneService = geozoneService;
	}
	
	public List<Type> findAll() {
		return repository.findAll();
	}
	
	public Type findByName(final String name) throws NotFoundException {
		return repository.findByName(name.toUpperCase()).orElseThrow(() -> new NotFoundException("Type not found: " + name));
	}
	
	public GeozoneTypeDTO getTypeByName(final String typeName) throws NotFoundException {
		Type type = findByName(typeName);
		return new GeozoneTypeDTO(
				type.getName(),
				type.getDescription(),
				geozoneService.getExampleGeozoneDTO(type)
				);
	}
}