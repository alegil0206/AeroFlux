package com.aeroflux.simulator.helper;

import com.aeroflux.drone.DroneSystem;
import com.aeroflux.simulator.model.drone.SimulatedBattery;
import com.aeroflux.simulator.model.drone.SimulatedRadio;
import com.aeroflux.simulator.service.DroneIdentificationService;
import com.aeroflux.simulator.service.MicroserviceRegistryService;
import com.aeroflux.simulator.model.drone.SimulatedGPS;
import com.aeroflux.simulator.model.drone.SimulatedMotor;
import com.aeroflux.simulator.model.drone.SimulatedAltimeter;

import com.aeroflux.drone.domain.model.DroneProperties;
import com.aeroflux.drone.domain.model.Position;
import com.aeroflux.drone.infrastructure.component.HardwareAbstractionLayer;
import com.aeroflux.drone.domain.model.AdaptiveCapabilities;
import com.aeroflux.simulator.dto.DronePropertiesDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class DroneSystemFactory {

    private final DroneIdentificationService droneIdentificationService;
    private final MicroserviceRegistryService microservicesRegistryService;

    public DroneSystemFactory(DroneIdentificationService droneIdentificationService, MicroserviceRegistryService microservicesRegistryService) {
        this.droneIdentificationService = droneIdentificationService;
        this.microservicesRegistryService = microservicesRegistryService;
    }

    public List<DroneSystem> createDrones() {
        List<DronePropertiesDTO> droneDTOs = droneIdentificationService.getDrones();
        List<DroneSystem> drones = new ArrayList<>();
        
        for (DronePropertiesDTO droneDTO : droneDTOs) {
            SimulatedBattery battery = new SimulatedBattery(droneDTO.getBattery());
            SimulatedRadio radio = new SimulatedRadio();
            SimulatedGPS gps = new SimulatedGPS(droneDTO.getSource().getLatitude(), droneDTO.getSource().getLongitude());
            SimulatedAltimeter altimeter = new SimulatedAltimeter(0);
            SimulatedMotor motor = new SimulatedMotor(gps, altimeter);
            DroneProperties droneProperties = new DroneProperties(
                droneDTO.getId(),
                droneDTO.getName(),
                droneDTO.getModel(),
                droneDTO.getOwner(),
                droneDTO.getOperationCategory(),
                droneDTO.getPlanDefinitionTimestamp(),
                new AdaptiveCapabilities(
                    droneDTO.getAdaptiveCapabilities().getSafeLanding(),
                    droneDTO.getAdaptiveCapabilities().getCollisionAvoidance(),
                    droneDTO.getAdaptiveCapabilities().getGeoAwareness(),
                    droneDTO.getAdaptiveCapabilities().getAutoAuthorization(),
                    droneDTO.getAdaptiveCapabilities().getBatteryManagement()
                ),
                droneDTO.getBattery(),
                new Position(droneDTO.getSource()),
                new Position(droneDTO.getDestination())
            );
            HardwareAbstractionLayer hardwareAbstractionLayer = new HardwareAbstractionLayer(
                battery,
                radio,
                gps,
                altimeter,
                motor
            );

            Map<String, String> microservicesUrlsMap = microservicesRegistryService.getServiceUrls();

            DroneSystem drone = new DroneSystem(droneProperties, hardwareAbstractionLayer, microservicesUrlsMap);
            drone.powerOn();
            drones.add(drone);
        }

        for (DroneSystem drone : drones) {
            ( (SimulatedRadio)drone.getHardwareAbstractionLayer().getRadio() ).setSender(drone);
            ( (SimulatedRadio)drone.getHardwareAbstractionLayer().getRadio() ).setAvailableDrone(drones);
        }

        return drones;
    }
}