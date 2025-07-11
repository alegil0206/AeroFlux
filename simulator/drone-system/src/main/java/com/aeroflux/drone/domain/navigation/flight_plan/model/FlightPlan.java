package com.aeroflux.drone.domain.navigation.flight_plan.model;

import java.util.ArrayList;
import java.util.List;

import com.aeroflux.drone.domain.geo.GeoCalculatorFactory;
import com.aeroflux.drone.domain.geo.GeoDistanceCalculator;
import com.aeroflux.drone.domain.model.Position;
import com.aeroflux.drone.domain.navigation.flight_plan.model.graph.FlightPlanCalculationReport;
import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.Cell;
import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.ZoneCell;

/**
 * FlightPlan represents a path that a drone can take, consisting of a list of
 * cells. It also includes a report that provides information about the flight
 * plan calculation.
 * 
 * @implNote path is guaranteed to be not null.
 */
public class FlightPlan {

	private List<Cell> path;
	private FlightPlanCalculationReport report;
	
	public FlightPlan(List<Cell> path, final FlightPlanCalculationReport report) {		
		if (path == null) {
			path = new ArrayList<>();
		} 
		
		this.path = path;
		setReport(report);
	}
	
	public FlightPlan(final List<Cell> path) {
		this(path, null);
	}
	
	public FlightPlan() {
		this(null);
	}
		
	public List<Cell> getPath() {
		return path;
	}
		
	public FlightPlanCalculationReport getReport() {
		return report;
	}
	
	public void setReport(FlightPlanCalculationReport report) {
		this.report = report;
	}
	
	public boolean hasPath() {
		return !path.isEmpty();
	}
	
	public List<Position> getPathPositions() {
		List<Position> points = new ArrayList<>(path.size());
		for (Cell cell : path) {
			points.add(cell.getCenter());
		}
		
		return points;
	}
	
	public List<ZoneCell> getZoneCells() {
		List<ZoneCell> zoneCells = new ArrayList<>();
		for (Cell cell : path) {
			if (cell instanceof ZoneCell) {
				ZoneCell zoneCell = (ZoneCell) cell;
				zoneCells.add(zoneCell);
			}
		}

		return zoneCells;
	}
	
	public double getPathLength() {
		double length = 0;
		GeoDistanceCalculator calculator = GeoCalculatorFactory.getGeoDistanceCalculator();
		
		List<Position> points = getPathPositions();
		for (int i = 0; i < points.size() - 1; i++) {
			length += calculator.distance(points.get(i), points.get(i + 1));
		}

		return length;
	}
	
	@Override
	public String toString() {
		return String.format("FlightPlan[path=%s, report=%s]", path, report);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		FlightPlan other = (FlightPlan) obj;
		for (int i = 0; i < path.size(); i++) {
			Position p1 = path.get(i).getCenter();
			Position p2 = other.path.get(i).getCenter();
			if (!p1.equals(p2))
				return false;
		}
		
		return true;
	}

	@Override
	public int hashCode() {
		int result = 1;
		for (Cell cell : path) {
			result = 31 * result + cell.hashCode();
		}
		return result;
	}
}