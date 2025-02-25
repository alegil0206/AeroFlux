package com.brianzolilecchesi.geoauthorization.db_init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.brianzolilecchesi.geoauthorization.model.persistency.authorization.Duration;
import com.brianzolilecchesi.geoauthorization.model.repository.DurationRepository;

@Component
public class DatabaseInitializer implements CommandLineRunner {
	
	@Autowired
    private DurationRepository repository;

    @Override
    public void run(String... args) throws Exception {
    	String[] names = {"TEST", "SHORT", "STANDARD", "EXTENDED"};
    	Integer[] values = {1, 30, 60, 120};
    	
		for (int i = 0; i < names.length; ++i) {
			if (!repository.existsById(names[i]))
				repository.save(new Duration(names[i], values[i]));
		}
    }
}