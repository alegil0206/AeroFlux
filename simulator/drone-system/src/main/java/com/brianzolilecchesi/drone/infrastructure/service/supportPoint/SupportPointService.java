package com.brianzolilecchesi.drone.infrastructure.service.supportPoint;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import com.brianzolilecchesi.drone.domain.dto.SupportPointDTO;
import com.brianzolilecchesi.drone.domain.integration.GeoAwarenessGateway;
import com.brianzolilecchesi.drone.domain.model.SupportPoint;

public class SupportPointService {
    
    private final GeoAwarenessGateway geoAwarenessGateway;
    private List<SupportPoint> supportPoints;

    public SupportPointService(GeoAwarenessGateway geoAwarenessGateway) {
        this.geoAwarenessGateway = geoAwarenessGateway;
        this.supportPoints = new ArrayList<>();
    }

    public void fetchSupportPoints() {
        try {      
            List<SupportPointDTO> supportPointDTOs = geoAwarenessGateway.getSupportPoints();
            supportPoints = supportPointDTOs.stream()
                .map(dto -> new SupportPoint(dto))
                .toList();
        } catch (HttpClientErrorException.NotFound e) {
            System.err.println("Error 404: Support Points not found - " + e.getMessage());
        } catch (RestClientException e) {
            System.err.println("Error communicating with geo-awareness service: " + e.getMessage());
        }
    }

    public List<SupportPoint> getSupportPoints() {
        return supportPoints;
    }
}
