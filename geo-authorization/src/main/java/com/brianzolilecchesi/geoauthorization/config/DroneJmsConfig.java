package com.brianzolilecchesi.geoauthorization.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroneJmsConfig {
	
	@Value("${new_drone_topic}")
    private String newDroneTopic;
	
	@Value("${updated_drone_topic}")
    private String updatedDroneTopic;
	
	@Value("${deleted_drone_topic}")
    private String deletedDroneTopic;

    public String getNewDroneTopic() {
        return newDroneTopic;
    }
    
	public String getUpdatedDroneTopic() {
		return updatedDroneTopic;
	}
	
	public String getDeletedDroneTopic() {
		return deletedDroneTopic;
	}
}