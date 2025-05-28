package com.brianzolilecchesi.drone.domain.integration;
import com.brianzolilecchesi.drone.domain.dto.AuthorizationRequestDTO;
import com.brianzolilecchesi.drone.domain.dto.AuthorizationDTO;
import com.brianzolilecchesi.drone.domain.exception.AuthorizationException;
import com.brianzolilecchesi.drone.domain.exception.ExternalServiceException;

import java.util.List;

public interface GeoAuthorizationGateway {
    AuthorizationDTO requestAuthorization(AuthorizationRequestDTO authorizationDTO) throws AuthorizationException, ExternalServiceException; ;
    List<AuthorizationDTO> getAuthorizations(String droneId) throws AuthorizationException, ExternalServiceException;
}