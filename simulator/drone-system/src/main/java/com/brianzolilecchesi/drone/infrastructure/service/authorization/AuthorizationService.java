package com.brianzolilecchesi.drone.infrastructure.service.authorization;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import com.brianzolilecchesi.drone.domain.dto.AuthorizationRequestDTO;
import com.brianzolilecchesi.drone.domain.dto.AuthorizationResponseDTO;
import com.brianzolilecchesi.drone.domain.exception.AuthorizationException;
import com.brianzolilecchesi.drone.domain.integration.GeoAuthorizationGateway;
import com.brianzolilecchesi.drone.domain.model.Authorization;

public class AuthorizationService {

    private final GeoAuthorizationGateway restApiGateway;
    private List<Authorization> authorizations;

    public AuthorizationService(GeoAuthorizationGateway restApiGateway) {
        this.restApiGateway = restApiGateway;
        this.authorizations = new ArrayList<>();
    }

    public Authorization requestNewAuthorization(AuthorizationRequestDTO authorizationRequestDTO) {
        try {
            AuthorizationResponseDTO authorizationDTO = restApiGateway.requestAuthorization(authorizationRequestDTO);
            return new Authorization(authorizationDTO); 
        } catch (AuthorizationException e) {
            System.err.println("Error while handling authorization: " + e.getMessage());
            return new Authorization(); 
        }
    }
    
    public void fetchAuthorizations(String droneId) {
        try {
            List<AuthorizationResponseDTO> authorizationDTOs = restApiGateway.getAuthorizations(droneId);
            authorizations = authorizationDTOs.stream()
                    .map(Authorization::new)
                    .toList();
        } catch (HttpClientErrorException.NotFound e) {
            System.err.println("Error 404: No authorization found for the drone - " + e.getMessage());
        } catch (RestClientException e) {
            System.err.println("Error communicating with authorization service: " + e.getMessage());
        }
    }

    public List<Authorization> getAuthorizations() {
        return authorizations;
    }
}
