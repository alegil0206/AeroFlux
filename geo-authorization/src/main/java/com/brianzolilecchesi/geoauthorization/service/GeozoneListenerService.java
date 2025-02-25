package com.brianzolilecchesi.geoauthorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.brianzolilecchesi.geoauthorization.config.GeozoneJmsConfig;
import com.brianzolilecchesi.geoauthorization.dto.PublishedGeozoneDTO;
import com.brianzolilecchesi.geoauthorization.exception.NotFoundException;
import com.brianzolilecchesi.geoauthorization.model.persistency.geozone.Geozone;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GeozoneListenerService {
	
	private final AuthorizationService authorizationService;
	private final GeozoneService geozoneService;
	@SuppressWarnings("unused")
	private final GeozoneJmsConfig jmsConfig;
	
	private final ObjectMapper objectMapper;
	
	@Autowired
	public GeozoneListenerService(
			final AuthorizationService authorizationService,
			final GeozoneService geozoneService, 
			final GeozoneJmsConfig jmsConfig,
			final ObjectMapper objectMapper
			) {
		this.authorizationService = authorizationService;
		this.geozoneService = geozoneService;
		this.jmsConfig = jmsConfig;
		this.objectMapper = objectMapper;
	}
	
	private PublishedGeozoneDTO parseGeozoneDTO(String json) throws JsonMappingException, JsonProcessingException {
        PublishedGeozoneDTO geozoneDTO = objectMapper.readValue(json, PublishedGeozoneDTO.class);
		
		assert geozoneDTO != null;
		assert geozoneDTO.getId() != null;
		assert geozoneDTO.getCategory() != null;
		assert geozoneDTO.getStatus() != null;
		
        return geozoneDTO;
	}
	
	@JmsListener(destination = "${new_geozone_topic}")
    public void onNewGeozoneMessage(String json) {
		PublishedGeozoneDTO geozoneDTO = null;
		try {
			geozoneDTO = parseGeozoneDTO(json);
		} catch (Exception e) {
			System.err.println(String.format("Error parsing new published geozone: %s", e.getMessage()));
			return;
		}
		
		geozoneService.save(geozoneDTO);
    }
	
	@JmsListener(destination = "${updated_geozone_topic}")
    public void onUpdatedGeozoneMessage(String json) {
		PublishedGeozoneDTO geozoneDTO = null;
		try {
			geozoneDTO = parseGeozoneDTO(json);
		} catch (Exception e) {
			System.err.println(String.format("Error parsing updated published geozone: %s", e.getMessage()));
			return;
		}
		
		try {
			geozoneService.update(geozoneDTO);
		} catch (NotFoundException e) {
			System.err.println(String.format("Error updating published geozone: %s", e.getMessage()));
		}
    }
	
	@JmsListener(destination = "${deleted_geozone_topic}")
    public void onDeletedGeozoneMessage(String json) {
		PublishedGeozoneDTO geozoneDTO = null;
		try {
			geozoneDTO = parseGeozoneDTO(json);
		} catch (Exception e) {
			System.err.println(String.format("Error parsing deleted published geozone: %s", e.getMessage()));
			return;
		}
		
		Geozone geozone = null;
		try {
			geozone = geozoneService.getGeozoneById(geozoneDTO.getId());
		} catch (NotFoundException e) {
			System.err.println(String.format("Error deleting published geozone: %s", e.getMessage()));
		}
		authorizationService.deleteByGeozone(geozone);
		geozoneService.delete(geozone);
    }
}