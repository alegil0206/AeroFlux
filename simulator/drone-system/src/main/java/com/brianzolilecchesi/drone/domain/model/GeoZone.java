package com.brianzolilecchesi.drone.domain.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.brianzolilecchesi.drone.domain.dto.GeoZoneDTO;

public class GeoZone {
	
	public static enum Type {
		CIRCULAR("CIRCULAR"), POLYGONAL("POLYGONAL");
		
		private String type;
		
		private Type(String type) {
			this.type = type;
		}
		
		public String getName() {
			return type;
		}
	}
	
	public static enum Category {
		RESTRICTED("RESTRICTED"), EXCLUDED("EXCLUDED");
		
		private String category;

		private Category(String category) {
			this.category = category;
		}

		public String getName() {
			return category;
		}
	}
	
	public static enum Status {
		ACTIVE("ACTIVE"), INACTIVE("INACTIVE");

		private String status;

		private Status(String status) {
			this.status = status;
		}

		public String getName() {
			return status;
		}
	}

    private String id;
    private String name;
    private String type;
    private String category;
    private String status;
    private String altitudeLevelLimitInferior;
    private Double altitudeLimitInferior;
    private String altitudeLevelLimitSuperior;
    private Double altitudeLimitSuperior;

    // Only for circular geozones
    private final Double latitude;
    private final Double longitude;
    private final Double radius;

    // Only for polygonal geozones
    private final List<Coordinate> coordinates;
	
    public GeoZone(String id, String name, String type, String category, String status,
            String altitude_level_limit_inferior, Double altitude_limit_inferior,
            String altitude_level_limit_superior, Double altitude_limit_superior, 
            Double latitude, Double longitude, Double radius, 
            List<Coordinate> coordinates) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.status = status;
        this.altitudeLevelLimitInferior = altitude_level_limit_inferior;
        this.altitudeLimitInferior = altitude_limit_inferior;
        this.altitudeLevelLimitSuperior = altitude_level_limit_superior;
        this.altitudeLimitSuperior = altitude_limit_superior;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.coordinates = coordinates;
    }
    
     public GeoZone(GeoZoneDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.type = dto.getType();
        this.category = dto.getCategory();
        this.status = dto.getStatus();
        this.altitudeLevelLimitInferior = dto.getAltitude_level_limit_inferior();
        this.altitudeLimitInferior = dto.getAltitude_limit_inferior();
        this.altitudeLevelLimitSuperior = dto.getAltitude_level_limit_superior();
        this.altitudeLimitSuperior = dto.getAltitude_limit_superior();
        this.latitude = dto.getLatitude();
        this.longitude = dto.getLongitude();
        this.radius = dto.getRadius();
        this.coordinates = new ArrayList<>();
    
        if (dto.getCoordinates() != null) {
            for (List<Double> pair : dto.getCoordinates()) {
                if (pair != null && pair.size() >= 2 && pair.get(0) != null && pair.get(1) != null) {
                    this.coordinates.add(new Coordinate(pair.get(1), pair.get(0)));
                }
            }
        }
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public String getAltitudeLevelLimitInferior() { return altitudeLevelLimitInferior; }
    public Double getAltitudeLimitInferior() { return altitudeLimitInferior; }
    public String getAltitudeLevelLimitSuperior() { return altitudeLevelLimitSuperior; }
    public Double getAltitudeLimitSuperior() { return altitudeLimitSuperior; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public Double getRadius() { return radius; }
    public List<Coordinate> getCoordinates() { return Collections.unmodifiableList(coordinates); }

	public boolean isActive() {
		return Status.ACTIVE.getName().equals(status);
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeoZone geoZone = (GeoZone) o;

        boolean equals = id.equals(geoZone.id) &&
        				name.equals(geoZone.name) &&
        				type.equals(geoZone.type) && 
        				category.equals(geoZone.category) && 
        				status.equals(geoZone.status) && 
        				altitudeLevelLimitInferior.equals(geoZone.altitudeLevelLimitInferior) &&
                        altitudeLevelLimitSuperior.equals(geoZone.altitudeLevelLimitSuperior) &&
                        altitudeLimitInferior.equals(geoZone.altitudeLimitInferior) &&
                        altitudeLimitSuperior.equals(geoZone.altitudeLimitSuperior);
        
       if (!equals) return false;
       
       if (type.equals(Type.CIRCULAR.getName())) {
           assert latitude != null;
           assert longitude != null;
           assert radius != null;
    	   
           equals = latitude.equals(geoZone.latitude) && 
    			    longitude.equals(geoZone.longitude) && 
    			    radius.equals(geoZone.radius);
       } else if (type.equals(Type.POLYGONAL.getName())) {
           assert coordinates != null;
           
           equals = coordinates.equals(geoZone.coordinates);
       }
       
        return equals;
    }
       
    @Override
    public int hashCode() {
        return id.hashCode();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Geozone ID: ").append(id)
                .append("\nName: ").append(name)
                .append("\nType: ").append(type)
                .append("\nStatus: ").append(status)
                .append("\nAltitude Level Limit Inferior: ").append(altitudeLevelLimitInferior)
                .append("\nAltitude Limit Inferior: ").append(altitudeLimitInferior)
                .append("\nAltitude Level Limit Superior: ").append(altitudeLevelLimitSuperior)
                .append("\nAltitude Limit Superior: ").append(altitudeLimitSuperior);

        if ("CIRCULAR".equalsIgnoreCase(type)) {
            sb.append("\nLatitude: ").append(latitude)
            .append("\nLongitude: ").append(longitude)
            .append("\nRadius: ").append(radius);
        }

        if (coordinates != null && !coordinates.isEmpty()) {
            sb.append("\nCoordinates: ").append(coordinates);
        }

        sb.append("\n---------------------------");
        return sb.toString();
    }

}
