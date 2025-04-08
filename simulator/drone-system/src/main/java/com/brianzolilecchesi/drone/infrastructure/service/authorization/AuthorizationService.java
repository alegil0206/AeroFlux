package com.brianzolilecchesi.drone.infrastructure.service.authorization;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import com.brianzolilecchesi.drone.domain.dto.AuthorizationRequestDTO;
import com.brianzolilecchesi.drone.domain.dto.AuthorizationResponseDTO;
import com.brianzolilecchesi.drone.domain.exception.AuthorizationException;
import com.brianzolilecchesi.drone.domain.model.Authorization;
import com.brianzolilecchesi.drone.infrastructure.integration.RestApiGateway;

public class AuthorizationService {
    private final RestApiGateway restApiGateway;


    public AuthorizationService(RestApiGateway restApiGateway) {
        this.restApiGateway = restApiGateway;
    }

    public Authorization getResponseAuthorization(AuthorizationRequestDTO authorizationRequestDTO) {
        try {
            AuthorizationResponseDTO authorizationDTO = restApiGateway.requestAuthorization(authorizationRequestDTO);
            return new Authorization(authorizationDTO); 
        } catch (AuthorizationException e) {
            System.err.println("Error while handling authorization: " + e.getMessage());
            return new Authorization(); 
        }
    }
    

    public List<Authorization> getAuthorizations(String droneId) {
        try {
            List<AuthorizationResponseDTO> authorizationDTOs = restApiGateway.getAuthorizations(droneId);
            return authorizationDTOs.stream()
                    .map(Authorization::new)
                    .toList();
        } catch (HttpClientErrorException.NotFound e) {
            System.err.println("Error 404: No authorization found for the drone - " + e.getMessage());
            return Collections.emptyList();
        } catch (RestClientException e) {
            System.err.println("Error communicating with authorization service: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
