package com.brianzolilecchesi.simulator.service.api;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoAuthorizationService extends AbstractMicroservice {
    
    private final RestTemplate restTemplate;

    public GeoAuthorizationService() {
        super("http://api.uspace.local/geo-authorization");
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String getServiceName() {
        return "geo_authorization";
    }

    @Override
    public void resetMicroservice() {
        try {
            restTemplate.delete(serviceUrl + "/authorization");
            System.out.println("All authorizations deleted.");
        } catch (Exception e) {
            System.err.println("Error notifying geo_authorization: " + e.getMessage());
        }
    }
    
}
