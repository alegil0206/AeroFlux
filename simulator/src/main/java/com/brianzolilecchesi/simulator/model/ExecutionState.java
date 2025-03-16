package com.brianzolilecchesi.simulator.model;

public class ExecutionState {
    private int cycleCount;
    private boolean running;
    private boolean paused;

    public ExecutionState() {
        this.cycleCount = 0;
        this.running = false;
        this.paused = false;
    }

    public int getCycleCount() {
        return cycleCount;
    }

    public void incrementCycle() {
        this.cycleCount++;
    }

    public void resetCycle() {
        this.cycleCount = 0;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public String getStatus() {
        if (running) {
            return paused ? "Paused" : "Running";
        } 
        return "Stopped";
    }
    
}
