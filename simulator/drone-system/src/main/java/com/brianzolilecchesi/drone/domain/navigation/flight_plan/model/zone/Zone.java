package com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.zone;

import com.brianzolilecchesi.drone.domain.navigation.flight_plan.model.bounds.ThreeDBounds;

public abstract class Zone {
	
	private final String id;
	
	public Zone(String id) {
		this.id = id;
	}
	
	public abstract ThreeDBounds getBounds();
	
	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return String.format("Zone[id=%s, bounds=%s]", id, getBounds());
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		Zone other = (Zone) obj;
		return id.equals(other.getId());
	}
}