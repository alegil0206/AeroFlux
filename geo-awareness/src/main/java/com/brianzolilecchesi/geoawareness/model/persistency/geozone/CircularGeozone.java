package com.brianzolilecchesi.geoawareness.model.persistency.geozone;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.brianzolilecchesi.geoawareness.dto.CircularGeozoneDTO;
import com.brianzolilecchesi.geoawareness.dto.GeozoneDTO;
import com.brianzolilecchesi.geoawareness.model.persistency.Altitude;

import com.brianzolilecchesi.geoawareness.util.Constants;

@Document(collection = Constants.GEOZONE_COLLECTION_NAME)
public class CircularGeozone extends Geozone {
			
	@Field("center")
    private GeoJsonPoint center;
	
	@Field("radius")
	private Double radius;
	
	CircularGeozone(
			String name, 
			String type,
			String category,
			String status,
			final Altitude altitude,
			final GeoJsonPoint center,
			Double radius
			) {
		
		super(name, type, category, status, altitude);
		setCenter(center);
		setRadius(radius);
		setAltitude(altitude);
	}
		
	CircularGeozone() {
	}
	
	public GeoJsonPoint getCenter() {
		return center;
	}
	
	public void setCenter(final GeoJsonPoint center) {
		this.center = center;
	}
	
	public Double getRadius() {
		return radius;
	}
	
	public void setRadius(Double radius) {
		this.radius = radius;
	}
	
	@Override
	public void update(final GeozoneDTO geozoneDTO) {
		super.update(geozoneDTO);
		
		if (geozoneDTO instanceof CircularGeozoneDTO) {
			CircularGeozoneDTO circularGeozoneDTO = (CircularGeozoneDTO) geozoneDTO;
			setCenter(new GeoJsonPoint(circularGeozoneDTO.getLongitude(), circularGeozoneDTO.getLatitude()));
			setRadius(circularGeozoneDTO.getRadius());
		}
	}
	
	@Override
	public String toString() {
		return String.format(
				"CircularGeozone[%s, center=%s, radius=%f]",
				super.toString(),
				getCenter(), 
				getRadius()
				);
	}
}