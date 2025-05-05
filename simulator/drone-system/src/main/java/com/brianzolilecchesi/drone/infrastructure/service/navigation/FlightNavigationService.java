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
	
	public static final double INITIAL_CELL_WIDTH = 320.0;
	public static final List<Double> ALTITUDE_LEVELS = List.of(20.0, 40.0, 60.0, 80.0, 100.0, 120.0);
	
	public static final double TARGET_SENSITIVITY = 320.0;
	public static final double ACCEPTABLE_SENSITIVITY = 320.0;
	public static final int MAX_TIME_ACCEPTABLE = 60;
	public static final int MAX_TIME = 60;
	
	public static final double STEP_SIZE = 20.0;

	public static final int AVARAGE_CALCULATION_STEPS = 20;

	private final LogService logService;
    
    private double initialCellWidth;
    private double startingSensitivity;
    private List<Double> altitudeLevels;
    
    private List<Position> positions;
    private int nextPositionIndex;

	private DataStatus flightPlanStatus = DataStatus.NOT_REQUESTED;
    
    public FlightNavigationService(LogService logService) {
        this(logService, INITIAL_CELL_WIDTH, ALTITUDE_LEVELS);
    }
    
    public FlightNavigationService(
			LogService logService,
    		double initialCellWidth,
    		List<Double> altitudeLevels
    		) {
    	
        this(
				logService,
        		initialCellWidth,
        		altitudeLevels,
        		new ArrayList<Zone>(), 
        		TARGET_SENSITIVITY, 
        		ACCEPTABLE_SENSITIVITY, 
        		MAX_TIME_ACCEPTABLE, 
        		MAX_TIME
    		);
    }
    
    public FlightNavigationService(
			LogService logService,
    		double initialCellWidth,
    		List<Double> altitudeLevels,
    		List<Zone> zones,
    		double targetSensitivity,
    		double acceptableSensitivity,
    		int maxTimeAcceptable,
    		int maxTime
    		) {
    	
		this.logService = logService;
        
        setInitialCellWidth(initialCellWidth);
        setStartingSensitivity(startingSensitivity);
        setAltitudeLevels(altitudeLevels);
        
        this.positions = new ArrayList<>();
        this.nextPositionIndex = 1;
        
        FlightPlanCalculatorSingleton.getInstance();	// Force singleton initialization
    }
    
    public double getInitialCellWidth() {return initialCellWidth;}
	public void setInitialCellWidth(final double initialCellWidth) {this.initialCellWidth = initialCellWidth;}
	
	public double getStartingSensitivity() {return startingSensitivity;}
	public void setStartingSensitivity(final double startingSensitivity) {this.startingSensitivity = startingSensitivity;}
	
	public List<Double> getAltitudeLevels() {return altitudeLevels;}
	public void setAltitudeLevels(final List<Double> altitudeLevels) {this.altitudeLevels = altitudeLevels;}
    	
    @Override
    public void generateFlightPlan(final Position start, final Position destination) {
		if (getFlightPlanStatus() == DataStatus.LOADING) {
			return;
		}

		logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.GENERATING_FLIGHT_PLAN, "Generating flight plan");

		setFlightPlanStatus(DataStatus.LOADING);

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
					.refine(generatedFlightPlan.getPathPositions(), STEP_SIZE);		

			newFlightPlanPositions.addAll(0, getPositions().subList(0, getNextPositionIndex() - 1));
			setPositions(newFlightPlanPositions);
			setFlightPlanStatus(DataStatus.AVAILABLE);
			logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_GENERATED, "Flight plan generated: " + generatedFlightPlan.getReport());
		})
		.exceptionally(ex -> {
			setFlightPlanStatus(DataStatus.FAILED);
			logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_FAILED, "Flight plan generation failed: " + ex.getMessage());
			return null;
		});
	}

	@Override
	public void optimizeFlightPlan() {
		if (getFlightPlanStatus() != DataStatus.AVAILABLE ) {
			return;
		}

		int nextPositionIndex = getNextPositionIndex();
		List<Position> positions = getPositions();

		int remainingPositions = positions.size() - nextPositionIndex;
		if (remainingPositions < AVARAGE_CALCULATION_STEPS) {
			return;
		}

		logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.OPTIMIZING_FLIGHT_PLAN, "Optimizing flight plan");

		int calculateFrom = nextPositionIndex + AVARAGE_CALCULATION_STEPS;

		CompletableFuture.supplyAsync(() -> calculateFlightPlan(positions.get(calculateFrom), positions.getLast()))
		.thenApply(generatedFlightPlan -> {
			if (!generatedFlightPlan.hasPath()) {
				throw new IllegalStateException("Flight plan not found");
			}
			return generatedFlightPlan;
		})
		.thenAccept(generatedFlightPlan -> {
			List<Position> newFlightPlanPositions = FlightPlanRefinerSingleton
					.getInstance()
					.refine(generatedFlightPlan.getPathPositions(), STEP_SIZE);		

			newFlightPlanPositions.addAll(0, positions.subList(0, calculateFrom));

			setPositions(newFlightPlanPositions);
			setFlightPlanStatus(DataStatus.AVAILABLE);
			logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_OPTIMIZED, "Flight plan optimized: " + generatedFlightPlan.getReport());
		}).exceptionally(
			ex -> {
				setFlightPlanStatus(DataStatus.FAILED);
				logService.info(LogConstants.Component.NAVIGATION_SERVICE, LogConstants.Event.FLIGHT_PLAN_FAILED, "Flight plan optimization failed: " + ex.getMessage());
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
    public Position followFlightPlan() {
		List<Position> positions = getPositions();
		if (positions == null || positions.isEmpty()) {
			return null;
		}
		
		Position destination = positions.getLast();
		if (hasReached(destination)) {
			return null;
		}
		assert getNextPositionIndex() < positions.size();
        
		Position nextPosition = getNextPosition();
		incrementNextPositionIndex();
        return nextPosition;
    }

    @Override
    public boolean hasReached(Position position) {
		if (positions == null || positions.isEmpty()) {
			return false;
		}
		
		return getNextPositionIndex() >= positions.size();
    }

	@Override
	public synchronized boolean hasReached(Coordinate coordinate) {
		if (positions == null || positions.isEmpty()) {
			return false;
		}
		
		return getNextPositionIndex() >= positions.size();
	}

	@Override
	public void configureVerticalLanding(Position currentPosition) {
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
				.refine(generatedFlightPlan.getPathPositions(), STEP_SIZE);		
		
		newFlightPlanPositions.addAll(0, positions.subList(0, getNextPositionIndex() - 1));
		setPositions(newFlightPlanPositions);

		setFlightPlanStatus(DataStatus.AVAILABLE);
		
	}

	@Override
	public Position getNextPosition() {
		List<Position> positions = getPositions();
		if (positions == null || positions.isEmpty()) {
			return null;
		}
		int nextPositionIndex = getNextPositionIndex();
		if (nextPositionIndex >= positions.size()) {
			return null;
		}
		
		return positions.get(nextPositionIndex);
	}

	public synchronized FlightPlanDTO getFlightPlan() {
		if (positions == null || positions.isEmpty()) {
			return null;
		}
		return new FlightPlanDTO(positions);
	}

	@Override
	public synchronized DataStatus getFlightPlanStatus() {
		return flightPlanStatus;
	}

	private synchronized void setFlightPlanStatus(DataStatus status) {
		this.flightPlanStatus = status;
	}

	private synchronized void setPositions(List<Position> newPositions) {
		this.positions = newPositions;
	}
	
	private synchronized List<Position> getPositions() {
		return positions;
	}
	
	private synchronized int getNextPositionIndex() {
		return nextPositionIndex;
	}

	private synchronized void incrementNextPositionIndex() {
		if (nextPositionIndex < positions.size()) {
			nextPositionIndex++;
		}
	}
}