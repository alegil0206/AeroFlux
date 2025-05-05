package com.brianzolilecchesi.drone.domain.service.navigation;

public interface PrecedenceService {

    public boolean shouldYield(String droneId, String otherDroneId);

}
