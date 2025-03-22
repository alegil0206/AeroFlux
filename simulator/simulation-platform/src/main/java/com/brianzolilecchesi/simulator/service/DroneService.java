package com.brianzolilecchesi.simulator.service;

import com.brianzolilecchesi.drone.DroneSystem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
class DroneService {
    private final List<DroneSystem> drones = new CopyOnWriteArrayList<>();
    private final LogService logService;

    public DroneService(LogService logService) {
        this.logService = logService;
    }

    public List<DroneSystem> createDrones() {
        drones.clear();
        for (int i = 1; i <= 5; i++) {
            DroneSystem drone = new DroneSystem("Drone" + i);
            drone.addPropertyChangeListener(evt -> {
                if ("message".equals(evt.getPropertyName())) {
                    logService.sendLog("INFO", (String) evt.getNewValue());
                }
            });
            drones.add(drone);
        }
        return drones;
    }

    public void clearDrones() {
        drones.clear();
    }
}