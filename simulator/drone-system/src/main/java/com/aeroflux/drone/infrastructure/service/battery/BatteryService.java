package com.aeroflux.drone.infrastructure.service.battery;

import com.aeroflux.drone.domain.component.Battery;
import com.aeroflux.drone.domain.model.LogConstants;
import com.aeroflux.drone.infrastructure.service.log.LogService;

public class BatteryService {

    private static final double CRITICAL_BATTERY_LEVEL = 1000.0;
    private static final double BATTERY_CONSUMPTION_RATE = 100.0;

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
        return battery.getBatteryLevel() <= CRITICAL_BATTERY_LEVEL;
    }

    public boolean isBatteryEnoughForFlight(double distance) {
        double requiredBatteryLevel = distance * BATTERY_CONSUMPTION_RATE;
        logService.info(
            LogConstants.Component.BATTERY_SERVICE,
            LogConstants.Event.BATTERY_NEED_PREDICTION,
            "Required battery level for distance " + distance + " is " + requiredBatteryLevel + " mAh. Actual battery level is " + battery.getBatteryLevel() + " mAh." );
        return battery.getBatteryLevel() >= requiredBatteryLevel;
    }
}