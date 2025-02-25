package com.brianzolilecchesi.geoawareness.dbinit;

import org.springframework.beans.factory.annotation.Autowired;
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
 * Then it initializes the altitude, category, status and type registries with the default values. 
 */

@Component
@Profile("dev")
public class DatabaseInitializer implements CommandLineRunner {
	
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
    	initializeAltitude();
		initializeCategory();
		initializeStatus();
		initializeType();
		
		System.out.println("Database initialized.");
		System.out.println("Registries initialized.");
    }
    	
    private void initializeAltitude() {
    	System.out.println("Initializing altitude");
    	
    	String[] names = {"L0", "L1", "L2", "L3", "L4"};
    	Integer[] values = {0, 25, 45, 60, 120};
    	RegistryMap<Integer> registry = RegistryFacadeSingleton.getInstance().getAltitudeRegistry();
    	
    	for (Altitude altitude : altitudeRepository.findAll()) {
			registry.register(altitude.getName(), altitude.getValue());
		}
    	
		for (int i = 0; i < names.length; i++) {
			if (!registry.contains(names[i])) {
				altitudeRepository.save(new Altitude(names[i], values[i]));
				registry.register(names[i], values[i]);
			}
		}
	}
    
    private void initializeCategory() {
    	System.out.println("Initializing category");
    	
    	String[] names = {"RESTRICTED", "EXCLUDED"};
    	String[] descriptions = {
    			"Geozone which requires an authorization to be accessed.",
    			"Geozone which is not allowed to be accessed."
		};
    	RegistrySet registry = RegistryFacadeSingleton.getInstance().getCategoryRegistry();
    	
    	for (Category category : categoryRepository.findAll()) {
			registry.register(category.getName());
		}
    	
		for (int i = 0; i < names.length; i++) {
			if (!registry.contains(names[i])) {
				categoryRepository.save(new Category(names[i], descriptions[i]));
				registry.register(names[i]);
			}
		}
    }
    
    private void initializeStatus() {
    	System.out.println("Initializing status");
    	
    	String[] names = {"ACTIVE", "INACTIVE"};
    	String[] descriptions = {
    			"Geozone which is active.",
    			"Geozone which is not active and can be normally accessed."
		};
    	RegistrySet registry = RegistryFacadeSingleton.getInstance().getStatusRegistry();
    	
    	for (Status status : statusRepository.findAll()) {
			registry.register(status.getName());
		}
    	
		for (int i = 0; i < names.length; i++) {
			if (!registry.contains(names[i])) {
				statusRepository.save(new Status(names[i], descriptions[i]));
				registry.register(names[i]);
			}
		}
    }
    
    private void initializeType() {
    	System.out.println("Initializing type");
    	
    	String[] names = {"CIRCULAR", "POLYGONAL"};
    	String[] descriptions = {
    			"Cylindrical geozone with a center (geographic coordinates), a radius (meters) and an altitude (meters).",
    			"Polygonal geozone within a list of geographic coordinates (at least three) and an altitude (meters)."
		};
    	RegistrySet registry = RegistryFacadeSingleton.getInstance().getTypeRegistry();
    	
    	for (Type type : typeRepository.findAll()) {
            registry.register(type.getName());
    	}
 
		for (int i = 0; i < names.length; i++) {
			if (!registry.contains(names[i])) {
				typeRepository.save(new Type(names[i], descriptions[i]));
				registry.register(names[i]);
			}
		}
    }
}