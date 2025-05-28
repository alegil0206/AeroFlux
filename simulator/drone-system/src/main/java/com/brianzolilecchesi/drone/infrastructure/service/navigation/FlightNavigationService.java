package com.brianzolilecchesi.drone.infrastructure.service.navigation;

import com.brianzolilecchesi.drone.domain.dto.FlightPlanDTO;
import com.brianzolilecchesi.drone.domain.model.Coordinate;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.LogConstants;
import com.brianzolilecchesi.drone.domain.model.Position;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.ArrayList;

import com.brianzolilecchesi.drone.domain.service.log.LogService;
import com.brianzolilecchesi.drone.domain.service.navigation.NavigationService;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.FlightPlan;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.FlightPlanCalculatorSingleton;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.FlightPlanRefinerSingleton;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.GridZone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.Zone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.adapter.ZoneAdapterFacade;

public class FlightNavigationService implements NavigationService {

	private static final double GRID_EXPANSION_METERS = 1000.0;
	
	public static final double INITIAL_CELL_WIDTH = 160.0; //320
	public static final List<Double> ALTITUDE_LEVELS = List.of(20.0, 40.0, 60.0, 80.0, 100.0, 120.0);
	
	public static final int AVERAGE_CALCULATION_STEPS = 20;


	private final double stepSize;
	private final LogService logService;
    
    private double initialCellWidth;
    private List<Double> altitudeLevels;
    
    private List<Position> waypoints;
    private int nextWaypointIndex;
	private int flightPlanVersion;
	private DataStatus flightPlanStatus = DataStatus.NOT_REQUESTED;
    
    public FlightNavigationService(LogService logService, double stepSize) {
        this(logService, stepSize, INITIAL_CELL_WIDTH, ALTITUDE_LEVELS);
    }
    
