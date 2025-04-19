package com.brianzolilecchesi.drone.infrastructure.service.geozone;
import com.brianzolilecchesi.drone.domain.model.GeoZone;

import java.util.ArrayList;
import java.util.List;
import com.brianzolilecchesi.drone.domain.dto.GeoZoneDTO;
import com.brianzolilecchesi.drone.domain.integration.GeoAwarenessGateway;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

public class GeoZoneService {

    private final GeoAwarenessGateway restApiGateway;
    private List<GeoZone> geoZones;

    public GeoZoneService(GeoAwarenessGateway restApiGateway) {
        this.restApiGateway = restApiGateway;
        this.geoZones = new ArrayList<>();
    }

    public void fetchGeoZones() {
        try {
            List<GeoZoneDTO> geoZoneDTOs = restApiGateway.getGeoZones();
            geoZones = geoZoneDTOs.stream()
                    .map(GeoZone::new)
                    .toList();
        } catch (HttpClientErrorException.NotFound e) {
            System.err.println("Error 404: GeoZones not found - " + e.getMessage());
        } catch (RestClientException e) {
            System.err.println("Error communicating with geo-awareness service: " + e.getMessage());
        }
    }

    public List<GeoZone> getGeoZones() {
        return geoZones;
    }
}

