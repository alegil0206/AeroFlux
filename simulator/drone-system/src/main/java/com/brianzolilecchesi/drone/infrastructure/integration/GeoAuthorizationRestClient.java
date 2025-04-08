package com.brianzolilecchesi.drone.infrastructure.integration;

import com.brianzolilecchesi.drone.domain.integration.GeoAuthorizationClient;
import com.brianzolilecchesi.drone.domain.dto.AuthorizationRequestDTO;
import com.brianzolilecchesi.drone.domain.dto.AuthorizationResponseDTO;
import com.brianzolilecchesi.drone.domain.exception.AuthorizationException;

import java.util.Collections;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


public class GeoAuthorizationRestClient implements GeoAuthorizationClient {
    private static final String authorizationApiUrl = "http://api.uspace.local/geo-authorization";
    private final RestTemplate restTemplate;

    public GeoAuthorizationRestClient() {
        this.restTemplate = new RestTemplate();
    }

    public GeoAuthorizationRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AuthorizationResponseDTO requestAuthorization(AuthorizationRequestDTO authorizationRequestDTO) throws AuthorizationException {
        try {
            ResponseEntity<AuthorizationResponseDTO> responseEntity = restTemplate.postForEntity(
                authorizationApiUrl + "/authorization",
                new HttpEntity<>(authorizationRequestDTO),
                AuthorizationResponseDTO.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println(e.getMessage());
            throw new AuthorizationException(e.getMessage());
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
            throw new AuthorizationException(e.getMessage());
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
            throw new AuthorizationException(e.getMessage());
        }
    }

    @Override
    public List<AuthorizationResponseDTO> getAuthorizations(String droneId) {
        System.out.println("Call to geo-authorization API for the drone: " + droneId);

        try {
            ResponseEntity<List<AuthorizationResponseDTO>> response = restTemplate.exchange(
                authorizationApiUrl + "/authorization/drone/" + droneId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AuthorizationResponseDTO>>() {}
            );

            return response.getBody() != null ? response.getBody() : Collections.emptyList();

        } catch (HttpClientErrorException.NotFound e) {
            System.err.println("Drone not found: " + e.getMessage());
            return Collections.emptyList();

        } catch (RestClientException e) {
            System.err.println("Error during comunication with authorization service: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
