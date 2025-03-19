package com.brianzolilecchesi.simulator.service;

import com.brianzolilecchesi.simulator.model.ServiceEndpoint;
import com.brianzolilecchesi.simulator.dto.ServiceEndpointDTO;
import com.brianzolilecchesi.simulator.dto.UserCoordinatesDTO;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.ArrayList;

@Service
public class ServiceEndpointsService {

    private List<ServiceEndpoint> services;
    private final RestTemplate restTemplate;

    public ServiceEndpointsService() {
        this.restTemplate = new RestTemplate();
        this.services = new ArrayList<>();
        
        // Definizione statica dei servizi conosciuti
        services.add(new ServiceEndpoint("drone_identification", "http://api.uspace.local/drone-identification"));
        services.add(new ServiceEndpoint("geo_awareness", "http://api.uspace.local/geo-awareness"));
        services.add(new ServiceEndpoint("geo_authorization", "http://api.uspace.local/geo-authorization"));
        services.add(new ServiceEndpoint("weather", "http://api.uspace.local/weather"));
    }

    public List<ServiceEndpointDTO> getAllServices() {
        List<ServiceEndpointDTO> dtoList = new ArrayList<>();
        for (ServiceEndpoint service : services) {
            dtoList.add(new ServiceEndpointDTO(service.getName(), service.getUrl()));
        }
        return dtoList;
    }

    public void updateServiceUrl(String serviceName, String newUrl) {
        for (ServiceEndpoint service : services) {
            if (service.getName().equalsIgnoreCase(serviceName)) {
                service.setUrl(newUrl);
                break;
            }
        }
    }

    public void notifyServices(UserCoordinatesDTO coordinates) {
        for (ServiceEndpoint service : services) {
            try {
                // If the service is "weather", update only the coordinates
                if (service.getName().equals("weather")) { 
                    restTemplate.put(service.getUrl() + "/setting/coordinates", coordinates);
                    System.out.println("Weather service notified with new coordinates.");
                    continue; // Skip the rest of the code for the weather service
                }
    
                // For other services, delete existing data
                switch (service.getName()) {
                    case "drone_identification":
                        restTemplate.delete(service.getUrl() + "/drone");
                        System.out.println("All drones deleted.");
                        break;
                    case "geo_authorization":
                        restTemplate.delete(service.getUrl() + "/authorization");
                        System.out.println("All authorizations deleted.");
                        break;
                    case "geo_awareness":
                        restTemplate.delete(service.getUrl() + "/geozone");
                        System.out.println("All geozones deleted.");
                        break;
                    default:
                        System.out.println("No reset action for service: " + service.getName());
                }
            } catch (Exception e) {
                System.err.println("Error notifying service: " + service.getName() + " - " + e.getMessage());
            }
        }
    }
    
}
