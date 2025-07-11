package com.aeroflux.drone_identification.model.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aeroflux.drone_identification.model.persistency.drone.Drone;

public interface DroneRepository extends MongoRepository<Drone, String> {

}