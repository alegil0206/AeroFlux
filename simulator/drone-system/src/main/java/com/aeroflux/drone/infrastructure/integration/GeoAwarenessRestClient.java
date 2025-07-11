package com.aeroflux.drone.infrastructure.integration;

import com.aeroflux.drone.domain.integration.GeoAwarenessGateway;
import com.aeroflux.drone.domain.dto.GeoZoneDTO;
import com.aeroflux.drone.domain.dto.SupportPointDTO;
import com.aeroflux.drone.domain.exception.ExternalServiceException;

import java.util.Collections;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


public class GeoAwarenessRestClient implements GeoAwarenessGateway {

    private final String geozoneApiUrl;
    private final RestTemplate restTemplate;

    public GeoAwarenessRestClient(String geozoneApiUrl, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.geozoneApiUrl = geozoneApiUrl;
    }

    @Override
    public List<GeoZoneDTO> getGeoZones() throws ExternalServiceException {
        try{
            ResponseEntity<List<GeoZoneDTO>> response = restTemplate.exchange(
                geozoneApiUrl + "/geozone",
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
                geozoneApiUrl + "/support-point",
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
