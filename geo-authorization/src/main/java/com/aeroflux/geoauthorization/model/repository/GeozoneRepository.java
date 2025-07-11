package com.aeroflux.geoauthorization.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aeroflux.geoauthorization.model.persistency.geozone.Geozone;

public interface GeozoneRepository extends JpaRepository<Geozone, String> {

}