package com.aeroflux.geoauthorization.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aeroflux.geoauthorization.model.persistency.authorization.AuthorizationRevoked;
import com.aeroflux.geoauthorization.model.persistency.drone.Drone;
import com.aeroflux.geoauthorization.model.persistency.geozone.Geozone;

public interface AuthorizationRevokedRepository extends JpaRepository<AuthorizationRevoked, Long> {
	
	List<AuthorizationRevoked> findByDroneAndGeozone(Drone drone, Geozone geozone);
	List<AuthorizationRevoked> findByDrone(Drone drone);
	List<AuthorizationRevoked> findByGeozone(Geozone geozone);	
	
	void deleteByDrone(Drone drone);
	void deleteByGeozone(Geozone geozone);
}