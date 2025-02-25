package com.brianzolilecchesi.geoawareness.model.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.brianzolilecchesi.geoawareness.model.persistency.Type;

@Repository
public interface TypeRepository extends MongoRepository<Type, String> {
	
	Optional<Type> findByName(String name);
}