package com.brianzolilecchesi.drone.infrastructure.service.authorization;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.brianzolilecchesi.drone.domain.dto.AuthorizationRequestDTO;
import com.brianzolilecchesi.drone.domain.dto.AuthorizationResponseDTO;
import com.brianzolilecchesi.drone.domain.exception.AuthorizationException;
import com.brianzolilecchesi.drone.domain.exception.ExternalServiceException;
import com.brianzolilecchesi.drone.domain.integration.GeoAuthorizationGateway;
import com.brianzolilecchesi.drone.domain.model.Authorization;
import com.brianzolilecchesi.drone.infrastructure.integration.GeoAuthorizationRestClient;

public class AuthorizationService {

    private final GeoAuthorizationGateway restApiGateway;
    private List<Authorization> authorizations;

    public AuthorizationService() {
        this.restApiGateway = new GeoAuthorizationRestClient();
        this.authorizations = new ArrayList<>();
    }

    public Authorization requestNewAuthorization(AuthorizationRequestDTO authorizationRequestDTO) {
        try {
            AuthorizationResponseDTO authorizationDTO = restApiGateway.requestAuthorization(authorizationRequestDTO);
            if (authorizationDTO != null) {
                Authorization authorization = new Authorization(authorizationDTO);
                authorizations.add(authorization);
                return authorization;
            } else {
                throw new AuthorizationException("Authorization response is null");
            }
        } catch (AuthorizationException | ExternalServiceException e) {
            System.err.println("Error requesting authorization: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return null;
        }
    }
    
    public void fetchAuthorizations(String droneId) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return restApiGateway.getAuthorizations(droneId);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenAccept(authorizationDTOs -> {
            if (authorizationDTOs != null) {
                authorizations = authorizationDTOs.stream()
                        .map(Authorization::new)
                        .toList();
            }
        }).exceptionally(e -> {
            Throwable cause = e.getCause();
            if (cause instanceof ExternalServiceException) {
                System.err.println("Error communicating with geo-authorization service: " + cause.getMessage());
            } else if (cause instanceof AuthorizationException) {
                System.err.println("Authorization error: " + cause.getMessage());
            } else {
                System.err.println("Unexpected error: " + e.getMessage());
            }
            return null;
        });
    }

    public List<Authorization> getAuthorizations() {
        return authorizations;
    }
}
