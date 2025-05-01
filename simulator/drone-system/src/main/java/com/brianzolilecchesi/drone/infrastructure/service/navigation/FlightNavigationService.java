package com.brianzolilecchesi.drone.infrastructure.service.navigation;

import com.brianzolilecchesi.drone.domain.component.Altimeter;
import com.brianzolilecchesi.drone.domain.component.GPS;
import com.brianzolilecchesi.drone.domain.dto.FlightPlanDTO;
import com.brianzolilecchesi.drone.domain.model.Coordinate;
import com.brianzolilecchesi.drone.domain.model.DataStatus;
import com.brianzolilecchesi.drone.domain.model.Position;
import java.util.List;

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
	
    private final GPS gps;
    private final Altimeter altimeter;
	private final LogService logService;
    
    private double initialCellWidth;
    private double startingSensitivity;
    private List<Double> altitudeLevels;
    
    private FlightPlan flightPlan;
    private List<Position> positions;
    private int nextPositionIndex;

	private volatile DataStatus flightPlanStatus = DataStatus.NOT_REQUESTED;
    
    public FlightNavigationService(GPS gps, Altimeter altimeter, LogService logService) {
        this(gps, altimeter, logService, INITIAL_CELL_WIDTH, ALTITUDE_LEVELS);
    }
    
    public FlightNavigationService(
    		GPS gps, 
    		Altimeter altimeter,
			LogService logService,
    		double initialCellWidth,
    		List<Double> altitudeLevels
    		) {
    	
        this(
        		gps, 
        		altimeter,
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
    		GPS gps, 
    		Altimeter altimeter,
			LogService logService,
    		double initialCellWidth,
    		List<Double> altitudeLevels,
    		List<Zone> zones,
    		double targetSensitivity,
    		double acceptableSensitivity,
    		int maxTimeAcceptable,
    		int maxTime
    		) {
    	
        this.gps = gps;
        this.altimeter = altimeter;
		this.logService = logService;
        
        setInitialCellWidth(initialCellWidth);
        setStartingSensitivity(startingSensitivity);
        setAltitudeLevels(altitudeLevels);
        
        this.flightPlan = new FlightPlan();
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

	public FlightPlanDTO getFlightPlan() {
		if (!flightPlan.hasPath()) {
			return null;
		}
		return new FlightPlanDTO(flightPlan);
	}
    
	FlightPlan getFlightPlanTest() {return flightPlan;}
	
    @Override
    public Position getCurrentPosition() {
        return new Position(gps.getLatitude(), gps.getLongitude(), altimeter.getAltitude());
    }
    
    @Override
    public void calculateFlightPlan(final Position start, final Position destination) {

		if (flightPlanStatus == DataStatus.LOADING) {
			return;
		}
		flightPlanStatus = DataStatus.LOADING;

        GridZone grid = null;
        
        int i = 0, maxIter = 7;
        do {
        	grid = ZoneAdapterFacade.getInstance().getGridAdapter().build(
        			start, 
        			destination, 
        			GRID_EXPANSION_METERS * Math.pow(2, i++)
        			);
	    	flightPlan = FlightPlanCalculatorSingleton.getInstance().computeFlightPlan(
	        		grid,
	        		getAltitudeLevels(), 
	        		getInitialCellWidth(), 
	        		start, 
	        		destination
	        		);
	    	
        } while (i < maxIter && !flightPlan.hasPath());
        
		if (!flightPlan.hasPath()) {
			flightPlanStatus = DataStatus.FAILED;
			throw new IllegalStateException("Flight plan not found");
		}
		
		positions = FlightPlanRefinerSingleton
				.getInstance()
				.refine(flightPlan.getPathPositions(), STEP_SIZE);

		nextPositionIndex = 1;
		flightPlanStatus = DataStatus.AVAILABLE;
    }

    @Override
    public Position followFlightPlan() {
		if (!flightPlan.hasPath()) {
			return null;
		}
		
		Position destination = positions.getLast();
		if (hasReached(destination)) {
			return null;
		}
		assert nextPositionIndex < positions.size();
        
		Position nextPosition = positions.get(nextPositionIndex++);
        return nextPosition;
    }

    @Override
    public boolean hasReached(Position position) {
		if (!flightPlan.hasPath()) {
			return false;
		}
		
		return nextPositionIndex >= positions.size();
    }

	@Override
	public boolean hasReached(Coordinate coordinate) {
		if (!flightPlan.hasPath()) {
			return false;
		}
		
		return nextPositionIndex >= positions.size();
	}

	@Override
	public boolean isOnGround() {
		return altimeter.getAltitude() < 0.1;
	}

	@Override
	public void configureVerticalLanding() {
		Position onGroundPosition = new Position(
			getCurrentPosition().getLatitude(),
			getCurrentPosition().getLongitude(),
			0
		);
		calculateFlightPlan(
			getCurrentPosition(),
			onGroundPosition
		);
	}

	@Override
	public Position getNextPosition() {
		if (!flightPlan.hasPath()) {
			return null;
		}
		
		if (nextPositionIndex >= positions.size()) {
			return null;
		}
		
		return positions.get(nextPositionIndex);
	}

	@Override
	public DataStatus getFlightPlanStatus() {
		return flightPlanStatus;
	}
}