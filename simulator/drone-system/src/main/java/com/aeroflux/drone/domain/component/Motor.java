package com.aeroflux.drone.domain.component;

import com.aeroflux.drone.domain.model.Position;

public interface Motor {

    boolean isMotorOn();

    void start();
    void stop();
    void move(Position position);
    void hover();
}
