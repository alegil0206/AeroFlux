package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model;

import java.util.Arrays;
import java.util.List;

import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.util.Util;

public final class ThreeDBoundingBox {
	
	private final Position tno;
	private final Position bno;
	
	private final Position tne;
	private final Position bne;
	
	private final Position tse;
	private final Position bse;
	
	private final Position tso;
	private final Position bso;
	
	public ThreeDBoundingBox(
			Position tno, Position bno, 
			Position tne, Position bne, 
			Position tse, Position bse, 
			Position tso, Position bso
			) {
		
		this.tno = tno;
		this.bno = bno;
		this.tne = tne;
		this.bne = bne;
		this.tse = tse;
		this.bse = bse;
		this.tso = tso;
		this.bso = bso;
	}
	
	public Position getTNO() {
		return tno;
	}
	
	public Position getBNO() {
		return bno;
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
	
	public Position getTSO() {
		return tso;
	}
	
	public Position getBSO() {
        return bso;
	}
	
	public List<Position> getPositions() {
		return Arrays.asList(tno, bno, tne, bne, tse, bse, tso, bso);
	}
	
	public double getLowerLatitude() {
		return Util.min(tso.getLatitude(), bso.getLatitude(), tse.getLatitude(), bse.getLatitude());
	}
	
	public double getUpperLatitude() {
		return Util.max(tno.getLatitude(), bno.getLatitude(), tne.getLatitude(), bne.getLatitude());
	}
	
	public double getLowerLongitude() {
		return Util.min(tno.getLongitude(), bno.getLongitude(), tso.getLongitude(), bso.getLongitude());
	}
	
	public double getUpperLongitude() {
		return Util.max(tne.getLongitude(), bne.getLongitude(), tse.getLongitude(), bse.getLongitude());
	}
	
	public double getLowerAltitude() {
		return Util.min(bno.getAltitude(), bne.getAltitude(), bse.getAltitude(), bso.getAltitude());
	}
	
	public double getUpperAltitude() {
		return Util.max(tno.getAltitude(), tne.getAltitude(), tse.getAltitude(), tso.getAltitude());
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
			return intersect(bbox);
		}
		
		return contains(bbox);
	}
	
	public boolean intersect(final ThreeDBoundingBox other) {
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
				"ThreeDBoundingBox[tno=%s, bno=%s, tne=%s, bne=%s, tse=%s, bse=%s, tso=%s, bso=%s]", 
				tno, bno, 
				tne, bne, 
				tse, bse, 
				tso, bso
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
		return tno.equals(other.tno) && bno.equals(other.bno) && 
			   tne.equals(other.tne) && bne.equals(other.bne) && 
			   tse.equals(other.tse) && bse.equals(other.bse) && 
			   tso.equals(other.tso) && bso.equals(other.bso);
	}
}