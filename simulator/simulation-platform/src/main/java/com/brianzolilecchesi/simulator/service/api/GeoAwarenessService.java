package com.brianzolilecchesi.simulator.service.api;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class GeoAwarenessService extends AbstractMicroservice {
    
    private final RestTemplate restTemplate;

    public GeoAwarenessService() {
        super("http://api.uspace.local/geo-awareness");
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String getServiceName() {
        return "geo_awareness";
    }

    @Override
    public void resetMicroservice() {
        try {
            restTemplate.delete(serviceUrl + "/geozone");
            System.out.println("All geozones deleted from Geo Awareness Service.");
        } catch (Exception e) {
            System.err.println("Error notifying geo_awareness: " + e.getMessage());
        }
    }
    
}
