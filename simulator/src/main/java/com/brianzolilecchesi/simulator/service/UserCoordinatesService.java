package com.brianzolilecchesi.simulator.service;

import com.brianzolilecchesi.simulator.model.UserCoordinates;
import com.brianzolilecchesi.simulator.dto.UserCoordinatesDTO;
import org.springframework.stereotype.Service;

@Service
public class UserCoordinatesService {

    private UserCoordinates userCoordinates;

    public UserCoordinatesService() {
        this.userCoordinates = new UserCoordinates(45.476592, 9.219752);
    }

    public UserCoordinatesDTO getCoordinates() {
        return new UserCoordinatesDTO(userCoordinates.getCenterLat(), userCoordinates.getCenterLon());
    }

    public void updateCoordinates(UserCoordinatesDTO newCoordinates) {
        userCoordinates.setCenterLat(newCoordinates.getLatitude());
        userCoordinates.setCenterLon(newCoordinates.getLongitude());
    }
}
