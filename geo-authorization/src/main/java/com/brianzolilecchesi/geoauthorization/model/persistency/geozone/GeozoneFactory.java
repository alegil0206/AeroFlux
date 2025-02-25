package com.brianzolilecchesi.geoauthorization.model.persistency.geozone;

import com.brianzolilecchesi.geoauthorization.dto.PublishedGeozoneDTO;

public class GeozoneFactory {
	
	GeozoneFactory() {
	}
	
	public Geozone createGeozone(final PublishedGeozoneDTO geozoneDTO) {
		return new Geozone (
				geozoneDTO.getId(), 
				geozoneDTO.getCategory(), 
				geozoneDTO.getStatus()
                );
	}
}