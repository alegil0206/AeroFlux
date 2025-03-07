package com.brianzolilecchesi.simulator.dto;

public class ServiceEndpointDTO {
    private String name;
    private String url;

    public ServiceEndpointDTO() {}

    public ServiceEndpointDTO(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() { return name; }
    public String getUrl() { return url; }
}
