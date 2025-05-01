package com.brianzolilecchesi.drone.domain.integration;

import com.brianzolilecchesi.drone.domain.dto.GeoZoneDTO;
import com.brianzolilecchesi.drone.domain.dto.SupportPointDTO;
import com.brianzolilecchesi.drone.domain.exception.ExternalServiceException;

import java.util.List;

public interface GeoAwarenessGateway {
        List<GeoZoneDTO> getGeoZones() throws ExternalServiceException;
        List<SupportPointDTO> getSupportPoints() throws ExternalServiceException;
}
