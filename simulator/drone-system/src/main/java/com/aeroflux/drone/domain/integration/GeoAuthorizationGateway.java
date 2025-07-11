package com.aeroflux.drone.domain.integration;
import com.aeroflux.drone.domain.dto.AuthorizationRequestDTO;
import com.aeroflux.drone.domain.dto.AuthorizationDTO;
import com.aeroflux.drone.domain.exception.AuthorizationException;
import com.aeroflux.drone.domain.exception.ExternalServiceException;

import java.util.List;

public interface GeoAuthorizationGateway {
    AuthorizationDTO requestAuthorization(AuthorizationRequestDTO authorizationDTO) throws AuthorizationException, ExternalServiceException; ;
    List<AuthorizationDTO> getAuthorizations(String droneId) throws AuthorizationException, ExternalServiceException;
}