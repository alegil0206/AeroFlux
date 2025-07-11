package com.aeroflux.geoauthorization.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aeroflux.geoauthorization.model.persistency.drone.Drone;

public interface DroneRepository extends JpaRepository<Drone, String> {

}