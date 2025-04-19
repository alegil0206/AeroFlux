package com.brianzolilecchesi.drone.domain.integration;
import com.brianzolilecchesi.drone.domain.dto.AuthorizationRequestDTO;
import com.brianzolilecchesi.drone.domain.dto.AuthorizationResponseDTO;
import com.brianzolilecchesi.drone.domain.exception.AuthorizationException;

import java.util.List;

public interface GeoAuthorizationGateway {
    AuthorizationResponseDTO requestAuthorization(AuthorizationRequestDTO authorizationDTO) throws AuthorizationException ;
    List<AuthorizationResponseDTO> getAuthorizations(String droneId);
}