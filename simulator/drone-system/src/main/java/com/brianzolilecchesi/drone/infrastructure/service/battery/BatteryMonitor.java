package com.brianzolilecchesi.drone.infrastructure.service.battery;

import com.brianzolilecchesi.drone.domain.service.battery.BatteryService;
import com.brianzolilecchesi.drone.domain.component.Battery;

public class BatteryMonitor implements BatteryService {
    
    private Battery battery;

    public BatteryMonitor(Battery battery) {
        this.battery = battery;
    }

    @Override
    public double getBatteryLevel() {
        return battery.getBatteryLevel();
    }

    @Override
    public boolean isBatteryCritical() {
        return battery.getBatteryLevel() < 10;
    } 
}
