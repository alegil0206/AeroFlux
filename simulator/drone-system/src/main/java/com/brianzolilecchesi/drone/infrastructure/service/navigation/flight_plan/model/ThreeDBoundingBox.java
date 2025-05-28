package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model;

import java.util.Arrays;
import java.util.List;

import com.brianzolilecchesi.drone.domain.model.Coordinate;
import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.util.Util;

public final class ThreeDBoundingBox {
	
	private final Position tnw;
	private final Position bnw;
	
	private final Position tne;
	private final Position bne;
	
	private final Position tse;
	private final Position bse;
	
	private final Position tsw;
	private final Position bsw;
	
	public ThreeDBoundingBox(
			final Position tnw, final Position bnw, 
			final Position tne, final Position bne, 
			final Position tse, final Position bse, 
			final Position tsw, final Position bsw
			) {
		assert tnw != null;
		assert bnw != null;
		assert tne != null;
		assert bne != null;
		assert tse != null;
		assert bse != null;
		assert tsw != null;
		assert bsw != null;		
		
		this.tnw = tnw;
		this.bnw = bnw;
		this.tne = tne;
		this.bne = bne;
		this.tse = tse;
		this.bse = bse;
		this.tsw = tsw;
		this.bsw = bsw;
	}
	
	public ThreeDBoundingBox(final Position p1, final Position p2, double delta) {
		assert p1 != null;
		assert p2 != null;
		assert delta > 0;
		
		double northest = Math.max(p1.getLatitude(), p2.getLatitude());
		double southest = Math.min(p1.getLatitude(), p2.getLatitude());
		double westest = Math.min(p1.getLongitude(), p2.getLongitude());
		double eastest = Math.max(p1.getLongitude(), p2.getLongitude());
		double lowest = Math.min(p1.getAltitude(), p2.getAltitude());
		double highest = Math.max(p1.getAltitude(), p2.getAltitude());
		
		this.tnw = new Position(northest, westest, highest).move(delta, -delta, 0); 	// TNO
		this.bnw = new Position(northest, westest, lowest).move(delta, -delta, 0); 		// BNO
		this.tne = new Position(northest, eastest, highest).move(delta, delta, 0); 		// TNE
		this.bne = new Position(northest, eastest, lowest).move(delta, delta, 0); 		// BNE
		this.tse = new Position(southest, eastest, highest).move(-delta, delta, 0); 	// TSE
		this.bse = new Position(southest, eastest, lowest).move(-delta, delta, 0); 		// BSE
		this.tsw = new Position(southest, westest, highest).move(-delta, -delta, 0); 	// TSO
		this.bsw = new Position(southest, westest, lowest).move(-delta, -delta, 0); 	// BSO	
	}	
	
	public Position getTNW() {
		return tnw;
	}
	
	public Position getBNW() {
		return bnw;
	}
	
	public Position getTNE() {
		return tne;
	}
	
	public Position getBNE() {
		return bne;
	}
	
	public Position getTSE() {
		return tse;
	}
	
	public Position getBSE() {
		return bse;
	}
	
	public Position getTSW() {
		return tsw;
	}
	
	public Position getBSW() {
        return bsw;
	}
	
	public List<Coordinate> getCoordinates() {
		return Arrays.asList(tnw, bnw, tne, bne, tse, bse, tsw, bsw);
	}
	
	public List<Position> getPositions() {
		return Arrays.asList(tnw, bnw, tne, bne, tse, bse, tsw, bsw);
	}
	
	public double getLowerLatitude() {
		return Util.min(tsw.getLatitude(), bsw.getLatitude(), tse.getLatitude(), bse.getLatitude());
	}
	
	public double getUpperLatitude() {
		return Util.max(tnw.getLatitude(), bnw.getLatitude(), tne.getLatitude(), bne.getLatitude());
	}
	
	public double getLowerLongitude() {
		return Util.min(tnw.getLongitude(), bnw.getLongitude(), tsw.getLongitude(), bsw.getLongitude());
	}
	
	public double getUpperLongitude() {
		return Util.max(tne.getLongitude(), bne.getLongitude(), tse.getLongitude(), bse.getLongitude());
	}
	
	public double getLowerAltitude() {
		return Util.min(bnw.getAltitude(), bne.getAltitude(), bse.getAltitude(), bsw.getAltitude());
	}
	
	public double getUpperAltitude() {
		return Util.max(tnw.getAltitude(), tne.getAltitude(), tse.getAltitude(), tsw.getAltitude());
	}
	
	public boolean contains(Position position) {
		assert position != null;
		
		double upperBoundLat = getUpperLatitude();
		double lowerBoundLat = getLowerLatitude();
		
		double upperBoundLon = getUpperLongitude();
		double lowerBoundLon = getLowerLongitude();
		
		double upperBoundAlt = getUpperAltitude();
		double lowerBoundAlt = getLowerAltitude();
		
	    return 
	    		position.getLatitude() >= lowerBoundLat && position.getLatitude() <= upperBoundLat &&
	    		position.getLongitude() >= lowerBoundLon && position.getLongitude() <= upperBoundLon &&
	    		position.getAltitude() >= lowerBoundAlt && position.getAltitude() <= upperBoundAlt;
	}
	
	public boolean contains(final ThreeDBoundingBox bbox, boolean partially) {
		assert bbox != null;
		
		if (partially) {
			return overlaps(bbox);
		}
		
		return contains(bbox);
	}
	
	public boolean overlaps(final ThreeDBoundingBox other) {
		assert other != null;
		
		double lowerBoundLat = getLowerLatitude();
		double otherLowerBoundLat = other.getLowerLatitude();
		double upperBoundLat = getUpperLatitude();
		double otherUpperBoundLat = other.getUpperLatitude();
		
		double lowerBoundLon = getLowerLongitude();
		double otherLowerBoundLon = other.getLowerLongitude();
		double upperBoundLon = getUpperLongitude();
		double otherUpperBoundLon = other.getUpperLongitude();
		
		double lowerBoundAlt = getLowerAltitude();
		double otherLowerBoundAlt = other.getLowerAltitude();
		double upperBoundAlt = getUpperAltitude();
		double otherUpperBoundAlt = other.getUpperAltitude();
		
		return 
				lowerBoundLon <= otherUpperBoundLon && upperBoundLon >= otherLowerBoundLon &&
		        lowerBoundLat <= otherUpperBoundLat && upperBoundLat >= otherLowerBoundLat &&
        		lowerBoundAlt <= otherUpperBoundAlt && upperBoundAlt >= otherLowerBoundAlt;
	}
	
	public boolean contains(final ThreeDBoundingBox bbox) {
		assert bbox != null;

		for (Position position : bbox.getPositions()) {
			if (!contains(position)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return String.format(
				"ThreeDBoundingBox[tnw=%s, bnw=%s, tne=%s, bne=%s, tse=%s, bse=%s, tsw=%s, bsw=%s]", 
				tnw, bnw, 
				tne, bne, 
				tse, bse, 
				tsw, bsw
				);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		ThreeDBoundingBox other = (ThreeDBoundingBox) obj;
		return tnw.equals(other.tnw) && bnw.equals(other.bnw) && 
			   tne.equals(other.tne) && bne.equals(other.bne) && 
			   tse.equals(other.tse) && bse.equals(other.bse) && 
			   tsw.equals(other.tsw) && bsw.equals(other.bsw);
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(new Object[] { tnw, bnw, tne, bne, tse, bse, tsw, bsw });
	}
}