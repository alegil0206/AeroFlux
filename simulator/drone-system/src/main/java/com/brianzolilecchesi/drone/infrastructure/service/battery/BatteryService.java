package com.brianzolilecchesi.drone.infrastructure.service.battery;

import com.brianzolilecchesi.drone.domain.component.Battery;
import com.brianzolilecchesi.drone.infrastructure.service.log.LogService;

public class BatteryService {
    
    private Battery battery;
    private LogService logService;

    public BatteryService(Battery battery, LogService logService) {
        this.battery = battery;
        this.logService = logService;
    }

    public double getBatteryLevel() {
        return battery.getBatteryLevel();
    }

    public boolean isBatteryCritical() {
        return battery.getBatteryLevel() <= 300;
    } 
}