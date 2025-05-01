package com.brianzolilecchesi.drone.infrastructure.integration;

import com.brianzolilecchesi.drone.domain.integration.GeoAwarenessGateway;
import com.brianzolilecchesi.drone.domain.dto.GeoZoneDTO;
import com.brianzolilecchesi.drone.domain.dto.SupportPointDTO;
import com.brianzolilecchesi.drone.domain.exception.ExternalServiceException;

import java.util.Collections;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


public class GeoAwarenessRestClient implements GeoAwarenessGateway {

    private static final String geozone_api_url = "http://api.uspace.local/geo-awareness";
    private final RestTemplate restTemplate;

    public GeoAwarenessRestClient() {
        this.restTemplate = new RestTemplate();
    }
    
    public GeoAwarenessRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<GeoZoneDTO> getGeoZones() throws ExternalServiceException {
        try{
            ResponseEntity<List<GeoZoneDTO>> response = restTemplate.exchange(
                geozone_api_url + "/geozone",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<GeoZoneDTO>>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (RestClientException e) {
            throw new ExternalServiceException("Error during communication with geo-awareness service: " + e.getMessage());
        }
    }

    @Override
    public List<SupportPointDTO> getSupportPoints() throws ExternalServiceException {
        try{
            ResponseEntity<List<SupportPointDTO>> response = restTemplate.exchange(
                geozone_api_url + "/support-point",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SupportPointDTO>>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (RestClientException e) {
            throw new ExternalServiceException("Error during communication with geo-awareness service: " + e.getMessage());
        }
    }
}
