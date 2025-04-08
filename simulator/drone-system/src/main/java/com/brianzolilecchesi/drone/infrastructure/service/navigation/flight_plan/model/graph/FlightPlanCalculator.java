package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.exception.flight_plan.model.graph.IllegalLinearPathException;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.FlightPlan;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.ThreeDBoundingBox;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.bounds.ThreeDRectangularBounds;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.FlightPlanCalculationReport.Code;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.finder.LinearFinder;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph.finder.PathFinder;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.Cell;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.GridZone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.Zone;

public class FlightPlanCalculator {

	private static final double TOLLERANCE = 1e-12;
	static final double DEFAULT_HEIGHT = 40.0;
	
	protected double targetSensitivity;
	protected double acceptableSensitivity;
	protected int maxTimeAcceptable;
	protected int maxTime;

	private final LinearFinder lpf;
	private PathFinder pf;
	private CellGraphBuilder cgb;
	
	public FlightPlanCalculator(
			double targetSensitivity,
			double acceptableSensitivity,
			int maxTimeAcceptable,
			int maxTime,
			final PathFinder pf,
			final CellGraphBuilder cgb
			) {
		
		assert targetSensitivity > 0;
		assert acceptableSensitivity > 0;
		assert maxTimeAcceptable > 0;
		assert maxTime > 0;
	  
		setTargetSensitivity(targetSensitivity);
		setAcceptableSensitivity(acceptableSensitivity);
		setMaxTimeAcceptable(maxTimeAcceptable);
		setMaxTime(maxTime);
		
		this.lpf = new LinearFinder();
		setPathFinder(pf);
		setCellGraphBuilder(cgb);
	}
	
	public double getTargetSensitivity() {
	    return targetSensitivity;
	}

	public void setTargetSensitivity(double targetSensitivity) {
		this.targetSensitivity = targetSensitivity;
	}

	public double getAcceptableSensitivity() {
		return acceptableSensitivity;
	}

	public void setAcceptableSensitivity(double acceptableSensitivity) {
		this.acceptableSensitivity = acceptableSensitivity;
	}

	public int getMaxTimeAcceptable() {
	    return maxTimeAcceptable;
	}

	public void setMaxTimeAcceptable(int maxTimeAcceptable) {
	    this.maxTimeAcceptable = maxTimeAcceptable;
	}

	public int getMaxTime() {
	    return maxTime;
	}

	public void setMaxTime(int maxTime) {
	    this.maxTime = maxTime;
	}
	
	public PathFinder getPathFinder() {
		return pf;
	}
	
	public void setPathFinder(PathFinder pf) {
		this.pf = pf;
	}
	
	public CellGraphBuilder getCellGraphBuilder() {
		return cgb;
	}

	public void setCellGraphBuilder(CellGraphBuilder cgb) {
		this.cgb = cgb;
	}
	
	private FlightPlan computeLinearFlightPlan(final Cell source, final Cell dest) throws IllegalLinearPathException {
		assert source != null;
		assert dest != null;
		
		double delta = 25.0;
		
		double northest = Math.max(source.getCenter().getLatitude(), dest.getCenter().getLatitude());
		double southest = Math.min(source.getCenter().getLatitude(), dest.getCenter().getLatitude());
		double westest = Math.min(source.getCenter().getLongitude(), dest.getCenter().getLongitude());
		double eastest = Math.max(source.getCenter().getLongitude(), dest.getCenter().getLongitude());
		double lowest = Math.min(source.getCenter().getAltitude(), dest.getCenter().getAltitude());
		double highest = Math.max(source.getCenter().getAltitude(), dest.getCenter().getAltitude());
		
		ThreeDRectangularBounds pathBounds = new ThreeDRectangularBounds(new ThreeDBoundingBox(
				new Position(northest, westest, highest).move(delta, -delta, 0), 	// TNO
				new Position(northest, westest, lowest).move(delta, -delta, 0), 	// BNO
				new Position(northest, eastest, highest).move(delta, delta, 0), 	// TNE
				new Position(northest, eastest, lowest).move(delta, delta, 0), 		// BNE
				new Position(southest, eastest, highest).move(-delta, delta, 0), 	// TSE
				new Position(southest, eastest, lowest).move(-delta, delta, 0), 	// BSE
				new Position(southest, westest, highest).move(-delta, -delta, 0), 	// TSO
				new Position(southest, westest, lowest).move(-delta, -delta, 0)		// BSO
				));
		
		for (Zone zone : cgb.getZones()) {
			if (zone.getBounds().intersects(pathBounds)) {
				throw new IllegalLinearPathException(zone);
			}
		}
		
		CellGraph cg = new CellGraph(source, dest);
		return lpf.computePath(cg);
	}
	
