package com.aeroflux.geoawareness.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.aeroflux.geoawareness.dto.PublishedGeozoneDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PublisherService {

	@Value("${new_geozone_topic}")
    private String newGeozoneTopic;

    @Value("${updated_geozone_topic}")
    private String updatedGeozoneTopic;

    @Value("${deleted_geozone_topic}")
    private String deletedGeozoneTopic;
	
    private JmsTemplate jmsTemplate;
    private ObjectMapper serializer;
    
    @Autowired
	public PublisherService(final JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
		// Set the destination to be a topic (for publish/subscribe messaging with multiple subscribers)
		jmsTemplate.setPubSubDomain(true);	
		
		this.serializer = new ObjectMapper();
	}
    
    public void publishNewGeozone(final PublishedGeozoneDTO geozoneDTO) {
    	try {
	        jmsTemplate.convertAndSend(
	        		newGeozoneTopic, 
	        		serializer.writeValueAsString(geozoneDTO)
	        		);
		} catch (JsonProcessingException e) {
			System.err.println(String.format("Error publishing new geozone: %s", e.getMessage()));
		}
    }   
    
    public void publishUpdatedGeozone(final PublishedGeozoneDTO geozoneDTO) {
    	try {
	        jmsTemplate.convertAndSend(
	        		updatedGeozoneTopic, 
	        		serializer.writeValueAsString(geozoneDTO)
	        		);
		} catch (JsonProcessingException e) {
			System.err.println(String.format("Error publishing updated geozone: %s", e.getMessage()));
		}
    }   
    
    public void publishDeletedGeozone(final PublishedGeozoneDTO geozoneDTO) {
    	try {
	        jmsTemplate.convertAndSend(
	        		deletedGeozoneTopic, 
	        		serializer.writeValueAsString(geozoneDTO)
	        		);
		} catch (JsonProcessingException e) {
			System.err.println(String.format("Error publishing deleted geozone: %s", e.getMessage()));
		}
    }   
}