package com.brianzolilecchesi.simulator.service.api;

import com.brianzolilecchesi.simulator.dto.UserCoordinatesDTO;

public abstract class AbstractMicroservice {
    protected String serviceUrl;

    public AbstractMicroservice(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void updateUrl(String newUrl) {
        this.serviceUrl = newUrl;
    }

    public abstract String getServiceName();

    public void updateCoordinates(UserCoordinatesDTO coordinates) {
        throw new UnsupportedOperationException("updateCoordinates non supportato per questo servizio");
    }

    public void resetMicroservice(){
        throw new UnsupportedOperationException("resetMicroservice non supportato per questo servizio");
    }
}
