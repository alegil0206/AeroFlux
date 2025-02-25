package com.brianzolilecchesi.geoauthorization.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brianzolilecchesi.geoauthorization.model.persistency.geozone.Geozone;

public interface GeozoneRepository extends JpaRepository<Geozone, String> {

}