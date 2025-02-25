package com.brianzolilecchesi.geoauthorization.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeozoneJmsConfig {
	
	@Value("${new_geozone_topic}")
    private String newGeozoneTopic;
	
	@Value("${updated_geozone_topic}")
    private String updatedGeozoneTopic;
	
	@Value("${deleted_geozone_topic}")
    private String deletedGeozoneTopic;

    public String getNewGeozoneTopic() {
        return newGeozoneTopic;
    }
    
	public String getUpdatedGeozoneTopic() {
		return updatedGeozoneTopic;
	}
	
	public String getDeletedGeozoneTopic() {
		return deletedGeozoneTopic;
	}
}