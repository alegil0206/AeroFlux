package com.brianzolilecchesi.drone.infrastructure.service.weather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.brianzolilecchesi.drone.domain.exception.ExternalServiceException;
import com.brianzolilecchesi.drone.domain.integration.WeatherGateway;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.model.RainCell;
import com.brianzolilecchesi.drone.infrastructure.integration.WeatherServiceRestClient;
import com.brianzolilecchesi.drone.infrastructure.service.log.LogService;

public class WeatherService {

    private final WeatherGateway restApiGateway;
    private final LogService logService;

    private List<RainCell> rainCells = Collections.emptyList();
    private DataStatus rainCellsStatus = DataStatus.NOT_REQUESTED;

    public WeatherService(LogService logService) {
        this.restApiGateway = new WeatherServiceRestClient();
        this.logService = logService;
    }

    public CompletableFuture<Void> fetchRainCells() {

        synchronized(this) {
            if (rainCellsStatus == DataStatus.LOADING) return CompletableFuture.completedFuture(null);
            rainCellsStatus = DataStatus.LOADING;
        }

        logService.info(
                LogConstants.Component.WEATHER_SERVICE,
                LogConstants.Event.FETCHING,
                "Fetching rain cells from weather service"
        );
        return CompletableFuture.supplyAsync(() -> {
            try {
                return restApiGateway.getWeather();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenAccept(
            rainCellDTOs -> {
                if (rainCellDTOs != null) {
                    synchronized (this) {
                        rainCells = rainCellDTOs.stream()
                                .map(rainCellDTO -> new RainCell(rainCellDTO.getCoordinates()))
                                .toList();
                        rainCellsStatus = DataStatus.AVAILABLE;
                    }

                    logService.info(
                            LogConstants.Component.WEATHER_SERVICE,
                            LogConstants.Event.FETCHED,
                            "Rain Cells fetched successfully");
                } else {
                    synchronized (this) {
                        rainCellsStatus = DataStatus.FAILED;
                    }
                }
        })
        .exceptionally(e -> {
            synchronized (this) {
                rainCellsStatus = DataStatus.FAILED;
            }            
            Throwable cause = e.getCause();
            if (cause instanceof ExternalServiceException) {
                logService.info(
                        LogConstants.Component.WEATHER_SERVICE,
                        LogConstants.Event.FETCH_FAILED,
                        "Failed to fetch rain cells: " + cause.getMessage());
            } else {
                logService.info(
                        LogConstants.Component.WEATHER_SERVICE,
                        LogConstants.Event.FETCH_FAILED,
                        "Unexpected error while fetching rain cells: " + cause.getMessage());
            }
            return null;
        });
    }

    public synchronized List<RainCell> getRainCells() {
        return new ArrayList<>(rainCells);
    }

    public synchronized DataStatus getRainCellsStatus(){
        return rainCellsStatus;
    }

    public synchronized void setRainCellsStatus(DataStatus status) {
        this.rainCellsStatus = status;
    }
}
