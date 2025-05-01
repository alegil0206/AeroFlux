package com.brianzolilecchesi.drone.infrastructure.service.supportPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.brianzolilecchesi.drone.domain.exception.ExternalServiceException;
import com.brianzolilecchesi.drone.domain.integration.GeoAwarenessGateway;
import com.brianzolilecchesi.drone.domain.model.SupportPoint;
import com.brianzolilecchesi.drone.infrastructure.integration.GeoAwarenessRestClient;

public class SupportPointService {
    
    private final GeoAwarenessGateway geoAwarenessGateway;
    private List<SupportPoint> supportPoints;

    public SupportPointService() {
        this.geoAwarenessGateway = new GeoAwarenessRestClient();
        this.supportPoints = new ArrayList<>();
    }

    public void fetchSupportPoints() {
        CompletableFuture.supplyAsync(() -> {
            try {
                return geoAwarenessGateway.getSupportPoints();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenAccept(supportPointDTOs -> {
            if (supportPointDTOs != null) {
                supportPoints = supportPointDTOs.stream()
                    .map(dto -> new SupportPoint(dto))
                    .toList();
            }
        }).exceptionally(e -> {
            Throwable cause = e.getCause();
            if (cause instanceof ExternalServiceException) {
                System.err.println("Error communicating with geo-awareness service: " + cause.getMessage());
            } else {
                System.err.println("Unexpected error: " + e.getMessage());
            }
            return null;
        });
    }

    public List<SupportPoint> getSupportPoints() {
        return supportPoints;
    }
}
