package com.brianzolilecchesi.drone.domain.integration;

import com.brianzolilecchesi.drone.domain.dto.AuthorizationDTO;
import java.util.List;

public interface GeoAuthorizationService {
    AuthorizationDTO requestAuthorization(AuthorizationDTO authorizationDTO);
    List<AuthorizationDTO> getAuthorizations(String droneId);
}