    public FlightNavigationService(
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
    
    public FlightNavigationService(
			LogService logService,
			double stepSize,
    		double initialCellWidth,
    		List<Double> altitudeLevels,
    		List<Zone> zones
    		) {
    	
		this.logService = logService;
        this.stepSize = stepSize;
        setInitialCellWidth(initialCellWidth);
        setAltitudeLevels(altitudeLevels);
        
        this.waypoints = new ArrayList<>();
        this.nextWaypointIndex = 1;
		this.flightPlanVersion = 0;
        
        FlightPlanCalculatorSingleton.getInstance();	// Force singleton initialization
    }
    
    public double getInitialCellWidth() {return initialCellWidth;}
	public void setInitialCellWidth(final double initialCellWidth) {this.initialCellWidth = initialCellWidth;}
	
	public List<Double> getAltitudeLevels() {return altitudeLevels;}
	public void setAltitudeLevels(final List<Double> altitudeLevels) {this.altitudeLevels = altitudeLevels;}

    	
    @Override
    public void generateFlightPlan(final Position start, final Position destination) {

		synchronized (this) {
			if (flightPlanStatus == DataStatus.LOADING) {
				return;
			}
		}

		logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.GENERATING_FLIGHT_PLAN, "Generating flight plan");
		
		int initialFlightPlanVersion;
		
		synchronized (this) {
			initialFlightPlanVersion = flightPlanVersion;
			flightPlanStatus = DataStatus.LOADING;
		}

		CompletableFuture.supplyAsync(() -> calculateFlightPlan(start, destination))
		.thenApply(generatedFlightPlan -> {
			if (!generatedFlightPlan.hasPath()) {
				throw new IllegalStateException("Flight plan not found");
			}
			return generatedFlightPlan;
		})
		.thenAccept(generatedFlightPlan -> {

			List<Position> newFlightPlanPositions = FlightPlanRefinerSingleton
					.getInstance()
					.refine(generatedFlightPlan.getPathPositions(), stepSize);		
			synchronized (this) {
				newFlightPlanPositions.addAll(0, waypoints.subList(0, nextWaypointIndex - 1));
				int actualFlightPlanVersion = flightPlanVersion;
				if (initialFlightPlanVersion != actualFlightPlanVersion) {
					logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_GENERATED, "Flight plan generation cancelled: new flight plan version");
					return;
				}
				flightPlanStatus = DataStatus.AVAILABLE;
				waypoints = newFlightPlanPositions;
				flightPlanVersion++;
			}

			logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_GENERATED, "Flight plan generated: " + generatedFlightPlan.getReport());
		})
		.exceptionally(ex -> {
			synchronized (this) {
				flightPlanStatus = DataStatus.FAILED;
			}
			logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_FAILED, "Flight plan generation failed: " + ex.getMessage());
			return null;
		});
	}

	@Override
	public void optimizeFlightPlan() {
		int initialFlightPlanVersion;
		int initialNextPositionIndex;
		List<Position> initialPositions;	
		
		synchronized (this) {
			if (flightPlanStatus != DataStatus.AVAILABLE) {
				return;
			}
			initialPositions = waypoints;
			initialFlightPlanVersion = flightPlanVersion;
			initialNextPositionIndex = nextWaypointIndex;
		}

		int remainingPositions = initialPositions.size() - initialNextPositionIndex;
		
		if (remainingPositions < AVERAGE_CALCULATION_STEPS) {
			return;
		}

		logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.OPTIMIZING_FLIGHT_PLAN, "Optimizing flight plan");

		int calculateFrom = initialNextPositionIndex + AVERAGE_CALCULATION_STEPS;

		CompletableFuture.supplyAsync(() -> calculateFlightPlan(initialPositions.get(calculateFrom), initialPositions.getLast()))
		.thenApply(generatedFlightPlan -> {
			if (!generatedFlightPlan.hasPath()) {
				throw new IllegalStateException("Flight plan not found");
			}
			return generatedFlightPlan;
		})
		.thenAccept(generatedFlightPlan -> {
			List<Position> newFlightPlanPositions = FlightPlanRefinerSingleton
					.getInstance()
					.refine(generatedFlightPlan.getPathPositions(), stepSize);		

			newFlightPlanPositions.addAll(0, initialPositions.subList(0, calculateFrom));

			synchronized (this) {
				if (initialFlightPlanVersion != flightPlanVersion) {
					logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_OPTIMIZED, "Flight plan optimization cancelled: new flight plan version");
					return;
				}

				flightPlanStatus = DataStatus.AVAILABLE;
				waypoints = newFlightPlanPositions;
				flightPlanVersion++;
			}

			logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_OPTIMIZED, "Flight plan optimized: " + generatedFlightPlan.getReport());
		
		}).exceptionally(
			ex -> {
				synchronized (this) {
					flightPlanStatus = DataStatus.FAILED;
				}
				logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_FAILED, "Flight plan optimization failed: " + ex.getMessage());
				return null;
			}
		);

	}

	@Override
	public void adaptFlightPlan() {
		int initialFlightPlanVersion;
		int initialNextPositionIndex;
		int finalPositionIndex;
		List<Position> initialPositions;	
		
		synchronized (this) {
			if (flightPlanStatus != DataStatus.AVAILABLE) {
				return;
			}
			initialPositions = waypoints;
			initialFlightPlanVersion = flightPlanVersion;
			initialNextPositionIndex = nextWaypointIndex - 1;
			finalPositionIndex = initialNextPositionIndex + 2 * (int)(INITIAL_CELL_WIDTH / stepSize);

			if (finalPositionIndex >= initialPositions.size()) {
				return;
			}

			flightPlanStatus = DataStatus.LOADING;
		}

		logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.ADAPT_FLIGHT_PLAN, "Adapting flight plan");

		CompletableFuture.supplyAsync(() -> calculateFlightPlan(initialPositions.get(initialNextPositionIndex), initialPositions.get(finalPositionIndex)))
		.thenApply(generatedFlightPlan -> {
			if (!generatedFlightPlan.hasPath()) {
				throw new IllegalStateException("Flight plan not found");
			}
			return generatedFlightPlan;
		})
		.thenAccept(generatedFlightPlan -> {
			List<Position> newFlightPlanPositions = FlightPlanRefinerSingleton
					.getInstance()
					.refine(generatedFlightPlan.getPathPositions(), stepSize);		

			newFlightPlanPositions.addAll(0, initialPositions.subList(0, initialNextPositionIndex));
			newFlightPlanPositions.addAll(newFlightPlanPositions.size(), initialPositions.subList(finalPositionIndex + 1, initialPositions.size()) );

			synchronized (this) {
				if (initialFlightPlanVersion != flightPlanVersion) {
					logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_ADAPTED, "Flight plan adaptation cancelled: new flight plan version");
					return;
				}

				flightPlanStatus = DataStatus.AVAILABLE;
				waypoints = newFlightPlanPositions;
				flightPlanVersion++;
			}

			logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_ADAPTED, "Flight plan adapted: " + generatedFlightPlan.getReport());
		
		}).exceptionally(
			ex -> {
				synchronized (this) {
					flightPlanStatus = DataStatus.FAILED;
				}
				logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_FAILED, "Flight plan adaptation failed: " + ex.getMessage());
				return null;
			}
		);

	}

	private FlightPlan calculateFlightPlan(final Position start, final Position destination) {

        GridZone grid = null;
		FlightPlan generatedFlightPlan = new FlightPlan();
    
        int i = 0, maxIter = 7;
        do {
        	grid = ZoneAdapterFacade.getInstance().getGridAdapter().build(
        			start, 
        			destination, 
        			GRID_EXPANSION_METERS * Math.pow(2, i++)
        			);
	    	generatedFlightPlan = FlightPlanCalculatorSingleton.getInstance().computeFlightPlan(
	        		grid,
	        		getAltitudeLevels(), 
	        		getInitialCellWidth(), 
	        		start, 
	        		destination
	        		);
	    	
        } while (i < maxIter && !generatedFlightPlan.hasPath());
        
		return generatedFlightPlan;
    }

    @Override
    public synchronized boolean hasReached(Position currentPosition, Position destination) {
		if (waypoints == null || waypoints.isEmpty()) {
			return false;
		}
		return currentPosition.distance(destination) < stepSize;
    }

	@Override
	public synchronized boolean hasReached(Coordinate currentPosition, Coordinate destination) {
		if (waypoints == null || waypoints.isEmpty()) {
			return false;
		}
		return currentPosition.distance(destination) < stepSize;
	}

	@Override
	public synchronized void configureVerticalLanding(Position currentPosition) {

		flightPlanVersion++;

		Position onGroundPosition = new Position(
			currentPosition.getLatitude(),
			currentPosition.getLongitude(),
			0
		);

		FlightPlan generatedFlightPlan = calculateFlightPlan(
			currentPosition,
			onGroundPosition
		);
		
		if (!generatedFlightPlan.hasPath()) {
			return;
		}

		List<Position> newFlightPlanPositions = FlightPlanRefinerSingleton
				.getInstance()
				.refine(generatedFlightPlan.getPathPositions(), stepSize);		
		
		newFlightPlanPositions.addAll(0, waypoints.subList(0, nextWaypointIndex - 1));
		waypoints = newFlightPlanPositions;

		flightPlanStatus = DataStatus.AVAILABLE;
		
	}

	@Override
    public synchronized Position followFlightPlan() {
		if (waypoints == null || waypoints.isEmpty()) {
			return null;
		}
		
		assert nextWaypointIndex < waypoints.size();
        
		Position nextPosition = waypoints.get(nextWaypointIndex++);
	
		return nextPosition;
    }

	@Override
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

	public synchronized FlightPlanDTO getFlightPlan() {
		if (waypoints == null || waypoints.isEmpty()) {
			return null;
		}
		return new FlightPlanDTO(waypoints);
	}

	@Override
	public synchronized DataStatus getFlightPlanStatus() {
		return flightPlanStatus;
	}

}