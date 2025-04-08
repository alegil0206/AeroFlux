package com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.zone;

import com.brianzolilecchesi.drone.domain.model.Position;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.bounds.ThreeDBounds;
import com.brianzolilecchesi.drone.infrastructure.service.navigation.flight_plan.model.bounds.ThreeDRectangularBounds;

public class Cell extends Zone {
	
	private static String getId(int x, int y, int z) {
		return String.format("Cell[%d, %d, %d]", x, y, z);
	}
	
	private final int x;
	private final int y;
	private final int z;
	
	private final Position center;
	private final double width;
	private final double height;
	private ThreeDBounds bounds;
	
	public Cell(int x, int y, int z, Position center, double width, double height) {
		super(getId(x, y, z));
		this.x = x;
		this.y = y;
		this.z = z;
		this.center = center;
		this.width = width;
		this.height = height;
		
		bounds = null;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public Position getCenter() {
		return center;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	@Override
	public ThreeDBounds getBounds() {
		if (bounds == null) {
			bounds = new ThreeDRectangularBounds(center, width, height);
		}
		return bounds;
	}
	
	@Override
	public Cell clone() {
        return new Cell(x, y, z, center, width, height);
	}
	
	@Override
	public String toString() {
		return String.format("Cell[%s, x=%d, y=%d, z=%d, center=%s, width=%f, height=%f]", 
				super.toString(),
				x, y, z, 
				center, 
				width, 
				height
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

		Cell other = (Cell) obj;
		return 
				x == other.x && 
				y == other.y && 
				z == other.z;
	}
}