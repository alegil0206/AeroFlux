package com.brianzolilecchesi.simulator.model.drone;

import com.brianzolilecchesi.drone.domain.component.Battery;

public class SimulatedBattery implements Battery {
    private double batteryLevel;

    public SimulatedBattery(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @Override
    public double getBatteryLevel() {
        return batteryLevel;
    }

    public void drainBattery(double amount) {
        batteryLevel = Math.max(0, batteryLevel - amount);
    }
}
