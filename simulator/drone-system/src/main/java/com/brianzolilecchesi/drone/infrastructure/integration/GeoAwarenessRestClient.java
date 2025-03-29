package com.brianzolilecchesi.drone.infrastructure.integration;

import com.brianzolilecchesi.drone.domain.integration.GeoAwarenessService;
import com.brianzolilecchesi.drone.domain.dto.GeoZoneDTO;

import java.util.ArrayList;
import java.util.List;


public class GeoAwarenessRestClient implements GeoAwarenessService {

    @Override
    public List<GeoZoneDTO> getGeoZones() {
        // Implementazione per recuperare le geoZone
        System.out.println("Chiamata all'API di geo-awareness");
        return new ArrayList<>();
    }
    
}
