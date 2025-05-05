package com.brianzolilecchesi.drone.domain.component;

import com.brianzolilecchesi.drone.domain.model.Position;

public interface Motor {

    boolean isMotorOn();

    void start();
    void stop();
    void move(Position position);
    void hover();
}
