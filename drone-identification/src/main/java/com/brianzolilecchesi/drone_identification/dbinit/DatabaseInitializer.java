package com.brianzolilecchesi.drone_identification.dbinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.brianzolilecchesi.drone_identification.model.persistency.operationcategory.OperationCategory;
import com.brianzolilecchesi.drone_identification.model.repository.OperationCategoryRepository;
import com.brianzolilecchesi.drone_identification.registry.OperationCategoryRegistrySingleton;
import com.brianzolilecchesi.drone_identification.registry.Registry;

@Component
@Profile("dev")
public class DatabaseInitializer implements CommandLineRunner {
	
	@Autowired
    private OperationCategoryRepository operationCategoryRepository;

    @Override
    public void run(String... args) throws Exception {    	
    	String[] names = {"CERTIFIED", "SPECIFIC"};
    	String[] descriptions = {
    			"Low priority category for specific operations.",
    			"High priority category for certified operations."
		};
    	Registry registry = OperationCategoryRegistrySingleton.getInstance();
    	
    	for (OperationCategory category : operationCategoryRepository.findAll()) {
			registry.register(category.getName());
		}
    	
		for (int i = 0; i < names.length; i++) {
			if (!registry.contains(names[i])) {
				operationCategoryRepository.save(new OperationCategory(names[i], descriptions[i]));
				registry.register(names[i]);
			}
		}
		
		System.out.println("Database initialized.");
		System.out.println("Registry initialized.");
    }
}