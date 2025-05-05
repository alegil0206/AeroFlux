package com.brianzolilecchesi.drone.domain.handler;

import com.brianzolilecchesi.drone.domain.model.DroneContext;

public interface StepHandler {
    boolean handle(DroneContext context);
}
