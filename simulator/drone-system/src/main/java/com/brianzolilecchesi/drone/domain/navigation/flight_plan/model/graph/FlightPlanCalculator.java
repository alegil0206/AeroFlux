package com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.graph;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.brianzolilecchesi.drone.domain.geo.GeoCalculatorFactory;
import com.brianzolilecchesi.drone.domain.geo.GeoDistanceCalculator;
import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.domain.navigation.exception.flight_plan.model.graph.IllegalLinearPathException;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.FlightPlan;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.bounds.ThreeDRectangularBounds;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.graph.FlightPlanCalculationReport.Code;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.graph.finder.LinearFinder;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.graph.finder.PathFinder;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.zone.Cell;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.zone.GridZone;
import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.zone.Zone;

public class FlightPlanCalculator {

	private static final double PATH_BOUNDS_DELTA = 25.0;
	static final double DEFAULT_HEIGHT = 40.0;
	
	private final LinearFinder lpf;
	private PathFinder pf;
	private CellGraphBuilder cgb;
	
	public FlightPlanCalculator(
			final PathFinder pf,
			final CellGraphBuilder cgb
			) {
		
		this.lpf = new LinearFinder();
		setPathFinder(pf);
		setCellGraphBuilder(cgb);
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
		
		ThreeDRectangularBounds pathBounds = new ThreeDRectangularBounds(source.getCenter(), dest.getCenter(), PATH_BOUNDS_DELTA);		
		for (Zone zone : cgb.getZones()) {
			if (zone.getBounds().overlaps(pathBounds)) {
				throw new IllegalLinearPathException(zone);
			}
		}
		
		CellGraph cg = new CellGraph(source, dest);
		return lpf.computePath(cg);
	}
	
	public List<Zone> getLinearPathZones(final Position source, final Position dest) {
		assert source != null;
		assert dest != null;
		
		List<Zone> overlappingZones = new ArrayList<>();
		
		ThreeDRectangularBounds pathBounds = new ThreeDRectangularBounds(source, dest, PATH_BOUNDS_DELTA);		
		for (Zone zone : cgb.getZones()) {
			if (zone.getBounds().overlaps(pathBounds)) {
				overlappingZones.add(zone);
			}
		}
		
		return overlappingZones;
	}
	
	public FlightPlan computeFlightPlan(
			final GridZone grid,
			final List<Double> altitudeLevels,
			double cellWidth,
			final Position source,
			final Position dest
			) {
		return computeFlightPlan(grid, altitudeLevels, cellWidth, source, dest, true);
	}
	
	private double getPositionAltitudeLevel(final List<Double> altitudeLevels, final Position p) {
		assert altitudeLevels != null;
		assert p != null;
		
		double altitude = p.getAltitude();
		int i = 0;
		while (i < altitudeLevels.size() && altitude > altitudeLevels.get(i)) {
			++i;
		}
		
		if (i == 0)
			return 0.0;
				
		return altitudeLevels.get(i - 1);
	}
	
