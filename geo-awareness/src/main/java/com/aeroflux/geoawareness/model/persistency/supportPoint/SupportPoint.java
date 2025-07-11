package com.aeroflux.geoawareness.model.persistency.supportPoint;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.aeroflux.geoawareness.dto.SupportPointDTO;
import com.aeroflux.geoawareness.util.Constants;

@Document(collection = Constants.SUPPORT_POINT_COLLECTION_NAME)
public class SupportPoint {
    
   @Id
    private String id;
    
    @Field("name")
    private String name;

    @Field("coordinates")
    private GeoJsonPoint coordinates;

    SupportPoint(String name, GeoJsonPoint coordinates) {
        setName(name);
        setCoordinates(coordinates);
    }

    public SupportPoint(String name, double longitude, double latitude) {
        setName(name);
        setCoordinates(new GeoJsonPoint(longitude, latitude));
    }

    SupportPoint() {
    }

    public SupportPoint(final SupportPointDTO supportPointDTO) {
        assert supportPointDTO != null;
 
        setId(supportPointDTO.getId());
        setName(supportPointDTO.getName());
        setCoordinates(new GeoJsonPoint(supportPointDTO.getLongitude(), supportPointDTO.getLatitude()));
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

    public GeoJsonPoint getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GeoJsonPoint coordinates) {
        this.coordinates = coordinates;
    }

    public void update(final SupportPointDTO supportPointDTO) {
        assert supportPointDTO != null;

        setName(supportPointDTO.getName());
        setCoordinates(new GeoJsonPoint(supportPointDTO.getLongitude(), supportPointDTO.getLatitude()));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SupportPoint that = (SupportPoint) obj;
        return id.equals(that.id) && name.equals(that.name) && coordinates.equals(that.coordinates);
    }

    @Override
    public String toString() {
        return String.format("SupportPoint [id=%s, name=%s, coordinates=%s]", 
        id, name, coordinates);
    }

}
