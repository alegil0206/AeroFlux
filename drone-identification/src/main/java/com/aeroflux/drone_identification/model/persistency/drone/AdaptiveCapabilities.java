package com.aeroflux.drone_identification.model.persistency.drone;
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

    @Field("battery_management")
    private Boolean batteryManagement;

    public AdaptiveCapabilities() {}

    public AdaptiveCapabilities(Boolean safeLanding, Boolean collisionAvoidance, Boolean geoAwareness, Boolean autoAuthorization, Boolean batteryManagement) {
        this.safeLanding = safeLanding;
        this.collisionAvoidance = collisionAvoidance;
        this.geoAwareness = geoAwareness;
        this.autoAuthorization = autoAuthorization;
        this.batteryManagement = batteryManagement;
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

    @Override
    public String toString() {
        return "AdaptiveCapabilities{" +
                "safeLanding=" + safeLanding +
                ", collisionAvoidance=" + collisionAvoidance +
                ", geoAwareness=" + geoAwareness +
                ", autoAuthorization=" + autoAuthorization +
                ", batteryManagement=" + batteryManagement +
                '}';
    }
}
