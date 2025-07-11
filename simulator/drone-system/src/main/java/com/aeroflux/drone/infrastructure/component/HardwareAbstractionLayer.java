package com.aeroflux.drone.infrastructure.component;

import com.aeroflux.drone.domain.component.Altimeter;
import com.aeroflux.drone.domain.component.Battery;
import com.aeroflux.drone.domain.component.GPS;
import com.aeroflux.drone.domain.component.Motor;
import com.aeroflux.drone.domain.component.Radio;

public class HardwareAbstractionLayer {
    
    private final Battery battery;
    private final Radio radio;
    private final GPS gps;
    private final Altimeter altimeter;
    private final Motor motor;

    public HardwareAbstractionLayer(
            Battery battery,
            Radio radio,
            GPS gps,
            Altimeter altimeter,
            Motor motor) {
        this.battery = battery;
        this.radio = radio;
        this.gps = gps;
        this.altimeter = altimeter;
        this.motor = motor;
    }

    public Battery getBattery() {
        return battery;
    }

    public Radio getRadio() {
        return radio;
    }

    public GPS getGps() {
        return gps;
    }

    public Altimeter getAltimeter() {
        return altimeter;
    }

    public Motor getMotor() {
        return motor;
    }
}
