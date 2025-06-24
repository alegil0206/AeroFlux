package com.brianzolilecchesi.simulator.service;

import com.brianzolilecchesi.simulator.dto.ServiceEndpointDTO;
import com.brianzolilecchesi.simulator.service.api.AbstractMicroservice;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class MicroserviceRegistryService {
    private final Map<String, AbstractMicroservice> serviceHandlers;

    public MicroserviceRegistryService(List<AbstractMicroservice> handlers) {
        this.serviceHandlers = new HashMap<>();
        handlers.forEach(handler -> serviceHandlers.put(handler.getServiceName(), handler));
    }

    public List<ServiceEndpointDTO> getServices() {
        List<ServiceEndpointDTO> dtoList = new ArrayList<>();
        for (String serviceName : serviceHandlers.keySet()) {
            dtoList.add(new ServiceEndpointDTO(serviceName, serviceHandlers.get(serviceName).getServiceUrl()));
        }
        return dtoList;
    }

    public Map<String, AbstractMicroservice> getServiceHandlers() {
        return serviceHandlers;
    }

    public Map<String, String> getServiceUrls() {
        Map<String, String> urls = new HashMap<>();
        for (Map.Entry<String, AbstractMicroservice> entry : serviceHandlers.entrySet()) {
            urls.put(entry.getKey(), entry.getValue().getServiceUrl());
        }
        return urls;
    }

    public void updateServiceUrl(String serviceName, String newUrl) {
        AbstractMicroservice handler = serviceHandlers.get(serviceName);
        if (handler != null) {
            handler.updateUrl(newUrl);
        }
    }
}
