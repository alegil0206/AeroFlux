package com.brianzolilecchesi.simulator.model;

import org.springframework.stereotype.Component;

@Component
public class SimulationStatus {
    private boolean running = false;
    private boolean paused = false;

    public synchronized boolean isRunning() {
        return running;
    }

    public synchronized void setRunning(boolean running) {
        this.running = running;
    }

    public synchronized boolean isPaused() {
        return paused;
    }

    public synchronized void setPaused(boolean paused) {
        this.paused = paused;
    }

    public synchronized String getStatus() {
        if (running && paused) return "PAUSED";
        if (running) return "RUNNING";
        return "STOPPED";
    }
}
