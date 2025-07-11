package com.aeroflux.drone.infrastructure.service.authorization;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.aeroflux.drone.domain.dto.AuthorizationRequestDTO;
import com.aeroflux.drone.domain.exception.AuthorizationException;
import com.aeroflux.drone.domain.exception.ExternalServiceException;
import com.aeroflux.drone.domain.integration.GeoAuthorizationGateway;
import com.aeroflux.drone.domain.model.Authorization;
import com.aeroflux.drone.domain.model.DataStatus;
import com.aeroflux.drone.domain.model.GeoZone;
import com.aeroflux.drone.domain.model.LogConstants;
import com.aeroflux.drone.domain.model.Position;
import com.aeroflux.drone.domain.navigation.FlightPlanCalculatorFacade;
import com.aeroflux.drone.domain.navigation.flight_plan.model.graph.FlightPlanCalculator;
import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.Geozone;
import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.Zone;
import com.aeroflux.drone.infrastructure.service.authorization.registry.AuthorizationRegistry;
import com.aeroflux.drone.infrastructure.service.log.LogService;

public class AuthorizationService {

    private LogService logService;
    private final GeoAuthorizationGateway restApiGateway;
    private final AuthorizationRegistry authorizationRegistry;
    private DataStatus authorizationsStatus = DataStatus.NOT_REQUESTED;

    public AuthorizationService(LogService logService, GeoAuthorizationGateway restApiGateway) {
        this.logService = logService;
        this.restApiGateway = restApiGateway;
        this.authorizationRegistry = new AuthorizationRegistry();
    }

    public CompletableFuture<Void> requestAuthorization(final String droneId, final String geoZoneId) {

        return CompletableFuture.supplyAsync(() -> {
            AuthorizationRequestDTO authorizationRequestDTO = new AuthorizationRequestDTO(
                droneId,
                geoZoneId,
                Authorization.DurationType.STANDARD.getName()
            );
            logService.info(
                LogConstants.Component.AUTHORIZATION_SERVICE,
                LogConstants.Event.REQUESTING_AUTHORIZATION,
                "Requesting authorization for geoZone " + geoZoneId
            );
            try {
                return restApiGateway.requestAuthorization(authorizationRequestDTO);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenAccept(authorizationDTO -> {
            if (authorizationDTO != null) {
                Authorization authorization = new Authorization(authorizationDTO);
                if(authorization.isGranted()) {
                    logService.info(
                        LogConstants.Component.AUTHORIZATION_SERVICE,
                        LogConstants.Event.AUTHORIZATION_GRANTED,
                        "Authorization granted for geoZone " + geoZoneId + ": " + authorization.getId()
                    );
                } else {
                    logService.info(
                        LogConstants.Component.AUTHORIZATION_SERVICE,
                        LogConstants.Event.AUTHORIZATION_DENIED,
                        "Authorization denied for geoZone " + geoZoneId
                    );
                }
            }
        }).exceptionally(e -> {
            Throwable cause = e.getCause();
            if (cause instanceof ExternalServiceException) {
                logService.info(
                    LogConstants.Component.AUTHORIZATION_SERVICE,
                    LogConstants.Event.FETCH_FAILED,
                    "Failed to request authorization: " + cause.getMessage()
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
                    "Unexpected error while requesting authorization: " + cause.getMessage()
                );
            }
            return null;
        });
    }
    
    public CompletableFuture<Void> fetchAuthorizations(String droneId) {

        synchronized(this) {
            if (authorizationsStatus == DataStatus.LOADING) return CompletableFuture.completedFuture(null);
            authorizationsStatus = DataStatus.LOADING;
        }

        logService.info(
            LogConstants.Component.AUTHORIZATION_SERVICE,
            LogConstants.Event.FETCHING,
            "Fetching authorizations from geo-authorization service"
        );

        return CompletableFuture.supplyAsync(() -> {
            try {
                return restApiGateway.getAuthorizations(droneId);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenAccept(authorizationDTOs -> {
            if (authorizationDTOs != null) {
                List<Authorization> grantedAuthorizations = new ArrayList<>();
                authorizationDTOs.forEach(dto -> {
                    if (dto.isGranted())
                        grantedAuthorizations.add(new Authorization(dto));
                });
                synchronized(this) {
		            authorizationRegistry.clear();
    	            authorizationRegistry.addAll(grantedAuthorizations);
                    authorizationsStatus = DataStatus.AVAILABLE;
                }
                logService.info(
                    LogConstants.Component.AUTHORIZATION_SERVICE,
                    LogConstants.Event.FETCHED,
                    "Authorizations fetched successfully"
                );
            }
        }).exceptionally(e -> {
            synchronized(this) {
                authorizationsStatus = DataStatus.FAILED; 
            }
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

	public void requestLinearPathAuthorizations(String droneId, final Position source, final Position destination, Map<String, GeoZone> geoZones) {

        FlightPlanCalculatorFacade calculatorService = new FlightPlanCalculatorFacade();
		FlightPlanCalculator calculator = calculatorService.getFlightPlanCalculator();
        calculatorService.getGeozoneService().add(new ArrayList<>(geoZones.values()));

        List<Zone> overlappingZones = calculator.getLinearPathZones(source, destination);
		List<GeoZone> overlappingGeozones = new ArrayList<>();
		
		for (Zone zone : overlappingZones) {
			if (zone instanceof Geozone) {
				Geozone geozoneZone = (Geozone) zone;
				GeoZone geoZone = geoZones.get(geozoneZone.getId());
				if (geoZone != null) {
					overlappingGeozones.add(geoZone);
				}
			}
		}
		
		Map<String, Authorization> authorizations = getAuthorizations();
		List<GeoZone> geozonesToRequest = new ArrayList<>();
		
		for (GeoZone geoZone : overlappingGeozones) {
			if (geoZone.getCategory().equals(GeoZone.Category.EXCLUDED.getName())) {
				continue;
			}
			
			if (geoZone.getStatus().equals(GeoZone.Status.INACTIVE.getName())) {
				continue;
			}
			
			Authorization authorization = authorizations.get(geoZone.getId());
			if (authorization == null) {
				geozonesToRequest.add(geoZone);
			}
		}

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (GeoZone geoZone : geozonesToRequest) {
            futures.add(requestAuthorization(droneId, geoZone.getId()));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenRun(() -> {
                authorizationsStatus = DataStatus.EXPIRED;
            });
	}    
}
