package com.brianzolilecchesi.drone.infrastructure.service.authorization;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.brianzolilecchesi.drone.domain.dto.AuthorizationRequestDTO;
import com.brianzolilecchesi.drone.domain.dto.AuthorizationDTO;
import com.brianzolilecchesi.drone.domain.exception.AuthorizationException;
import com.brianzolilecchesi.drone.domain.exception.ExternalServiceException;
import com.brianzolilecchesi.drone.domain.integration.GeoAuthorizationGateway;
import com.brianzolilecchesi.drone.domain.model.Authorization;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.service.log.LogService;
import com.brianzolilecchesi.drone.infrastructure.integration.GeoAuthorizationRestClient;
import com.brianzolilecchesi.drone.infrastructure.service.authorization.registry.AuthorizationRegistry;

public class AuthorizationService {

    private LogService logService;
    private final GeoAuthorizationGateway restApiGateway;
    private final AuthorizationRegistry authorizationRegistry;
    private DataStatus authorizationsStatus = DataStatus.NOT_REQUESTED;

    public AuthorizationService(LogService logService) {
        this.logService = logService;
        this.restApiGateway = new GeoAuthorizationRestClient();
        this.authorizationRegistry = new AuthorizationRegistry();
    }

    public Optional<Authorization> requestAuthorization(final AuthorizationRequestDTO authorizationRequestDTO) {
        AuthorizationDTO authorizationDTO = null;
        try {
            authorizationDTO = restApiGateway.requestAuthorization(authorizationRequestDTO);
        } catch (AuthorizationException | ExternalServiceException e) {
            logService.info(
                LogConstants.Component.AUTHORIZATION_SERVICE,
                LogConstants.Event.FETCH_FAILED,
                "Error requesting authorization: " + e.getMessage()
            );
            return Optional.empty();
        }
        Authorization authorization = new Authorization(authorizationDTO);
        logService.info(
            LogConstants.Component.AUTHORIZATION_SERVICE,
            LogConstants.Event.FETCHED,
            "Authorization requested successfully: " + authorization.getId()
        );
        synchronized(this) {
            if (!(authorizationsStatus == DataStatus.LOADING || authorizationsStatus == DataStatus.FAILED)) {
                authorizationRegistry.add(authorization);
            }
        }
        return Optional.of(authorization);
    }
    
    public void fetchAuthorizations(String droneId) {

        synchronized(this) {
            if (authorizationsStatus == DataStatus.LOADING) return;
            authorizationsStatus = DataStatus.LOADING;
        }

        logService.info(
            LogConstants.Component.AUTHORIZATION_SERVICE,
            LogConstants.Event.FETCHING,
            "Fetching authorizations from geo-authorization service"
        );

        CompletableFuture.supplyAsync(() -> {
            try {
                return restApiGateway.getAuthorizations(droneId);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenAccept(authorizationDTOs -> {
            if (authorizationDTOs != null) {
                List<Authorization> authorizations = new ArrayList<>();
                authorizationDTOs.forEach(dto -> {
                    authorizations.add(new Authorization(dto));
                });
                synchronized(this) {
		            authorizationRegistry.clear();
    	            authorizationRegistry.addAll(authorizations);
                    authorizationsStatus = DataStatus.AVAILABLE;
                }
                logService.info(
                    LogConstants.Component.AUTHORIZATION_SERVICE,
                    LogConstants.Event.FETCHED,
                    "Authorizations fetched successfully"
                );
            }
        }).exceptionally(e -> {
            Throwable cause = e.getCause();
            if (cause instanceof ExternalServiceException) {
                logService.info(
                    LogConstants.Component.AUTHORIZATION_SERVICE,
                    LogConstants.Event.FETCH_FAILED,
                    "Failed to fetch authorizations: " + cause.getMessage()
                );
            } else if (cause instanceof AuthorizationException) {
                logService.info(
                    LogConstants.Component.AUTHORIZATION_SERVICE,
                    LogConstants.Event.FETCH_FAILED,
                    "Authorization error: " + cause.getMessage()
                );
            } else {
                logService.info(
                    LogConstants.Component.AUTHORIZATION_SERVICE,
                    LogConstants.Event.FETCH_FAILED,
                    "Unexpected error while fetching authorizations: " + cause.getMessage()
                );
            }
            return null;
        });

        synchronized(this) {
            authorizationsStatus = DataStatus.FAILED;    
        }
    }

    public synchronized Map<String, Authorization> getAuthorizations() {
        return new HashMap<>(authorizationRegistry.getAll());
    }

    public synchronized DataStatus getAuthorizationsStatus() {
        return authorizationsStatus;
    }

    public synchronized void setAuthorizationsStatus(DataStatus status) {
        this.authorizationsStatus = status;
    }
    
}
