package com.aeroflux.drone.infrastructure.integration;

import com.aeroflux.drone.domain.integration.GeoAuthorizationGateway;
import com.aeroflux.drone.domain.dto.AuthorizationRequestDTO;
import com.aeroflux.drone.domain.dto.AuthorizationDTO;
import com.aeroflux.drone.domain.exception.AuthorizationException;
import com.aeroflux.drone.domain.exception.ExternalServiceException;

import java.util.Collections;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


public class GeoAuthorizationRestClient implements GeoAuthorizationGateway {
    private final String authorizationApiUrl;
    private final RestTemplate restTemplate;

    public GeoAuthorizationRestClient(String authorizationApiUrl, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.authorizationApiUrl = authorizationApiUrl;
    }

    @Override
    public AuthorizationDTO requestAuthorization(AuthorizationRequestDTO authorizationRequestDTO) throws AuthorizationException, ExternalServiceException {
        try {
            ResponseEntity<AuthorizationDTO> responseEntity = restTemplate.postForEntity(
                authorizationApiUrl + "/authorization",
                new HttpEntity<>(authorizationRequestDTO),
                AuthorizationDTO.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            throw new AuthorizationException(e.getMessage());
        } catch (RestClientException e) {
            throw new ExternalServiceException(e.getMessage());
        }
    }

    @Override
    public List<AuthorizationDTO> getAuthorizations(String droneId) throws AuthorizationException, ExternalServiceException {
        System.out.println("Call to geo-authorization API for the drone: " + droneId);
        try {
            ResponseEntity<List<AuthorizationDTO>> response = restTemplate.exchange(
                authorizationApiUrl + "/authorization/drone/" + droneId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AuthorizationDTO>>() {}
            );
            return response.getBody() != null ? response.getBody() : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            throw new AuthorizationException("Drone not found: " + e.getMessage());
        } catch (RestClientException e) {
            throw new ExternalServiceException("Error during comunication with authorization service: " + e.getMessage());
        }
    }
}
