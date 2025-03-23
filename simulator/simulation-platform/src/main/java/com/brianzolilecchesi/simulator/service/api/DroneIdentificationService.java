package com.brianzolilecchesi.simulator.service.api;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DroneIdentificationService extends AbstractMicroservice {
    
    private final RestTemplate restTemplate;

    public DroneIdentificationService() {
        super("http://api.uspace.local/drone-identification");
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String getServiceName() {
        return "drone_identification";
    }

    @Override
    public void resetMicroservice() {
        try {
            restTemplate.delete(serviceUrl + "/drone");
            System.out.println("All drones deleted from Drone Identification Service.");
        } catch (Exception e) {
            System.err.println("Error notifying drone_identification: " + e.getMessage());
        }
    }
    
}
