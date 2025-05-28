package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.graph;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import com.brianzolilecchesi.drone.domain.geo.GeoCalculatorFactory;
import com.brianzolilecchesi.drone.domain.geo.GeoDistanceCalculator;
import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.ThreeDBoundingBox;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.Cell;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.GridZone;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone.Zone;

public class CellGraphBuilder {

	private volatile List<Zone> zones;

	public CellGraphBuilder(final List<Zone> zones) {
		setZones(zones);
	}

	public CellGraphBuilder() {
		this(new ArrayList<>());
	}

	public synchronized List<Zone> getZones() {
		return zones;
	}

	public synchronized void setZones(final List<Zone> zones) {
		this.zones = zones;
	}
	
	//TODO: set cellHeight dynamically
	public CellGraph build(
			final GridZone grid,
			final List<Double> altitudeLevels,
			double cellWidth,
			Position source,
			Position dest
			) {

		assert altitudeLevels != null;
		assert altitudeLevels.size() > 0;
		assert cellWidth > 0;
		assert source != null;
		assert dest != null;

		GeoDistanceCalculator dc = GeoCalculatorFactory.getGeoDistanceCalculator();
		ThreeDBoundingBox bb = grid.getBounds().getBoundingBox();

		int width = (int) Math.ceil(dc.distance(bb.getBNW(), bb.getBNE()) / cellWidth);
		int height = (int) Math.ceil(dc.distance(bb.getBNW(), bb.getBSW()) / cellWidth);
		int nAltitudes = altitudeLevels.size();

		Position tl = bb.getBNW();

		Cell[][][] gridList = new Cell[height][width][nAltitudes];

		Cell s = null; // source
		Cell d = null; // destination

		boolean isZoneBlocked = false;

		Graph<Cell, DefaultWeightedEdge> graph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
		List<Zone> zones = getZones();

		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				for (int k = 0; k < nAltitudes; ++k) {
					isZoneBlocked = false;
					
					double cellHeight = altitudeLevels.get(k);
					if (k > 0)
						cellHeight -= altitudeLevels.get(k - 1);
				    cellHeight *= 2;
					
					Position center = tl.move(
							-(i * cellWidth) + (cellWidth / 2),
							(j * cellWidth) + (cellWidth / 2),
							altitudeLevels.get(k));

					gridList[i][j][k] = new Cell(i, j, k, center, cellWidth, cellHeight);
						
					for (Zone z : zones) {
						if (z.getBounds().contains(gridList[i][j][k].getBounds(), true)) {
							isZoneBlocked = true;
							break;
						}
					}
						
					if (isZoneBlocked)
						continue;

					graph.addVertex(gridList[i][j][k]);

					if (gridList[i][j][k].getBounds().contains(source)) {
						s = gridList[i][j][k];
					}
					if (gridList[i][j][k].getBounds().contains(dest)) {
						d = gridList[i][j][k];
					}

				}
			}
		}
		
		DefaultWeightedEdge e = null;
		
		// All neighbors of cells with at least one lower index
		// The neighbors with only greater indices are added in the following loops
		int[][] offsets = {
			    {-1, -1, 0}, 	// x-1, y-1, z
			    {-1, 0, -1}, 	// x-1, y, z-1
			    {0, -1, -1}, 	// x, y-1, z-1
			    {-1, -1, -1}, 	// x-1, y-1, z-1
			    {1, -1, 0}, 	// x+1, y-1, z
			    {-1, 1, 0}, 	// x-1, y+1, z
			    {0, -1, 1}, 	// x, y-1, z+1
			    {-1, 0, 1}, 	// x-1, y, z+1
			    {0, 1, -1}, 	// x, y+1, z-1
			    {1, 0, -1}, 	// x+1, y, z-1
			    {1, -1, 1}, 	// x+1, y-1, z+1
			    {1, 1, -1}, 	// x+1, y+1, z-1
			    {0, 0, -1}, 	// x, y, z-1
			    {-1, 0, 0}, 	// x-1, y, z
			    {0, -1, 0}, 	// x, y-1, z
			    {-1, -1, 1}, 	// x-1, y-1, z+1
			    {-1, 1, 1}, 	// x-1, y+1, z+1
			    {1, -1, -1}, 	// x+1, y-1, z-1
			    {1, 1, -1}, 	// x+1, y+1, z-1
			    {-1, 1, -1} 	// x-1, y+1, z-1
			};

		for (int x = 0; x < height; ++x) {
			for (int y = 0; y < width; ++y) {
				for (int z = 0; z < nAltitudes; ++z) {
					for (int[] offset : offsets) {
					    int dx = offset[0];
					    int dy = offset[1];
					    int dz = offset[2];

					    int newX = x + dx;
					    int newY = y + dy;
					    int newZ = z + dz;

					    if (
					    		newX >= 0 && newY >= 0 && newZ >= 0 && 
					    		newX < height && newY < width && newZ < nAltitudes &&
					    		graph.containsVertex(gridList[x][y][z]) &&
					    		graph.containsVertex(gridList[newX][newY][newZ])
				    		) {
					            
					    		e = graph.addEdge(gridList[x][y][z], gridList[newX][newY][newZ]);
					    		if (e != null) {
						    		graph.setEdgeWeight(e, dc.distance(
						    				gridList[x][y][z].getCenter(),
				                            gridList[newX][newY][newZ].getCenter()
				                            ));
					    		}
					        }
					}
				}
			}
		}

		return new CellGraph(graph, s, d);
	}

	@Override
	public String toString() {
		return String.format("CellGraphBuilder[zones=%s]", zones);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		CellGraphBuilder other = (CellGraphBuilder) obj;
		return zones.equals(other.zones);
	}

	@Override
	public int hashCode() {
		return zones.hashCode();
	}
	
	public void reset() {
		if (zones != null) {
			zones.clear();
		}
	}
}