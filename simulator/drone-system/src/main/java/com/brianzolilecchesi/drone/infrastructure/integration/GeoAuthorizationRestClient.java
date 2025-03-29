package com.brianzolilecchesi.drone.infrastructure.integration;

import com.brianzolilecchesi.drone.domain.integration.GeoAuthorizationService;
import com.brianzolilecchesi.drone.domain.dto.AuthorizationDTO;

import java.util.ArrayList;
import java.util.List;


public class GeoAuthorizationRestClient implements GeoAuthorizationService {
    @Override
    public AuthorizationDTO requestAuthorization(AuthorizationDTO authorizationDTO) {
        // Implementazione per recuperare le autorizzazioni geografiche
        System.out.println("Chiamata all'API di geo-authorization");
        return new AuthorizationDTO(/* dati simulati */);
    }

    @Override
    public List<AuthorizationDTO> getAuthorizations(String droneId) {
        // Implementazione per recuperare le autorizzazioni geografiche
        System.out.println("Chiamata all'API di geo-authorization");
        return new ArrayList<>();
    }
}
