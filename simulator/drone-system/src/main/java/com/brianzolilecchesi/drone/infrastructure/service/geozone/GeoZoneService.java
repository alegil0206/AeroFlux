package com.brianzolilecchesi.drone.infrastructure.service.geozone;
import com.brianzolilecchesi.drone.domain.model.GeoZone;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.infrastructure.integration.GeoAwarenessRestClient;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import com.brianzolilecchesi.drone.domain.exception.ExternalServiceException;
import com.brianzolilecchesi.drone.domain.integration.GeoAwarenessGateway;

public class GeoZoneService {

    private final GeoAwarenessGateway restApiGateway;
    private volatile List<GeoZone> geoZones = Collections.emptyList();
    private volatile DataStatus geoZonesStatus = DataStatus.NOT_REQUESTED;

    public GeoZoneService() {
        this.restApiGateway = new GeoAwarenessRestClient();
    }

    public void fetchGeoZones() {

        if (geoZonesStatus == DataStatus.LOADING) return;

        geoZonesStatus = DataStatus.LOADING;

        CompletableFuture.supplyAsync(() -> {
            try {
                return restApiGateway.getGeoZones();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenAccept(geoZoneDTOs -> {
            if (geoZoneDTOs != null) {
                geoZones = geoZoneDTOs.stream()
                        .map(GeoZone::new)
                        .collect(Collectors.toUnmodifiableList());
                geoZonesStatus = DataStatus.AVAILABLE;
            } else {
                geoZonesStatus = DataStatus.FAILED;
            }
        }).exceptionally(e -> {
            Throwable cause = e.getCause();
            if (cause instanceof ExternalServiceException) {
                System.err.println("Error communicating with geo-awareness service: " + cause.getMessage());
            } else {
                System.err.println("Unexpected error: " + e.getMessage());
            }
            geoZonesStatus = DataStatus.FAILED;
            return null;
        });
    }

    public List<GeoZone> getGeoZones() {
        return geoZones;
    }

    public DataStatus getGeoZonesStatus() {
        return geoZonesStatus;
    }
}

