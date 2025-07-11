package com.aeroflux.geoauthorization.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aeroflux.geoauthorization.model.persistency.authorization.Duration;

public interface DurationRepository extends JpaRepository<Duration, String> {
	
}
