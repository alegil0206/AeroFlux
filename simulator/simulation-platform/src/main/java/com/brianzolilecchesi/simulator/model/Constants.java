package com.brianzolilecchesi.simulator.model;

import com.brianzolilecchesi.drone.domain.model.LogConstants;

public class Constants extends LogConstants {

    public static class System {
        public static final String SIMULATOR = "Simulator";
    }

    public static class Service extends LogConstants.Service {
        public static final String SIMULATOR_SERVICE = "SimulatorService";
    }

    public static class Event extends LogConstants.Event {
        public static final String SIMULATION_START = "SIMULATION_START";
        public static final String SIMULATION_STOP = "SIMULATION_STOP";
        public static final String SIMULATION_PAUSE = "SIMULATION_PAUSE";
        public static final String SIMULATION_RESUME = "SIMULATION_RESUME";
        public static final String SIMULATION_COMPLETED = "SIMULATION_COMPLETED";
    }

    public static class Level extends LogConstants.Level {
        public static final String TRACE = "TRACE";
    }
}
