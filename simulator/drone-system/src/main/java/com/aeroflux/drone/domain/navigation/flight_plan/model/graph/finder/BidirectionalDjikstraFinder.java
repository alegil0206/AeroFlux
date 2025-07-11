package com.aeroflux.drone.domain.navigation.flight_plan.model.graph.finder;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BidirectionalDijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.aeroflux.drone.domain.navigation.flight_plan.model.FlightPlan;
import com.aeroflux.drone.domain.navigation.flight_plan.model.graph.CellGraph;
import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.Cell;

public class BidirectionalDjikstraFinder extends PathFinder {

	public BidirectionalDjikstraFinder() {
		super();
	}
	
	@Override
	public String algorithm() {
		return "Djikstra";
	}

	@Override
	public FlightPlan computePath(CellGraph graph) {
		assert graph != null;
		assert graph.getGraph() != null;
		assert graph.getSource() != null;
		assert graph.getDest() != null;
		
		BidirectionalDijkstraShortestPath<Cell, DefaultWeightedEdge> djikstra = new BidirectionalDijkstraShortestPath<>(graph.getGraph());
		GraphPath<Cell, DefaultWeightedEdge> path = djikstra.getPath(graph.getSource(), graph.getDest());
		if (path == null) {
			return new FlightPlan();
		}
		
		FlightPlan fp = new FlightPlan(path.getVertexList());
		return fp;
	}

	@Override
	public String toString() {
		return String.format("DjikstraFinder[%s]", super.toString());
	}
}