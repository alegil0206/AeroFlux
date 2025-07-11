package com.aeroflux.geoawareness.model.persistency.geozone;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.aeroflux.geoawareness.dto.CircularGeozoneDTO;
import com.aeroflux.geoawareness.dto.GeozoneDTO;
import com.aeroflux.geoawareness.model.persistency.Altitude;

import com.aeroflux.geoawareness.util.Constants;

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
			final Altitude altitudeLimitInferior,
			final Altitude altitudeLimitSuperior,
			final GeoJsonPoint center,
			Double radius
			) {
		
		super(name, type, category, status, altitudeLimitInferior, altitudeLimitSuperior);
		setCenter(center);
		setRadius(radius);
		setAltitudeLimitInferior(altitudeLimitInferior);
		setAltitudeLimitSuperior(altitudeLimitSuperior);
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