package com.brianzolilecchesi.drone_identification.model.persistency.drone;
import org.springframework.data.mongodb.core.mapping.Field;

public class AdaptiveCapabilities {

    @Field("safe_landing")
    private Boolean safeLanding;

    @Field("collision_avoidance")
    private Boolean collisionAvoidance;

    @Field("geo_awareness")
    private Boolean geoAwareness;

    @Field("auto_authorization")
    private Boolean autoAuthorization;

    @Field("flight_autonomy_management")
    private Boolean flightAutonomyManagement;

    public AdaptiveCapabilities() {}

    public AdaptiveCapabilities(Boolean safeLanding, Boolean collisionAvoidance, Boolean geoAwareness, Boolean autoAuthorization, Boolean flightAutonomyManagement) {
        this.safeLanding = safeLanding;
        this.collisionAvoidance = collisionAvoidance;
        this.geoAwareness = geoAwareness;
        this.autoAuthorization = autoAuthorization;
        this.flightAutonomyManagement = flightAutonomyManagement;
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

    @Override
    public String toString() {
        return "AdaptiveCapabilities{" +
                "safeLanding=" + safeLanding +
                ", collisionAvoidance=" + collisionAvoidance +
                ", geoAwareness=" + geoAwareness +
                ", autoAuthorization=" + autoAuthorization +
                ", flightAutonomyManagement=" + flightAutonomyManagement +
                '}';
    }
}
