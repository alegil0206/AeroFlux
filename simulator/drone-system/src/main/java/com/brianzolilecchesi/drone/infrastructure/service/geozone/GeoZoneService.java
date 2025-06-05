package com.brianzolilecchesi.drone.infrastructure.service.geozone;
import com.brianzolilecchesi.drone.domain.model.GeoZone;
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.infrastructure.integration.GeoAwarenessRestClient;
import com.brianzolilecchesi.drone.infrastructure.service.geozone.registry.GeoZoneRegistry;
import com.brianzolilecchesi.drone.infrastructure.service.log.LogService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.brianzolilecchesi.drone.domain.dto.GeoZoneDTO;
import com.brianzolilecchesi.drone.domain.exception.ExternalServiceException;
import com.brianzolilecchesi.drone.domain.integration.GeoAwarenessGateway;

public class GeoZoneService {

    private final GeoAwarenessGateway restApiGateway;
    private final LogService logService;
    private final GeoZoneRegistry geoZoneRegistry;
    private DataStatus geoZonesStatus = DataStatus.NOT_REQUESTED;

    public GeoZoneService(LogService logService) {
        this.restApiGateway = new GeoAwarenessRestClient();
        this.geoZoneRegistry = new GeoZoneRegistry();
        this.logService = logService;
    }

    public CompletableFuture<Void> fetchGeoZones() {

        synchronized(this){
            if (geoZonesStatus == DataStatus.LOADING) return CompletableFuture.completedFuture(null);
            geoZonesStatus = DataStatus.LOADING;
        }

        logService.info(
                LogConstants.Component.GEOZONE_SERVICE,
                LogConstants.Event.FETCHING,
                "Fetching geo zones from geo-awareness service"
        );

        return CompletableFuture.supplyAsync(() -> {
            try {
                return restApiGateway.getGeoZones();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenAccept(geoZoneDTOs -> {
            if (geoZoneDTOs != null) {
                synchronized(this){
                    geoZoneRegistry.clear();
                    for (GeoZoneDTO dto : geoZoneDTOs) {
                        geoZoneRegistry.add(new GeoZone(dto));
                    }
                    geoZonesStatus = DataStatus.AVAILABLE;
                }
              
                logService.info(
                        LogConstants.Component.GEOZONE_SERVICE,
                        LogConstants.Event.FETCHED,
                        "Geo zones fetched successfully"
                ); 
            }
        }).exceptionally(e -> {
            synchronized(this) {
                geoZonesStatus = DataStatus.FAILED;
            }
            Throwable cause = e.getCause();
            if (cause instanceof ExternalServiceException) {
                logService.info(
                        LogConstants.Component.GEOZONE_SERVICE,
                        LogConstants.Event.FETCH_FAILED,
                        "Failed to fetch geo zones: " + cause.getMessage()
                );
            } else {
                logService.info(
                        LogConstants.Component.GEOZONE_SERVICE,
                        LogConstants.Event.FETCH_FAILED,
                        "Unexpected error while fetching geo zones: " + cause.getMessage()
                );
            }
            return null;
        });
    }

    public synchronized Map<String, GeoZone> getGeoZones() {
		return new HashMap<>(geoZoneRegistry.getAll());
	}

    public synchronized DataStatus getGeoZonesStatus() {
        return geoZonesStatus;
    }

    public synchronized void setGeoZonesStatus(DataStatus status) {
        this.geoZonesStatus = status;
    }
}

