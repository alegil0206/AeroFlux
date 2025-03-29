package com.brianzolilecchesi.drone.domain.component;

public interface Motor {

    boolean isMotorOn();

    void start();
    void stop();
    void move(double distance, double bearing, double altitude);
}