	private void adjustSourceAndDestination(
			FlightPlan fp, 
			final Position source, 
			final Position dest, 
			double sensitivity,
			double sourceAltitudeLevel, 
			double destAltitudeLevel
			) {
		
		assert fp != null;
		assert fp.getPath() != null;
		assert source != null;
		assert dest != null;
		assert sensitivity > 0;
		assert sourceAltitudeLevel >= 0;
		assert destAltitudeLevel >= 0;
		
		if (fp.getPath().isEmpty()) {
			return;
		}
		
		// Add the source and destination cells to the path
    	Cell firstCell = fp.getPath().get(0);
    	Cell sourceCell = new Cell(
	    		firstCell.getX() - 1, firstCell.getY() - 1, firstCell.getZ() - 1,
	    		source,
	    		sensitivity,
	    		sourceAltitudeLevel
	    		);
	    fp.getPath().add(0, sourceCell);
	    
	    Cell lastCell = fp.getPath().get(fp.getPath().size() - 1);
	    Cell destCell = new Cell(
	    		lastCell.getX() + 1, lastCell.getY() + 1, lastCell.getZ() + 1, 
	    		dest,
	    		sensitivity,
	    		destAltitudeLevel
	    		);
	    fp.getPath().add(destCell);
	    
	    // Smooth the first and last cells of the path
	    GeoDistanceCalculator gc = GeoCalculatorFactory.getGeoDistanceCalculator();
	    
	    Cell secondCell = fp.getPath().get(2);
	    Cell penultimateCell = fp.getPath().get(fp.getPath().size() - 3);
	    
	    Position sourceCenter = sourceCell.getCenter(),
	    		 firstCenter = firstCell.getCenter(),
	    		 secondCenter = secondCell.getCenter();
	    		 
        Position destCenter = destCell.getCenter(),
          		 lastCenter = lastCell.getCenter(),
          		 penultimateCenter = penultimateCell.getCenter();
        
        boolean smoothSource = true, smoothDest = true;
        ThreeDRectangularBounds sourceBounds = new ThreeDRectangularBounds(sourceCenter, secondCenter, PATH_BOUNDS_DELTA),
    		                    destBounds = new ThreeDRectangularBounds(lastCenter, destCenter, PATH_BOUNDS_DELTA);
        
        for (Zone zone : cgb.getZones()) {
        	var bounds = zone.getBounds();
			if (bounds.overlaps(sourceBounds)) {
				smoothSource = false;
			}
			if (bounds.overlaps(destBounds)) {
				smoothDest = false;
			}
			if (!smoothSource && !smoothDest) {
				break;
			}
		}
		
	    if (
	    		smoothSource &&
	    		gc.distance(sourceCenter, firstCenter) + gc.distance(firstCenter, secondCenter) 
	    		> 
	    		gc.distance(sourceCenter, secondCenter)) {
			
	    	fp.getPath().remove(1);
		}
		
		if (	
				smoothDest &&
				gc.distance(destCenter, lastCenter) + gc.distance(lastCenter, penultimateCenter)
				> 
				gc.distance(destCenter, penultimateCenter)) {
			
			fp.getPath().remove(fp.getPath().size() - 2);
		}
	}
	
	public FlightPlan computeFlightPlan(
			final GridZone grid,
			final List<Double> altitudeLevels,
			double cellWidth,
			final Position source,
			final Position dest, 
			boolean computeLinearPath
			) {
		assert grid != null;
        assert altitudeLevels != null;
        assert altitudeLevels.size() > 0;
        assert cellWidth > 0;
        assert source != null;
        assert dest != null;
  
        FlightPlanCalculationReport report = new FlightPlanCalculationReport(pf.algorithm(), cellWidth);
        
        Instant startTime = Instant.now();
	    
	    FlightPlan fp = null;
	    
	    double sourceAltitudeLevel = getPositionAltitudeLevel(altitudeLevels, source);
	    double destAltitudeLevel = getPositionAltitudeLevel(altitudeLevels, dest);
	    
	    if (computeLinearPath) {
		    try {
		    	fp = computeLinearFlightPlan(
	        			new Cell(0, 0, 0, source, cellWidth, sourceAltitudeLevel),
	        			new Cell(1, 1, 1, dest, cellWidth, destAltitudeLevel)
	        			);
		    	
		    	report.setCode(Code.LINEAR_PATH_FOUND);
		    	report.setAlgorithm(lpf.algorithm());
		    	report.setTimeSeconds(Duration.between(startTime, Instant.now()).toMillis() / 1000.0);
		    	fp.setReport(report);
		    	
		    	return fp;
		    	
		    } catch (IllegalLinearPathException e) {}
	    }
	
	    CellGraph cg = cgb.build(grid, altitudeLevels, cellWidth, source, dest);
	    
	    if (cg.getSource() == null || cg.getDest() == null) {
			Instant endTime = Instant.now();
			if (cg.getSource() == null) report.setCode(Code.SOURCE_NOT_FOUND);
            else 						report.setCode(Code.DESTINATION_NOT_FOUND);
			
			report.setTimeSeconds(Duration.between(startTime, endTime).toMillis() / 1000.0);
			return new FlightPlan(null, report);
		}
		
	    // Compute the path
	    fp = pf.computePath(cg);
	    
		if (!fp.hasPath()) {
			report.setCode(Code.NO_PATH_FOUND);
			report.setTimeSeconds(Duration.between(startTime, Instant.now()).toMillis() / 1000.0);
			return fp;
		}
	    	    
	    adjustSourceAndDestination(fp, source, dest, cellWidth, sourceAltitudeLevel, destAltitudeLevel);
	    
	    Instant endTime = Instant.now();
	    double secondsElapsed = Duration.between(startTime, endTime).toMillis() / 1000.0;
	    report.setTimeSeconds(secondsElapsed);
		
		fp.setReport(report);
	    return fp;
    }
}