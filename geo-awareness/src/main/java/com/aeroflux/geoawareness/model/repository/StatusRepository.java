package com.aeroflux.geoawareness.model.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.aeroflux.geoawareness.model.persistency.Status;

@Repository
public interface StatusRepository extends MongoRepository<Status, String> {
	
	Optional<Status> findByName(String name);
}