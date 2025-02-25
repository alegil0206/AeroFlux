package com.brianzolilecchesi.geoawareness.model.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.brianzolilecchesi.geoawareness.model.persistency.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
	
	Optional<Category> findByName(String name);
}