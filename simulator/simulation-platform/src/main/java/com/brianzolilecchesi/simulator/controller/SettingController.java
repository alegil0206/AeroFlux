package com.brianzolilecchesi.simulator.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.brianzolilecchesi.simulator.service.MicroserviceRegistryService;
import com.brianzolilecchesi.simulator.service.UserCoordinatesService;

import com.brianzolilecchesi.simulator.dto.ServiceEndpointDTO;
import com.brianzolilecchesi.simulator.dto.UserCoordinatesDTO;

@RestController
@RequestMapping(SettingController.SETTING_BASE_URL)
public class SettingController {
	
	public static final String SETTING_BASE_URL = "/setting";

    private final UserCoordinatesService coordinatesService;
    private final MicroserviceRegistryService microserviceRegistryService;

    public SettingController(UserCoordinatesService coordinatesService, MicroserviceRegistryService microserviceRegistryService) {
        this.coordinatesService = coordinatesService;
        this.microserviceRegistryService = microserviceRegistryService;
    }

    @GetMapping("/coordinates")
    public ResponseEntity<UserCoordinatesDTO> getCoordinates() {
        return ResponseEntity.ok(coordinatesService.getCoordinates());
    }

    @PutMapping("/coordinates")
    public ResponseEntity<Void> updateCoordinates(@RequestBody UserCoordinatesDTO newCoordinates) {
        coordinatesService.updateCoordinates(newCoordinates);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/service")
    public ResponseEntity<List<ServiceEndpointDTO>> getAllServices() {
        return ResponseEntity.ok(microserviceRegistryService.getServices());
    }

    @PutMapping("/service/{serviceName}")
    public ResponseEntity<Void> updateServiceUrl(@PathVariable("serviceName") String serviceName, @RequestParam("newUrl") String newUrl) {
        System.out.println("Updating service " + serviceName + " with new URL: " + newUrl);
        microserviceRegistryService.updateServiceUrl(serviceName, newUrl);
        return ResponseEntity.ok().build();
    }
}
