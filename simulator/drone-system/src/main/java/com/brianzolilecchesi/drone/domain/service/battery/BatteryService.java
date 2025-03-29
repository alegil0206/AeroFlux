package com.brianzolilecchesi.drone.domain.service.battery;

public interface BatteryService {
    double getBatteryLevel();
    boolean isBatteryCritical();
}
