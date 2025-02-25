package com.brianzolilecchesi.geoauthorization.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brianzolilecchesi.geoauthorization.model.persistency.authorization.Duration;

public interface DurationRepository extends JpaRepository<Duration, String> {
	
}
