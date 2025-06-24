package com.brianzolilecchesi.simulator.service.api;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.brianzolilecchesi.simulator.dto.UserCoordinatesDTO;

@Service
public class WeatherService extends AbstractMicroservice {
    private final RestTemplate restTemplate;

    public WeatherService() {
        super("http://api.uspace.local/weather");
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String getServiceName() {
        return "WEATHER";
    }

    @Override
    public void updateCoordinates(UserCoordinatesDTO coordinates) {
        restTemplate.put(serviceUrl + "/setting/coordinates", coordinates);
        System.out.println("Weather service updated with new coordinates.");
    }

    
}
