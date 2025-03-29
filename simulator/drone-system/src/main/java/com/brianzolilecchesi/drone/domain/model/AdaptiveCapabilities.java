package com.brianzolilecchesi.drone.domain.model;

public class AdaptiveCapabilities {

    private Boolean safeLanding;
    private Boolean collisionAvoidance;
    private Boolean geoAwareness;
    private Boolean autoAuthorization;
    private Boolean batteryManagement;

    public AdaptiveCapabilities() {
    }

    public AdaptiveCapabilities(
            Boolean safeLanding,
            Boolean collisionAvoidance,
            Boolean geoAwareness,
            Boolean autoAuthorization,
            Boolean batteryManagement
            ) {
        setSafeLanding(safeLanding);
        setCollisionAvoidance(collisionAvoidance);
        setGeoAwareness(geoAwareness);
        setAutoAuthorization(autoAuthorization);
        setBatteryManagement(batteryManagement);
    }
    
    public Boolean getSafeLanding() {
        return safeLanding;
    }

    public void setSafeLanding(Boolean safeLanding) {
        this.safeLanding = safeLanding;
    }

    public Boolean getCollisionAvoidance() {
        return collisionAvoidance;
    }

    public void setCollisionAvoidance(Boolean collisionAvoidance) {
        this.collisionAvoidance = collisionAvoidance;
    }

    public Boolean getGeoAwareness() {
        return geoAwareness;
    }

    public void setGeoAwareness(Boolean geoAwareness) {
        this.geoAwareness = geoAwareness;
    }

    public Boolean getAutoAuthorization() {
        return autoAuthorization;
    }

    public void setAutoAuthorization(Boolean autoAuthorization) {
        this.autoAuthorization = autoAuthorization;
    }

    public Boolean getBatteryManagement() {
        return batteryManagement;
    }

    public void setBatteryManagement(Boolean batteryManagement) {
        this.batteryManagement = batteryManagement;
    }

    public String toString() {
        return String.format(
                "AdaptiveCapabilitiesDTO[safe_landing=%b, collisionAvoidance=%b, geoAwareness=%b, autoAuthorization=%b, batteryManagement=%b]",
                safeLanding, 
                collisionAvoidance, 
                geoAwareness,
                autoAuthorization,
                batteryManagement
                );
    }

    public boolean equals(AdaptiveCapabilities obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AdaptiveCapabilities other = (AdaptiveCapabilities) obj;
        return 
                safeLanding == other.getSafeLanding() &&
                collisionAvoidance == other.getCollisionAvoidance() &&
                geoAwareness == other.getGeoAwareness() &&
                autoAuthorization == other.getAutoAuthorization() &&
                batteryManagement == other.getBatteryManagement();
    }
}
