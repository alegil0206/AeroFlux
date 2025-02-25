package com.brianzolilecchesi.geoauthorization.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brianzolilecchesi.geoauthorization.model.persistency.drone.Drone;

public interface DroneRepository extends JpaRepository<Drone, String> {

}