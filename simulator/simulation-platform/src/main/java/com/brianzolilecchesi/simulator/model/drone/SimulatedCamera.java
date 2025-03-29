package com.brianzolilecchesi.simulator.model.drone;

import com.brianzolilecchesi.drone.domain.component.Camera;
import com.brianzolilecchesi.drone.domain.model.Coordinates;

public class SimulatedCamera implements Camera {

    @Override
    public String takePicture(Coordinates coordinates) {
        System.out.println("Taking picture...");
        return "Picture taken";
    }
}
