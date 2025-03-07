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

import com.brianzolilecchesi.simulator.service.ServiceEndpointsService;
import com.brianzolilecchesi.simulator.service.UserCoordinatesService;

import com.brianzolilecchesi.simulator.dto.ServiceEndpointDTO;
import com.brianzolilecchesi.simulator.dto.UserCoordinatesDTO;

@RestController
@RequestMapping(SettingController.SETTING_BASE_URL)
public class SettingController {
	
	public static final String SETTING_BASE_URL = "/setting";

    private final UserCoordinatesService coordinatesService;
    private final ServiceEndpointsService serviceEndpointsService;

    public SettingController(UserCoordinatesService coordinatesService, ServiceEndpointsService serviceEndpointsService) {
        this.coordinatesService = coordinatesService;
        this.serviceEndpointsService = serviceEndpointsService;
    }

    @GetMapping("/coordinates")
    public ResponseEntity<UserCoordinatesDTO> getCoordinates() {
        return ResponseEntity.ok(coordinatesService.getCoordinates());
    }

    @PutMapping("/coordinates")
    public ResponseEntity<Void> updateCoordinates(@RequestBody UserCoordinatesDTO newCoordinates) {
        coordinatesService.updateCoordinates(newCoordinates);
        serviceEndpointsService.notifyServices(newCoordinates);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/service")
    public ResponseEntity<List<ServiceEndpointDTO>> getAllServices() {
        return ResponseEntity.ok(serviceEndpointsService.getAllServices());
    }

    @PutMapping("/service/{serviceName}")
    public ResponseEntity<Void> updateServiceUrl(@PathVariable String serviceName, @RequestParam String newUrl) {
        serviceEndpointsService.updateServiceUrl(serviceName, newUrl);
        return ResponseEntity.ok().build();
    }
}
