package com.aeroflux.geoauthorization.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aeroflux.geoauthorization.model.persistency.authorization.AuthorizationGranted;
import com.aeroflux.geoauthorization.model.persistency.drone.Drone;
import com.aeroflux.geoauthorization.model.persistency.geozone.Geozone;

public interface AuthorizationGrantedRepository extends JpaRepository<AuthorizationGranted, Long> {
	
	List<AuthorizationGranted> findByDroneAndGeozone(Drone drone, Geozone geozone);
	List<AuthorizationGranted> findByDrone(Drone drone);
	List<AuthorizationGranted> findByGeozone(Geozone geozone);	
	
	void deleteByDrone(Drone drone);
	void deleteByGeozone(Geozone geozone);
	
	List<AuthorizationGranted> findByExpiresAtBefore(LocalDateTime expiresAt);
}