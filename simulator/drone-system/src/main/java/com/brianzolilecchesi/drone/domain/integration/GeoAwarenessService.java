package com.brianzolilecchesi.drone.domain.integration;

import com.brianzolilecchesi.drone.domain.dto.GeoZoneDTO;
import java.util.List;

public interface GeoAwarenessService {
        List<GeoZoneDTO> getGeoZones();
}
