package com.brianzolilecchesi.drone.domain.model;

public class LogConstants {

    public static class Service {
        public static final String BATTERY_SERVICE = "BatteryService";
        public static final String NAVIGATION_SERVICE = "NavigationService";
        public static final String COMMUNICATION_SERVICE = "CommunicationService";
        public static final String LANDING_SERVICE = "LandingService";
        public static final String FLIGHT_CONTROLLER = "FlightController";
        public static final String DRONE_SYSTEM = "DroneSystem";
    }

    public static class Event {
        public static final String BATTERY_CHECK = "BATTERY_CHECK";
        public static final String FOLLOW_FLIGHT_PLAN = "FOLLOW_FLIGHT_PLAN";
        public static final String POWER_ON = "POWER_ON";
        public static final String POWER_OFF = "POWER_OFF";
        public static final String DESTINATION_REACHED = "DESTINATION_REACHED";
        public static final String MESSAGE_SENT = "MESSAGE_SENT";
        public static final String MOVE = "MOVE";
        public static final String LAND = "LAND";
    }

    public static class Level {
        public static final String INFO = "INFO";
        public static final String DEBUG = "DEBUG";
        public static final String ERROR = "ERROR";
    }
}
