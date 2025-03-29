package com.brianzolilecchesi.drone.domain.model;

public class LandingZone {
    private boolean safe;
    
    public LandingZone(boolean safe) {
        this.safe = safe;
    }
    
    public boolean isSafe() {
        return safe;
    }
    
    @Override
    public String toString() {
        return safe ? "Safe Landing Zone" : "Unsafe Landing Zone";
    }
}
