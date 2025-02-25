package com.brianzolilecchesi.geoawareness.dbinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.brianzolilecchesi.geoawareness.model.persistency.Altitude;
import com.brianzolilecchesi.geoawareness.model.persistency.Category;
import com.brianzolilecchesi.geoawareness.model.persistency.Status;
import com.brianzolilecchesi.geoawareness.model.persistency.Type;
import com.brianzolilecchesi.geoawareness.model.repository.AltitudeRepository;
import com.brianzolilecchesi.geoawareness.model.repository.CategoryRepository;
import com.brianzolilecchesi.geoawareness.model.repository.StatusRepository;
import com.brianzolilecchesi.geoawareness.model.repository.TypeRepository;
import com.brianzolilecchesi.geoawareness.registry.RegistryFacadeSingleton;
import com.brianzolilecchesi.geoawareness.registry.RegistryMap;
import com.brianzolilecchesi.geoawareness.registry.RegistrySet;

/**
 * This class initializes the database with the default values for the altitude, category, status and type collections.
 */

@Component
@Profile("prod")
public class RegistryInitializer implements CommandLineRunner {
	
	@Value("${registry_init_delay_millis}")
	private long registryInitDelayMillis;
	
	@Autowired
	private AltitudeRepository altitudeRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
    private StatusRepository statusRepository;
	@Autowired
    private TypeRepository typeRepository;

    @Override
    public void run(String... args) throws Exception {
    	Thread.sleep(registryInitDelayMillis); // Wait for the database to be initialized
    	
    	initializeAltitude();
		initializeCategory();
		initializeStatus();
		initializeType();
		System.out.println("Registries initialized.");
    }
    	
    private void initializeAltitude() {
    	RegistryMap<Integer> registry = RegistryFacadeSingleton.getInstance().getAltitudeRegistry();
    	for (Altitude altitude : altitudeRepository.findAll()) {
			registry.register(altitude.getName(), altitude.getValue());
		}
	}
    
    private void initializeCategory() {
    	RegistrySet registry = RegistryFacadeSingleton.getInstance().getCategoryRegistry();
    	
    	for (Category category : categoryRepository.findAll()) {
			registry.register(category.getName());
		}
    }
    
    private void initializeStatus() {
    	RegistrySet registry = RegistryFacadeSingleton.getInstance().getStatusRegistry();
    	
    	for (Status status : statusRepository.findAll()) {
			registry.register(status.getName());
		}
    }
    
    private void initializeType() {
    	RegistrySet registry = RegistryFacadeSingleton.getInstance().getTypeRegistry();
    	
    	for (Type type : typeRepository.findAll()) {
            registry.register(type.getName());
    	}
    }
}