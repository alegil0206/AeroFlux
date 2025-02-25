package com.brianzolilecchesi.geoauthorization.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brianzolilecchesi.geoauthorization.model.persistency.authorization.AuthorizationExpired;
import com.brianzolilecchesi.geoauthorization.model.persistency.drone.Drone;
import com.brianzolilecchesi.geoauthorization.model.persistency.geozone.Geozone;

public interface AuthorizationExpiredRepository extends JpaRepository<AuthorizationExpired, Long> {
	
	List<AuthorizationExpired> findByDroneAndGeozone(Drone drone, Geozone geozone);
	List<AuthorizationExpired> findByDrone(Drone drone);
	List<AuthorizationExpired> findByGeozone(Geozone geozone);	
	
	void deleteByDrone(Drone drone);
	void deleteByGeozone(Geozone geozone);
}