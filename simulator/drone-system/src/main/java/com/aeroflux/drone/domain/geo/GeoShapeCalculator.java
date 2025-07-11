package com.aeroflux.drone.domain.geo;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.Circle;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.SpatialRelation;

import com.aeroflux.drone.domain.model.Coordinate;
import com.aeroflux.drone.domain.model.shape.GeoCircle;
import com.aeroflux.drone.domain.model.shape.GeoPolygon;
import com.aeroflux.drone.domain.model.shape.GeoRectangle;
import com.aeroflux.drone.domain.model.shape.GeoShape;

public class GeoShapeCalculator {
	
	private final SpatialContext ctx;
	
	GeoShapeCalculator(final SpatialContext ctx) {
		assert ctx != null;
		
		this.ctx = ctx;
	}
	
	GeoShapeCalculator() {
        this(SpatialContext.GEO);
    }
	
	private Rectangle fromGeoRectangle(final GeoRectangle geoRect) {
		assert geoRect != null;

		Coordinate sw = geoRect.getSw();
		Coordinate ne = geoRect.getNe();

		Point lowerLeft = ctx.getShapeFactory().pointXY(sw.getLongitude(), sw.getLatitude());
		Point upperRight = ctx.getShapeFactory().pointXY(ne.getLongitude(), ne.getLatitude());

		return ctx.getShapeFactory().rect(lowerLeft, upperRight);
	}
	
	private Circle fromGeoCircle(final GeoCircle geoCircle) {
		assert geoCircle != null;

		Coordinate center = geoCircle.getCenter();
		double radius = geoCircle.getRadius() / (1000.0 * DistanceUtils.DEG_TO_KM);

		Point centerPoint = ctx.getShapeFactory().pointXY(center.getLongitude(), center.getLatitude());
		return ctx.getShapeFactory().circle(centerPoint, radius);
	}
	
	private Rectangle fromGeoPolygon(final GeoPolygon geoPolygon) {
		assert geoPolygon != null;

		double minLat = Double.POSITIVE_INFINITY;
		double maxLat = Double.NEGATIVE_INFINITY;
		double minLon = Double.POSITIVE_INFINITY;
		double maxLon = Double.NEGATIVE_INFINITY;

		for (Coordinate c : geoPolygon.getPoints()) {
			minLat = Math.min(minLat, c.getLatitude());
			maxLat = Math.max(maxLat, c.getLatitude());
			minLon = Math.min(minLon, c.getLongitude());
			maxLon = Math.max(maxLon, c.getLongitude());
		}

		return ctx.getShapeFactory().rect(
				ctx.getShapeFactory().pointXY(minLon, minLat),
				ctx.getShapeFactory().pointXY(maxLon, maxLat)
				);
	}
	
	private Shape fromGeoShape(final GeoShape geoShape) {
		Shape shape = null;
		
		if (geoShape instanceof GeoRectangle) {
            shape = fromGeoRectangle((GeoRectangle) geoShape);
        } else if (geoShape instanceof GeoPolygon) {
            shape = fromGeoPolygon((GeoPolygon) geoShape);
        } else if (geoShape instanceof GeoCircle) {
            shape = fromGeoCircle((GeoCircle) geoShape);
        }
		
		if (shape == null) {
			throw new IllegalArgumentException("Unsupported shape type: " + geoShape);
		}
		
		return shape;
    }
		
	/**
	 * Checks whether a point is contained within a GeoShape.
	 * 
	 * @param geoShape the shape to check against
	 * @param c        the point to check
	 * @return true if the point is contained within the shape, false otherwise
	 */
	public boolean contains(final GeoShape geoShape, final Coordinate c) {
		Point point = ctx.getShapeFactory().pointXY(c.getLongitude(), c.getLatitude());
		Shape shape = fromGeoShape(geoShape);
		
		SpatialRelation relation = shape.relate(point);
		return relation == SpatialRelation.CONTAINS;
	}
	
	private boolean relates(final GeoShape geoShape1, final GeoShape geoShape2, final SpatialRelation relation) {
		Shape shape1 = fromGeoShape(geoShape1);
		Shape shape2 = fromGeoShape(geoShape2);
		
		SpatialRelation rel = shape1.relate(shape2);
		return rel == relation;
	}
	
	public boolean contains(final GeoShape geoShape1, final GeoShape geoShape2) {
		return relates(geoShape1, geoShape2, SpatialRelation.CONTAINS);
	}
	
	/**
	 * Checks whether geoShape1 is within geoShape2.
	 * @param geoShape1 the first shape
	 * @param geoShape2 the second shape
	 * @return true if geoShape1 is within geoShape2, false otherwise
	 */
	public boolean within(final GeoShape geoShape1, final GeoShape geoShape2) {
		return relates(geoShape1, geoShape2, SpatialRelation.WITHIN);
	}
		
	/**
	 * Checks whether geoShape1 intersects with geoShape2.
	 * 
	 * @param geoShape1 the first shape
	 * @param geoShape2 the second shape
	 * @return true if the shapes intersect, false otherwise
	 */
	public boolean intersects(final GeoShape geoShape1, final GeoShape geoShape2) {
		return relates(geoShape1, geoShape2, SpatialRelation.INTERSECTS);
	}
	
	/**
	 * Checks whether geoShape1 is disjoint from geoShape2.
	 * 
	 * @param geoShape1 the first shape
	 * @param geoShape2 the second shape
	 * @return true if the shapes are disjoint, false otherwise
	 */
	public boolean disjoint(final GeoShape geoShape1, final GeoShape geoShape2) {
        return relates(geoShape1, geoShape2, SpatialRelation.DISJOINT);
    }
	
	/**
	 * Checks whether geoShape1 overlaps (contains, is within or intersects) with geoShape2.
	 * @param geoShape1 the first shape
	 * @param geoShape2 the second shape
	 * @return true if the shapes overlap, false otherwise
	 */
	public boolean overlaps(final GeoShape geoShape1, final GeoShape geoShape2) {
		return !disjoint(geoShape1, geoShape2);
	}
}