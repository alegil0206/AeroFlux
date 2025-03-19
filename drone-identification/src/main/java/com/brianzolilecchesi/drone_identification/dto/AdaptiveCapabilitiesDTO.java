package com.brianzolilecchesi.drone_identification.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AdaptiveCapabilitiesDTO {

    private Boolean safeLanding;
    private Boolean collisionAvoidance;
    private Boolean geoAwareness;
    private Boolean autoAuthorization;
    private Boolean flightAutonomyManagement;

    @JsonCreator
    public AdaptiveCapabilitiesDTO(
            @JsonProperty("safe_landing") Boolean safeLanding,
            @JsonProperty("collision_avoidance") Boolean collisionAvoidance,
            @JsonProperty("geo_awareness") Boolean geoAwareness,
            @JsonProperty("auto_authorization") Boolean autoAuthorization,
            @JsonProperty("flight_autonomy_management") Boolean flightAutonomyManagement
            ) {
        
        setSafeLanding(safeLanding);
        setCollisionAvoidance(collisionAvoidance);
        setGeoAwareness(geoAwareness);
        setAutoAuthorization(autoAuthorization);
        setFlightAutonomyManagement(flightAutonomyManagement);
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

    public Boolean getFlightAutonomyManagement() {
        return flightAutonomyManagement;
    }

    public void setFlightAutonomyManagement(Boolean flightAutonomyManagement) {
        this.flightAutonomyManagement = flightAutonomyManagement;
    }

    public String toString() {
        return String.format(
                "AdaptiveCapabilitiesDTO[safe_landing=%b, collisionAvoidance=%b, geoAwareness=%b, autoAuthorization=%b, flightAutonomyManagement=%b]",
                safeLanding, 
                collisionAvoidance, 
                geoAwareness,
                autoAuthorization,
                flightAutonomyManagement
                );
    }

    public boolean equals(AdaptiveCapabilitiesDTO obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AdaptiveCapabilitiesDTO other = (AdaptiveCapabilitiesDTO) obj;
        return 
                safeLanding == other.getSafeLanding() &&
                collisionAvoidance == other.getCollisionAvoidance() &&
                geoAwareness == other.getGeoAwareness() &&
                autoAuthorization == other.getAutoAuthorization() &&
                flightAutonomyManagement == other.getFlightAutonomyManagement();
    }
}
