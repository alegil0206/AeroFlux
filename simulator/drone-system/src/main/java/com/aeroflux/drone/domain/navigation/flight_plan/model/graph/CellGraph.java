package com.aeroflux.drone.domain.navigation.flight_plan.model.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.aeroflux.drone.domain.navigation.flight_plan.model.zone.Cell;

public class CellGraph {
	
	private Graph<Cell, DefaultWeightedEdge> graph;

	private Cell source;
	private Cell dest;

	public CellGraph(final Graph<Cell, DefaultWeightedEdge> graph, final Cell source, final Cell dest) {
		setGraph(graph);
		setDest(dest);
		setSource(source);
	}

	public CellGraph(final Cell source, final Cell dest) {
		this(new DefaultUndirectedWeightedGraph<Cell, DefaultWeightedEdge>(DefaultWeightedEdge.class), source, dest);
	}

	public void setGraph(final Graph<Cell, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}

	public Graph<Cell, DefaultWeightedEdge> getGraph() {
		return this.graph;
	}

	public void setSource(Cell source) {
		this.source = source;
	}

	public void setDest(Cell dest) {
		this.dest = dest;
	}

	public Cell getSource() {
		return this.source;
	}

	public Cell getDest() {
		return this.dest;
	}
	
	@Override
	public String toString() {
		return String.format("CellGraph[source=%s, dest=%s]", getSource(), getDest());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof CellGraph)) {
			return false;
		}
		CellGraph other = (CellGraph) o;
		return graph.equals(other.getGraph()) && 
			   other.getSource().equals(getSource()) && 
			   other.getDest().equals(getDest());
	}

	@Override
	public int hashCode() {
		return graph.hashCode() + source.hashCode() + dest.hashCode();
	}
}