package com.aeroflux.drone.domain.model;

public class LogConstants {

    public static class Component {
        public static final String BATTERY_SERVICE = "BatteryService";
        public static final String NAVIGATION_SERVICE = "NavigationService";
        public static final String COMMUNICATION_SERVICE = "CommunicationService";
        public static final String LANDING_SERVICE = "LandingService";
        public static final String FLIGHT_CONTROLLER = "FlightController";
        public static final String GEOZONE_SERVICE = "GeoZoneService";
        public static final String WEATHER_SERVICE = "WeatherService";
        public static final String AUTHORIZATION_SERVICE = "AuthorizationService";
        public static final String SUPPORT_POINT_SERVICE = "SupportPointService";

        public static final String BATTERY_HANDLER = "BatteryHandler";
        public static final String FLIGHT_PLANNING_HANDLER = "FlightPlanningHandler";
        public static final String COLLISION_AVOIDANCE_HANDLER = "CollisionAvoidanceHandler";
        public static final String CONFLICT_AVOIDANCE_HANDLER = "ConflictAvoidanceHandler";
        public static final String DATA_ACQUISITION_HANDLER = "DataAcquisitionHandler";
        public static final String FLIGHT_CONTROL_HANDLER = "FlightControlHandler";
        public static final String GEOLOCATION_HANDLER = "GeolocationHandler";
        public static final String DRONE_SYSTEM = "DroneSystem";
    }

    public static class Event {
        public static final String BATTERY_CHECK = "BATTERY_CHECK";
        public static final String BATTERY_CRITICAL = "BATTERY_CRITICAL";
        public static final String BATTERY_NEED_PREDICTION = "BATTERY_NEED_PREDICTION";
        public static final String INSUFFICIENT_BATTERY = "INSUFFICIENT_BATTERY";
        public static final String FOLLOW_FLIGHT_PLAN = "FOLLOW_FLIGHT_PLAN";
        public static final String POWER_ON = "POWER_ON";
        public static final String POWER_OFF = "POWER_OFF";
        public static final String DESTINATION_REACHED = "DESTINATION_REACHED";
        public static final String MESSAGE_SENT = "MESSAGE_SENT";
        public static final String MESSAGE_RECEIVED = "MESSAGE_RECEIVED";

        public static final String GENERATING_FLIGHT_PLAN = "GENERATING_FLIGHT_PLAN";
        public static final String FLIGHT_PLAN_GENERATED = "FLIGHT_PLAN_GENERATED";
        public static final String FLIGHT_PLAN_FAILED = "FLIGHT_PLAN_FAILED";
        public static final String OPTIMIZING_FLIGHT_PLAN = "OPTIMIZING_FLIGHT_PLAN";
        public static final String FLIGHT_PLAN_CANCELLED = "FLIGHT_PLAN_CANCELLED";
        public static final String ADAPT_FLIGHT_PLAN = "ADAPT_FLIGHT_PLAN";
        public static final String DESTINATION_UNREACHABLE = "DESTINATION_UNREACHABLE";
        public static final String REROUTE_FLIGHT = "REROUTE_FLIGHT";


        public static final String FETCHING = "FETCHING";
        public static final String FETCHED = "FETCHED";
        public static final String FETCH_FAILED = "FETCH_FAILED";
        public static final String HOVERING = "HOVERING";
        public static final String WAITING = "WAITING";
        public static final String MOVING = "MOVING";
        public static final String EMERGENCY_LANDING = "EMERGENCY_LANDING";
        public static final String LANDING = "LANDING";
        public static final String SHUTDOWN = "SHUTDOWN";
        public static final String LANDED = "LANDED";

        public static final String COLLISION_AVOIDANCE = "COLLISION_AVOIDANCE";
        public static final String CONFLICT_AVOIDANCE = "CONFLICT_AVOIDANCE";

        public static final String REQUESTING_AUTHORIZATION = "REQUESTING_AUTHORIZATION";
        public static final String AUTHORIZATION_GRANTED = "AUTHORIZATION_GRANTED";
        public static final String AUTHORIZATION_DENIED = "AUTHORIZATION_DENIED";

        public static final String GEOZONE_ENTERED = "GEOZONE_ENTERED";
        public static final String RAIN_CELL_ENTERED = "RAIN_CELL_ENTERED";
        public static final String UNSAFE_FLIGHT = "UNSAFE_FLIGHT";
    }

    public static class Level {
        public static final String INFO = "INFO";
        public static final String DEBUG = "DEBUG";
        public static final String ERROR = "ERROR";
    }
}
