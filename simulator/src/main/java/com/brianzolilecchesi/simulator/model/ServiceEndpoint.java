package com.brianzolilecchesi.simulator.model;

public class ServiceEndpoint {
    private String name;
    private String url;

    public ServiceEndpoint(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() { return name; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
