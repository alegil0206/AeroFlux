package com.brianzolilecchesi.simulator.service;

import com.brianzolilecchesi.drone.DroneSystem;
import com.brianzolilecchesi.simulator.dto.DroneDTO;
import com.brianzolilecchesi.simulator.service.api.DroneIdentificationService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
class DroneService {
    private final List<DroneSystem> drones = new CopyOnWriteArrayList<>();
    private final LogService logService;
    private final MicroserviceRegistryService microserviceRegistryService;


    public DroneService(LogService logService, MicroserviceRegistryService microserviceRegistryService) {
        this.logService = logService;
        this.microserviceRegistryService = microserviceRegistryService;
    }

    public List<DroneDTO> getDronesFromIdentificationService() {
        DroneIdentificationService droneIdentificationService = 
            (DroneIdentificationService) microserviceRegistryService.getServiceHandlers().get("drone_identification");

        if (droneIdentificationService != null) {
            return droneIdentificationService.getAllDrones();
        } else {
            throw new RuntimeException("Drone Identification Service not found");
        }
    }

    public List<DroneSystem> createDrones() {
        drones.clear();
        List<DroneDTO> droneDTOs = getDronesFromIdentificationService();
        for (DroneDTO droneDTO : droneDTOs) {
            DroneSystem drone = new DroneSystem(droneDTO.getId());
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