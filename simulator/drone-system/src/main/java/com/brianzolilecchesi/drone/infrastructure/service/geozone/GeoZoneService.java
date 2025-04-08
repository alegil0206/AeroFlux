package com.brianzolilecchesi.drone.infrastructure.service.geozone;
import com.brianzolilecchesi.drone.domain.model.GeoZone;
import com.brianzolilecchesi.drone.infrastructure.integration.RestApiGateway;

import java.util.Collections;
import java.util.List;
import com.brianzolilecchesi.drone.domain.dto.GeoZoneDTO;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

public class GeoZoneService {

    private final RestApiGateway restApiGateway;


    public GeoZoneService(RestApiGateway restApiGateway) {
        this.restApiGateway = restApiGateway;
    }

    public List<GeoZone> getGeoZones() {
        try {
            List<GeoZoneDTO> geoZoneDTOs = restApiGateway.getGeoZones();
            return geoZoneDTOs.stream()
                    .map(GeoZone::new)
                    .toList();
        } catch (HttpClientErrorException.NotFound e) {
            System.err.println("Error 404: GeoZones not found - " + e.getMessage());
            return Collections.emptyList();
        } catch (RestClientException e) {
            System.err.println("Error communicating with geo-awareness service: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}

