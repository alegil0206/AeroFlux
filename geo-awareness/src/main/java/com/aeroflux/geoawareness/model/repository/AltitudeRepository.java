package com.aeroflux.geoawareness.model.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.aeroflux.geoawareness.model.persistency.Altitude;

@Repository
public interface AltitudeRepository extends MongoRepository<Altitude, String> {	
	
	Optional<Altitude> findByName(String name);
}