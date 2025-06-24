package com.brianzolilecchesi.drone.infrastructure.service.supportPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.brianzolilecchesi.drone.domain.exception.ExternalServiceException;
import com.brianzolilecchesi.drone.domain.integration.GeoAwarenessGateway;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.domain.model.SupportPoint;
import com.brianzolilecchesi.drone.infrastructure.service.log.LogService;

public class SupportPointService {
    
    private final GeoAwarenessGateway geoAwarenessGateway;
    private LogService logService;
    private List<SupportPoint> supportPoints;
    private DataStatus supportPointsStatus = DataStatus.NOT_REQUESTED;

    public SupportPointService(LogService logService, GeoAwarenessGateway geoAwarenessGateway) {
        this.logService = logService;
        this.geoAwarenessGateway = geoAwarenessGateway;
        this.supportPoints = new ArrayList<>();
    }

    public CompletableFuture<Void> fetchSupportPoints() {

        synchronized(this) {
            if (supportPointsStatus == DataStatus.LOADING) return CompletableFuture.completedFuture(null);
            supportPointsStatus = DataStatus.LOADING;
        }

        logService.info(
                LogConstants.Component.SUPPORT_POINT_SERVICE,
                LogConstants.Event.FETCHING,
                "Fetching support points from geo-awareness service"
        );
        
        return CompletableFuture.supplyAsync(() -> {
            try {
                return geoAwarenessGateway.getSupportPoints();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenAccept(supportPointDTOs -> {
            if (supportPointDTOs != null) {
                synchronized(this) {
                    supportPointsStatus = DataStatus.AVAILABLE;
                    supportPoints = supportPointDTOs.stream()
                        .map(dto -> new SupportPoint(dto))
                        .toList();
                }
                logService.info(
                        LogConstants.Component.SUPPORT_POINT_SERVICE,
                        LogConstants.Event.FETCHED,
                        "Support points fetched successfully"
                );
            }
        }).exceptionally(e -> {
            synchronized(this) {
                supportPointsStatus = DataStatus.FAILED;
            }
            Throwable cause = e.getCause();
            if (cause instanceof ExternalServiceException) {
                logService.info(
                        LogConstants.Component.SUPPORT_POINT_SERVICE,
                        LogConstants.Event.FETCH_FAILED,
                        "Failed to fetch support points from geo-awareness service: " + cause.getMessage()
                );
            } else {
                logService.info(
                        LogConstants.Component.SUPPORT_POINT_SERVICE,
                        LogConstants.Event.FETCH_FAILED,
                        "An unexpected error occurred while fetching support points: " + cause.getMessage()
                );
            }
            return null;
        });
    }

    public SupportPoint getClosestSupportPoint(Position currentPosition, List<SupportPoint> points) {
        return points.stream()
                .min((sp1, sp2) -> Double.compare(
                        currentPosition.distance(sp1.getCoordinate()),
                        currentPosition.distance(sp2.getCoordinate())
                ))
                .orElse(null);
    }

    public synchronized DataStatus getSupportPointsStatus() {
        return supportPointsStatus;
    }

    public synchronized List<SupportPoint> getSupportPoints() {
        return new ArrayList<>(supportPoints);
    }
}