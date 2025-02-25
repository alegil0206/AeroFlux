package com.brianzolilecchesi.drone_identification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.brianzolilecchesi.drone_identification.dto.PublishedDroneDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PublisherService {

	@Value("${new_drone_topic}")
    private String newDroneTopic;

    @Value("${updated_drone_topic}")
    private String updatedDroneTopic;

    @Value("${deleted_drone_topic}")
    private String deletedDroneTopic;
	
    private JmsTemplate jmsTemplate;
    private ObjectMapper serializer;
    
    @Autowired
	public PublisherService(final JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
		// Abilitate the publish/subscribe domain for multiple subscribers
		jmsTemplate.setPubSubDomain(true);
		
		this.serializer = new ObjectMapper();
	}
    
    public void publishNewDrone(final PublishedDroneDTO droneDTO) {
    	try {
	        jmsTemplate.convertAndSend(
	        		newDroneTopic, 
	        		serializer.writeValueAsString(droneDTO)
	        		);
		} catch (JsonProcessingException e) {
			System.err.println(String.format("Error publishing new drone: %s", e.getMessage()));
		}
    }   
    
    public void publishUpdatedDrone(final PublishedDroneDTO droneDTO) {
    	try {
	        jmsTemplate.convertAndSend(
	        		updatedDroneTopic, 
	        		serializer.writeValueAsString(droneDTO)
	        		);
		} catch (JsonProcessingException e) {
			System.err.println(String.format("Error publishing updated drone: %s", e.getMessage()));
		}
    }   
    
    public void publishDeletedDrone(final PublishedDroneDTO droneDTO) {
    	try {
	        jmsTemplate.convertAndSend(
	        		deletedDroneTopic, 
	        		serializer.writeValueAsString(droneDTO)
	        		);
		} catch (JsonProcessingException e) {
			System.err.println(String.format("Error publishing deleted drone: %s", e.getMessage()));
		}
    }   
}