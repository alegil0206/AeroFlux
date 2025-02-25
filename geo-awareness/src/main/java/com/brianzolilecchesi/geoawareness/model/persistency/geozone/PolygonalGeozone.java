package com.brianzolilecchesi.geoawareness.model.persistency.geozone;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.brianzolilecchesi.geoawareness.dto.GeozoneDTO;
import com.brianzolilecchesi.geoawareness.dto.PolygonalGeozoneDTO;
import com.brianzolilecchesi.geoawareness.model.persistency.Altitude;

import com.brianzolilecchesi.geoawareness.util.Constants;

@Document(collection = Constants.GEOZONE_COLLECTION_NAME)
public class PolygonalGeozone extends Geozone {
	
	@Field("coordinates")
	private List<GeoJsonPoint> coordinates;
	
	PolygonalGeozone(
			String name, 
			String type, 
			String category, 
			String status, 
			final Altitude altitude,
			final List<GeoJsonPoint> coordinates
			) {

		super(name, type, category, status, altitude);
		setCoordinates(coordinates);
	}
	
	PolygonalGeozone() {
	}
	
	public List<GeoJsonPoint> getCoordinates() {
		return coordinates;
	}
	
	public void setCoordinates(final List<GeoJsonPoint> coordinates) {
		this.coordinates = coordinates;
	}
	
	@Override
	public void update(final GeozoneDTO geozoneDTO) {
		super.update(geozoneDTO);
		
		if (geozoneDTO instanceof PolygonalGeozoneDTO) {
			PolygonalGeozoneDTO polygonalGeozoneDTO = (PolygonalGeozoneDTO) geozoneDTO;
			
			assert polygonalGeozoneDTO.getCoordinates() != null;
			assert polygonalGeozoneDTO.getCoordinates().size() > 2;		
		
			List<GeoJsonPoint> coordinates = new ArrayList<>(polygonalGeozoneDTO.getCoordinates().size());
			for (int i = 0; i < polygonalGeozoneDTO.getCoordinates().size(); ++i) {
				coordinates.add(new GeoJsonPoint(
						polygonalGeozoneDTO.getCoordinates().get(i)[0],
						polygonalGeozoneDTO.getCoordinates().get(i)[1])
						);
			}
			setCoordinates(coordinates);
		}
	}
	
	@Override
	public String toString() {
		return String.format(
				"PolygonalGeozone[%s, coordinates=%s]",
				super.toString(),
				coordinates
				);
	}
}