package com.brianzolilecchesi.drone.infrastructure.service.battery;

import com.brianzolilecchesi.drone.domain.service.battery.BatteryService;
import com.brianzolilecchesi.drone.domain.service.log.LogService;
import com.brianzolilecchesi.drone.domain.component.Battery;

public class BatteryMonitor implements BatteryService {
    
    private Battery battery;
    private LogService logService;

    public BatteryMonitor(Battery battery, LogService logService) {
        this.battery = battery;
        this.logService = logService;
    }

    @Override
    public double getBatteryLevel() {
        return battery.getBatteryLevel();
    }

    @Override
    public boolean isBatteryCritical() {
        return battery.getBatteryLevel() <= 300;
    } 
}