package com.aeroflux.drone_identification.model.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aeroflux.drone_identification.model.persistency.operationcategory.OperationCategory;

public interface OperationCategoryRepository extends MongoRepository<OperationCategory, String> {

	public Optional<OperationCategory> findByName(String name);	
}