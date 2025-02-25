package com.brianzolilecchesi.geoauthorization.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brianzolilecchesi.geoauthorization.model.persistency.authorization.AuthorizationDenied;
import com.brianzolilecchesi.geoauthorization.model.persistency.drone.Drone;
import com.brianzolilecchesi.geoauthorization.model.persistency.geozone.Geozone;

public interface AuthorizationDeniedRepository extends JpaRepository<AuthorizationDenied, Long> {
	
	List<AuthorizationDenied> findByDroneAndGeozone(Drone drone, Geozone geozone);
	List<AuthorizationDenied> findByDrone(Drone drone);
	List<AuthorizationDenied> findByGeozone(Geozone geozone);
	
	void deleteByDrone(Drone drone);
	void deleteByGeozone(Geozone geozone);
}