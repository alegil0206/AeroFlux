package com.brianzolilecchesi.simulator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MicroserviceRegistryService {

    private final Map<String, String> serviceEndpoints = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File storageFile = new File("/data/registry.json");

    @PostConstruct
    public void loadFromDisk() {
        if (storageFile.exists()) {
            try {
                Map<String, String> loaded = objectMapper.readValue(
                        storageFile,
                        new com.fasterxml.jackson.core.type.TypeReference<Map<String, String>>() {}
                );
                serviceEndpoints.putAll(loaded);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            setDefaultService();
            saveToDisk();
        }
    }

    private void setDefaultService() {
        serviceEndpoints.put("DRONE_IDENTIFICATION", "http://api.uspace.local/drone-identification");
        serviceEndpoints.put("GEO_AUTHORIZATION", "http://api.uspace.local/geo-authorization");
        serviceEndpoints.put("GEO_AWARENESS", "http://api.uspace.local/geo-awareness");
        serviceEndpoints.put("WEATHER", "http://api.uspace.local/weather");
    }

    public Map<String, String> getServiceUrls() {
        return new HashMap<>(serviceEndpoints);
    }

    public synchronized void updateServiceUrl(String serviceName, String newUrl) {
        serviceEndpoints.put(serviceName, newUrl);
        saveToDisk();
    }

    private void saveToDisk() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(storageFile, serviceEndpoints);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
