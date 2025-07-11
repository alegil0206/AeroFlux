package com.aeroflux.geoawareness.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aeroflux.geoawareness.dto.GeozoneTypeDTO;
import com.aeroflux.geoawareness.exception.NotFoundException;
import com.aeroflux.geoawareness.model.persistency.Type;
import com.aeroflux.geoawareness.model.repository.TypeRepository;

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