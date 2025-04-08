package com.brianzolilecchesi.simulator.model.drone;

import com.brianzolilecchesi.drone.domain.component.Camera;
import com.brianzolilecchesi.drone.domain.model.Coordinate;

public class SimulatedCamera implements Camera {

    @Override
    public String takePicture(Coordinate coordinates) {
        System.out.println("Taking picture...");
        return "Picture taken";
    }
}
