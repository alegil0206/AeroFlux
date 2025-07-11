package com.aeroflux.drone.infrastructure.service.navigation;

import com.aeroflux.drone.domain.dto.FlightPlanDTO;
import com.aeroflux.drone.domain.model.DataStatus;
import com.aeroflux.drone.domain.model.GeoZone;
import com.aeroflux.drone.domain.model.LogConstants;
import com.aeroflux.drone.domain.model.NearbyDroneStatus;
import com.aeroflux.drone.domain.model.Position;
import com.aeroflux.drone.domain.model.RainCell;
import com.aeroflux.drone.domain.navigation.FlightPlanCalculatorFacade;
import com.aeroflux.drone.domain.navigation.flight_plan.model.FlightPlan;
import com.aeroflux.drone.domain.navigation.flight_plan.model.graph.FlightPlanRefiner;
import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.GridZone;
import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.Zone;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.ArrayList;

import com.aeroflux.drone.infrastructure.service.log.LogService;

public class NavigationService  {

	private static final double GRID_EXPANSION_METERS = 5000.0;
	
	public static final double INITIAL_CELL_WIDTH = 160.0; //320
	public static final List<Double> ALTITUDE_LEVELS = List.of(20.0, 40.0, 60.0, 80.0, 100.0, 120.0);
	
	public static final int AVERAGE_CALCULATION_STEPS = 20;

	public static final double STEP_SIZE = 20.0; // meters

	private final LogService logService;

	private double stepSize;
    private double initialCellWidth;
    private List<Double> altitudeLevels;
    
    private List<Position> waypoints;
    private int nextWaypointIndex;
	private int flightPlanVersion;
	private DataStatus flightPlanStatus = DataStatus.NOT_REQUESTED;
    
    public NavigationService(LogService logService) {
        this(logService, STEP_SIZE, INITIAL_CELL_WIDTH, ALTITUDE_LEVELS);
    }
    
    public NavigationService(
			LogService logService,
			double stepSize,
    		double initialCellWidth,
    		List<Double> altitudeLevels
    		) {
    	
        this(
				logService,
				stepSize,
        		initialCellWidth,
        		altitudeLevels,
        		new ArrayList<Zone>()
    		);
    }
    
    public NavigationService(
			LogService logService,
			double stepSize,
    		double initialCellWidth,
    		List<Double> altitudeLevels,
    		List<Zone> zones
    		) {
    	
		this.logService = logService;
        setStepSize(stepSize);
        setInitialCellWidth(initialCellWidth);
        setAltitudeLevels(altitudeLevels);
        
        this.waypoints = new ArrayList<>();
        this.nextWaypointIndex = 1;
		this.flightPlanVersion = 0;
	}
    
    public double getInitialCellWidth() {return initialCellWidth;}
	public void setInitialCellWidth(final double initialCellWidth) {this.initialCellWidth = initialCellWidth;}
	
	public List<Double> getAltitudeLevels() {return altitudeLevels;}
	public void setAltitudeLevels(final List<Double> altitudeLevels) {this.altitudeLevels = altitudeLevels;}

	public double getStepSize() {return stepSize;}
	public void setStepSize(final double stepSize) {
		if (stepSize <= 0) {
			throw new IllegalArgumentException("Step size must be greater than zero");
		}
		this.stepSize = stepSize;
	}

	public void generateFlightPlan(Position start, Position destination, List<GeoZone> geoZones, List<RainCell> rainCells, List<NearbyDroneStatus> nearbyDrones) {
		int initialFlightPlanVersion;
		int initialWaypointIndex;
		List<Position> initialWaypoints;
		
		synchronized (this) {
			initialWaypoints = new ArrayList<>(waypoints);
			initialFlightPlanVersion = flightPlanVersion;
			initialWaypointIndex = nextWaypointIndex - 1;
			flightPlanStatus = DataStatus.LOADING;
		}

		logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.GENERATING_FLIGHT_PLAN, "Generating flight plan: version " + (initialFlightPlanVersion + 1));
		FlightPlanCalculatorFacade calculatorService = new FlightPlanCalculatorFacade();
		calculatorService.getGeozoneService().add(geoZones);
		calculatorService.getWeatherService().add(rainCells);
		calculatorService.getDroneZoneService().add(nearbyDrones);

