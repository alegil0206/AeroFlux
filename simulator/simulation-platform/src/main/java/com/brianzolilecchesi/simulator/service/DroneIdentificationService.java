package com.brianzolilecchesi.simulator.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.brianzolilecchesi.simulator.dto.DronePropertiesDTO;

@Service
public class DroneIdentificationService {
    
    private final RestTemplate restTemplate;
    private final MicroserviceRegistryService microserviceRegistryService;

    public DroneIdentificationService(MicroserviceRegistryService microserviceRegistryService) {
        this.restTemplate = new RestTemplate();
        this.microserviceRegistryService = microserviceRegistryService;
    }

    public List<DronePropertiesDTO> getDrones() {
        String serviceUrl = microserviceRegistryService.getServiceUrls().get("DRONE_IDENTIFICATION");
        return restTemplate.exchange(serviceUrl + "/drone", 
                                     HttpMethod.GET, 
                                     null, 
                                     new ParameterizedTypeReference<List<DronePropertiesDTO>>() {}).getBody();
    }
    
}
