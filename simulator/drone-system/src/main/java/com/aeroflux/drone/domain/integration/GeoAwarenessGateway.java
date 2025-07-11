package com.aeroflux.drone.domain.integration;

import com.aeroflux.drone.domain.dto.GeoZoneDTO;
import com.aeroflux.drone.domain.dto.SupportPointDTO;
import com.aeroflux.drone.domain.exception.ExternalServiceException;

import java.util.List;

public interface GeoAwarenessGateway {
        List<GeoZoneDTO> getGeoZones() throws ExternalServiceException;
        List<SupportPointDTO> getSupportPoints() throws ExternalServiceException;
}
