package com.brianzolilecchesi.simulator.service;

import com.brianzolilecchesi.simulator.model.UserCoordinates;
import com.brianzolilecchesi.simulator.dto.UserCoordinatesDTO;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.brianzolilecchesi.simulator.service.api.AbstractMicroservice;
import com.brianzolilecchesi.simulator.service.api.WeatherService;

@Service
public class UserCoordinatesService {

    private final MicroserviceRegistryService microserviceRegistryService;
    private UserCoordinates userCoordinates;

    public UserCoordinatesService(MicroserviceRegistryService microserviceRegistryService) {
        this.microserviceRegistryService = microserviceRegistryService;
        this.userCoordinates = new UserCoordinates(45.476592, 9.219752);
    }

    public UserCoordinatesDTO getCoordinates() {
        return new UserCoordinatesDTO(userCoordinates.getCenterLat(), userCoordinates.getCenterLon());
    }

    public void updateCoordinates(UserCoordinatesDTO newCoordinates) {
        userCoordinates.setCenterLat(newCoordinates.getLatitude());
        userCoordinates.setCenterLon(newCoordinates.getLongitude());

        Map<String, AbstractMicroservice> serviceHandlers = microserviceRegistryService.getServiceHandlers();

        for (AbstractMicroservice service : serviceHandlers.values()) {
            if (service instanceof WeatherService) {
                service.updateCoordinates(newCoordinates);
            } else {
                service.resetMicroservice();
            }
        }
    }
}
