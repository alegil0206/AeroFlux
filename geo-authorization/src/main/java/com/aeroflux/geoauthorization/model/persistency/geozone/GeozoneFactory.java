package com.aeroflux.geoauthorization.model.persistency.geozone;

import com.aeroflux.geoauthorization.dto.PublishedGeozoneDTO;

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