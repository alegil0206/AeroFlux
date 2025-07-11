package com.aeroflux.simulator.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.aeroflux.simulator.service.MicroserviceRegistryService;

@RestController
@RequestMapping(SettingController.SETTING_BASE_URL)
public class SettingController {
	
	public static final String SETTING_BASE_URL = "/setting";

    private final MicroserviceRegistryService microserviceRegistryService;

    public SettingController(MicroserviceRegistryService microserviceRegistryService) {
        this.microserviceRegistryService = microserviceRegistryService;
    }

    @GetMapping("/service")
    public ResponseEntity<Map<String, String>> getAllServices() {
        return ResponseEntity.ok(microserviceRegistryService.getServiceUrls());
    }

    @PutMapping("/service/{serviceName}")
    public ResponseEntity<Void> updateServiceUrl(@PathVariable("serviceName") String serviceName, @RequestParam("newUrl") String newUrl) {
        System.out.println("Updating service " + serviceName + " with new URL: " + newUrl);
        microserviceRegistryService.updateServiceUrl(serviceName, newUrl);
        return ResponseEntity.ok().build();
    }
}
