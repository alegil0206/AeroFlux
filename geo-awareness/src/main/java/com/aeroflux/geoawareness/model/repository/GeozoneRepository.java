package com.aeroflux.geoawareness.model.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.aeroflux.geoawareness.model.persistency.geozone.Geozone;

@Repository
public interface GeozoneRepository extends MongoRepository<Geozone, String> {
	
	List<Geozone> findByType(String type);
}