	public FlightPlan computeFlightPlan(
			final GridZone grid,
			final List<Double> altitudeLevels,
			double cellWidth,
			final Position source,
			final Position dest
			) {
		assert grid != null;
        assert altitudeLevels != null;
        assert altitudeLevels.size() > 0;
        assert cellWidth > 0;
        assert source != null;
        assert dest != null;
  
        FlightPlanCalculationReport report = new FlightPlanCalculationReport(pf.algorithm(), cellWidth);
        
        Instant startTime = Instant.now();
	    Instant maxAccettableTime = startTime.plusSeconds(maxTimeAcceptable);
	    Instant maxTime = startTime.plusSeconds(this.maxTime);
	    
	    FlightPlan fp = null;
	    
	    try {
	    	fp = computeLinearFlightPlan(
        			new Cell(0, 0, 0, source, cellWidth, DEFAULT_HEIGHT),
        			new Cell(1, 1, 1, dest, cellWidth, DEFAULT_HEIGHT)
        			);
	    	
	    	report.setCode(Code.LINEAR_PATH_FOUND);
	    	report.setAlgorithm(lpf.algorithm());
	    	report.setTimeSeconds(Duration.between(startTime, Instant.now()).toMillis() / 1000.0);
	    	report.setIterations(0);
	    	return fp;
	    	
	    } catch (IllegalLinearPathException e) {}
	
	    CellGraph cg = cgb.build(grid, altitudeLevels, cellWidth, source, dest);
	    
	    if (cg.getSource() == null || cg.getDest() == null) {
			Instant endTime = Instant.now();
			if (cg.getSource() == null) report.setCode(Code.SOURCE_NOT_FOUND);
            else 						report.setCode(Code.DESTINATION_NOT_FOUND);
			
			report.setTimeSeconds(Duration.between(startTime, endTime).toMillis() / 1000.0);
			report.setIterations(0);
			return new FlightPlan(null, report);
		}
		
	    // Compute the path
	    fp = pf.computePath(cg);
	    
		if (!fp.hasPath()) {
			report.setCode(Code.NO_PATH_FOUND);
			report.setTimeSeconds(Duration.between(startTime, Instant.now()).toMillis() / 1000.0);
			report.setIterations(0);
			return fp;
		}
	    	    
	    double sensitivity = cellWidth;
	    int i = 0;
	    boolean failure = false;
	    
	    // Iteratively refine the path with a lower sensitivity
	    while (
	    		(targetSensitivity < sensitivity) && 
	    		(sensitivity > acceptableSensitivity || Instant.now().isBefore(maxAccettableTime)) && 
	    		Instant.now().isBefore(maxTime)
	    	  ) {
	    	
	    	sensitivity /= 2;
	    	
	    	cg = cgb.build(fp, sensitivity, source, dest, sensitivity, TOLLERANCE);
	    	if (cg.getSource() == null || cg.getDest() == null) {
	    		failure = true;
	    		break;
	    	}
	    	
	    	FlightPlan fp1 = pf.computePath(cg);
			if (!fp1.hasPath()) {
				failure = true;
				break;
			}
			
			fp = fp1;
	    	++i;
	    }
	    
	    // Insert start and destination in the path
	    if (!fp.getPath().isEmpty()) {
	    	Cell firstCell = fp.getPath().getFirst();
		    fp.getPath().add(0, new Cell(
		    		firstCell.getX() - 1, firstCell.getY() - 1, firstCell.getZ() - 1,
		    		source,
		    		sensitivity,
		    		DEFAULT_HEIGHT
		    		));
		    
		    Cell lastCell = fp.getPath().getLast();
		    fp.getPath().add(new Cell(
		    		lastCell.getX() + 1, lastCell.getY() + 1, lastCell.getZ() + 1, 
		    		dest,
		    		sensitivity,
		    		DEFAULT_HEIGHT
		    		));
	    }
	    
	    Instant endTime = Instant.now();
	    double secondsElapsed = Duration.between(startTime, endTime).toMillis() / 1000.0;
	    report.setIterations(i);
	    report.setTimeSeconds(secondsElapsed);
	    
		if (sensitivity <= targetSensitivity) {
			report.setCode(Code.TARGET_SENSITIVITY_REACHED);
		} 
		else if (secondsElapsed >= this.maxTime) {
			report.setCode(Code.MAX_TIME_REACHED);
		} 
		else if (sensitivity <= acceptableSensitivity) {
			if (failure) {
				report.setCode(Code.UNABLE_TO_IMPROVE_SENSITIVITY);
            } else {
                report.setCode(Code.ACCEPTABLE_SENSITIVITY_REACHED);
			}
		}
		
		fp.setReport(report);
	    return fp;
    }
}