		CompletableFuture.supplyAsync(() -> calculateFlightPlan(start, destination, calculatorService))
		.thenApply(generatedFlightPlan -> {
			if (!generatedFlightPlan.hasPath()) {
				throw new IllegalStateException("Flight plan not found");
			}
			return generatedFlightPlan;
		})
		.thenAccept(generatedFlightPlan -> {
			List<Position> newFlightPlanPositions = FlightPlanRefiner
					.refine(generatedFlightPlan.getPathPositions(), stepSize);		

			newFlightPlanPositions.addAll(0, initialWaypoints.subList(0, initialWaypointIndex));

			synchronized (this) {
				if (initialFlightPlanVersion != flightPlanVersion) {
					logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_CANCELLED, "Flight plan (" + flightPlanVersion + ") generation cancelled: new flight plan version " +  flightPlanVersion + " detected.");
					return;
				}

				flightPlanStatus = DataStatus.AVAILABLE;
				waypoints = newFlightPlanPositions;
				flightPlanVersion++;

				logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_GENERATED, "Flight plan (" + flightPlanVersion + ") generated: " + generatedFlightPlan.getReport());
			}

		
		}).exceptionally(
			ex -> {
				synchronized (this) {
					flightPlanStatus = DataStatus.FAILED;
					logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_FAILED, "Flight plan (" + flightPlanVersion + ") generation failed: " + ex.getMessage());
				}
				return null;
			}
		);

	}

	private FlightPlan calculateFlightPlan(final Position start, final Position destination, FlightPlanCalculatorFacade calculatorService) {

        GridZone grid = null;
		FlightPlan generatedFlightPlan = new FlightPlan();
    
        int i = 0, maxIter = 7;
        do {
        	grid = calculatorService.getZoneAdapter().buildGridZone(
        			start, 
        			destination, 
        			GRID_EXPANSION_METERS * Math.pow(2, i++)
        			);
	    	generatedFlightPlan = calculatorService.getFlightPlanCalculator().computeFlightPlan(
	        		grid,
	        		getAltitudeLevels(), 
	        		getInitialCellWidth(), 
	        		start, 
	        		destination
	        		);
	    	
        } while (i < maxIter && !generatedFlightPlan.hasPath());
        
		return generatedFlightPlan;
    }

    public synchronized boolean hasReached(Position currentPosition, Position destination) {
		if (waypoints == null || waypoints.isEmpty()) {
			return false;
		}
		if (currentPosition == null || destination == null) {
			return false;
		}
		
		if (currentPosition.equals(destination)) {
			return true;
		}
		return false;
    }

	public synchronized double getFlightDistanceToEnd() {
		if (waypoints == null || waypoints.isEmpty()) {
			return -1.0;
		}
		
		double totalDistance = 0.0;
		for (int i = nextWaypointIndex; i < waypoints.size() - 1; i++) {
			totalDistance += waypoints.get(i).distance(waypoints.get(i + 1));
		}
		
		return totalDistance;
	}

	public synchronized Position configureVerticalLanding(Position currentPosition) {

		flightPlanVersion++;

		FlightPlanCalculatorFacade calculatorService = new FlightPlanCalculatorFacade();

		Position onGroundPosition = new Position(
			currentPosition.getLatitude(),
			currentPosition.getLongitude(),
			0
		);

		FlightPlan generatedFlightPlan = calculateFlightPlan(
			currentPosition,
			onGroundPosition,
			calculatorService
		);
		
		if (!generatedFlightPlan.hasPath()) {
			return onGroundPosition;
		}

		List<Position> newFlightPlanPositions = FlightPlanRefiner
				.refine(generatedFlightPlan.getPathPositions(), stepSize);		
		
		newFlightPlanPositions.addAll(0, waypoints.subList(0, nextWaypointIndex - 1));
		waypoints = newFlightPlanPositions;

		flightPlanStatus = DataStatus.AVAILABLE;
		return onGroundPosition;
	}

    public synchronized Position followFlightPlan() {
		if (waypoints == null || waypoints.isEmpty()) {
			return null;
		}
		
		if(nextWaypointIndex >= waypoints.size()) return null;
        
		Position nextPosition = waypoints.get(nextWaypointIndex++);
	
		return nextPosition;
    }

	public synchronized List<Position> getNextWaypoints() {
		if (waypoints == null || waypoints.isEmpty()) {
			return null;
		}
		if (nextWaypointIndex >= waypoints.size()) {
			return null;
		}

		int lastIndex = Math.min(nextWaypointIndex + (int)(INITIAL_CELL_WIDTH / stepSize), waypoints.size());

		return waypoints.subList(nextWaypointIndex, lastIndex);
	}

	public synchronized Position getCurrentDestination() {
		if (waypoints == null || waypoints.isEmpty()) {
			return null;
		}
		return waypoints.getLast();
	}

	public synchronized FlightPlanDTO getFlightPlan() {
		if (waypoints == null || waypoints.isEmpty()) {
			return null;
		}
		return new FlightPlanDTO(waypoints);
	}

	public synchronized DataStatus getFlightPlanStatus() {
		return flightPlanStatus;
	}

}