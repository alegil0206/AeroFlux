package com.brianzolilecchesi.drone.domain.model;

import com.brianzolilecchesi.drone.domain.dto.SupportPointDTO;

public class SupportPoint {
    
    private String id;
    private String name;
    private Coordinate coordinate;

    public SupportPoint(
        String id,
        String name, 
        Coordinate coordinates
    ) {
        setId(id);
        setName(name);
        setCoordinate(coordinates);
    }

    public SupportPoint() {
    }

    public SupportPoint(final SupportPointDTO supportPointDTO) {
        setId(supportPointDTO.getId());
        setName(supportPointDTO.getName());
        setCoordinate(new Coordinate(
            supportPointDTO.getLatitude(), 
            supportPointDTO.getLongitude()
        ));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinates) {
        this.coordinate = coordinates;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SupportPoint that = (SupportPoint) obj;
        return id.equals(that.id) && 
               name.equals(that.name) &&
               coordinate.equals(coordinate);
    }

    @Override
    public String toString() {
        return String.format(
            "SupportPointDTO[id=%s, name=%s, coordinates=%s]",
            getId(), 
            getName(), 
            getCoordinate().toString()
        );
    }

}
