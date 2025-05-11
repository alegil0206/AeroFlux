package com.brianzolilecchesi.drone.infrastructure.service.geozone;
import com.brianzolilecchesi.drone.domain.model.GeoZone;
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.service.log.LogService;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.infrastructure.integration.GeoAwarenessRestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import com.brianzolilecchesi.drone.domain.exception.ExternalServiceException;
import com.brianzolilecchesi.drone.domain.integration.GeoAwarenessGateway;

public class GeoZoneService {

    private final GeoAwarenessGateway restApiGateway;
    private final LogService logService;

    private List<GeoZone> geoZones = Collections.emptyList();
    private DataStatus geoZonesStatus = DataStatus.NOT_REQUESTED;

    public GeoZoneService(LogService logService) {
        this.restApiGateway = new GeoAwarenessRestClient();
        this.logService = logService;
    }

    public void fetchGeoZones() {

        synchronized(this){
            if (geoZonesStatus == DataStatus.LOADING) return;
            geoZonesStatus = DataStatus.LOADING;
        }

        logService.info(
                LogConstants.Component.GEOZONE_SERVICE,
                LogConstants.Event.FETCHING,
                "Fetching geo zones from geo-awareness service"
        );

        CompletableFuture.supplyAsync(() -> {
            try {
                return restApiGateway.getGeoZones();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenAccept(geoZoneDTOs -> {
            if (geoZoneDTOs != null) {
                synchronized(this){
                    geoZones = geoZoneDTOs.stream()
                    .map(GeoZone::new)
                    .collect(Collectors.toUnmodifiableList());
                    geoZonesStatus = DataStatus.AVAILABLE;
                }
              
                logService.info(
                        LogConstants.Component.GEOZONE_SERVICE,
                        LogConstants.Event.FETCHED,
                        "Geo zones fetched successfully"
                );
            } else {
                synchronized(this) {
                    geoZonesStatus = DataStatus.FAILED;
                }
                
            }
        }).exceptionally(e -> {
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
            synchronized(this) {
                geoZonesStatus = DataStatus.FAILED;    
            }
            
            return null;
        });
    }

    public synchronized List<GeoZone> getGeoZones() {
        return new ArrayList<>(geoZones);
    }

    public synchronized DataStatus getGeoZonesStatus() {
        return geoZonesStatus;
    }

}

