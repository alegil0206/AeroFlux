package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone;

import com.brianzolilecchesi.drone.domain.model.Position;

public class ZoneCell extends Cell {
	
	private Zone zone;

	public ZoneCell(
			int x, int y, int z, 
			Position center, 
			double width, 
			double height, 
			Zone zone
			) {
		
		super(x, y, z, center, width, height);
		
		assert zone != null;
		setZone(zone);
	}
	
	public Zone getZone() {
		return zone;
	}
	
	private void setZone(Zone zone) {
		this.zone = zone;
	}
		
	@Override
	public String toString() {
		return String.format("ZoneCell[%s, zone=%s]", 
				super.toString(),
				zone
				);
	}

	@Override
	public ZoneCell clone() {
		ZoneCell cloned = (ZoneCell) super.clone();
		return cloned;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ZoneCell)) {
			return false;
		}
		
		ZoneCell other = (ZoneCell) o;
		
		return super.equals(other) && zone.equals(other.getZone());
	}

	@Override
	public int hashCode() {
		return super.hashCode() + zone.hashCode();
	}
}