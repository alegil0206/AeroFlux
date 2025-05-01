package com.brianzolilecchesi.drone.domain.integration;
import com.brianzolilecchesi.drone.domain.dto.AuthorizationRequestDTO;
import com.brianzolilecchesi.drone.domain.dto.AuthorizationResponseDTO;
import com.brianzolilecchesi.drone.domain.exception.AuthorizationException;
import com.brianzolilecchesi.drone.domain.exception.ExternalServiceException;

import java.util.List;

public interface GeoAuthorizationGateway {
    AuthorizationResponseDTO requestAuthorization(AuthorizationRequestDTO authorizationDTO) throws AuthorizationException, ExternalServiceException; ;
    List<AuthorizationResponseDTO> getAuthorizations(String droneId) throws AuthorizationException, ExternalServiceException;
}