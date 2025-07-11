package com.aeroflux.drone_identification.dbinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.aeroflux.drone_identification.model.persistency.operationcategory.OperationCategory;
import com.aeroflux.drone_identification.model.repository.OperationCategoryRepository;
import com.aeroflux.drone_identification.registry.OperationCategoryRegistrySingleton;
import com.aeroflux.drone_identification.registry.Registry;

/**
 * This class initializes the database with the default values for the altitude, category, status and type collections.
 */

@Component
@Profile("prod")
public class RegistryInitializer implements CommandLineRunner {
	
	@Value("${registry_init_delay_millis}")
	private long registryInitDelayMillis;
	
	@Autowired
    private OperationCategoryRepository operationCategoryRepository;

    @Override
    public void run(String... args) throws Exception {   
    	Registry registry = OperationCategoryRegistrySingleton.getInstance();
    	
    	Thread.sleep(registryInitDelayMillis);		// Wait for the database to be initialized
    	for (OperationCategory category : operationCategoryRepository.findAll()) {
			registry.register(category.getName());
		}
    	
    	System.out.println("Registry initialized.");
    }	
}