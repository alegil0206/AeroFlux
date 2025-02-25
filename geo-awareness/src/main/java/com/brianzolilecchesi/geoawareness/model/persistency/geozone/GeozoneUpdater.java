package com.brianzolilecchesi.geoawareness.model.persistency.geozone;

import org.springframework.stereotype.Component;

import com.brianzolilecchesi.geoawareness.dto.CircularGeozoneDTO;
import com.brianzolilecchesi.geoawareness.dto.GeozoneDTO;
import com.brianzolilecchesi.geoawareness.dto.PolygonalGeozoneDTO;
import com.brianzolilecchesi.geoawareness.exception.geozone.ForbiddenGeozoneUpdateException;

@Component
public class GeozoneUpdater {
	
	GeozoneUpdater() {
	}
	
	public void update(final Geozone geozone, final GeozoneDTO geozoneDTO) throws ForbiddenGeozoneUpdateException {

		assert geozone != null;
		assert geozoneDTO != null;
		
		if (geozone instanceof CircularGeozone) {
			if (!(geozoneDTO instanceof CircularGeozoneDTO)) {
				throw new ForbiddenGeozoneUpdateException("Cannot update a circular geozone to a polygonal geozone");
			}
			((CircularGeozone) geozone).update(geozoneDTO);
		} else if (geozone instanceof PolygonalGeozone) {
			if (!(geozoneDTO instanceof PolygonalGeozoneDTO)) {
				throw new ForbiddenGeozoneUpdateException("Cannot update a polygonal geozone to a circular geozone");
			}
			((PolygonalGeozone) geozone).update(geozoneDTO);
		} else {
			throw new RuntimeException("Unsupported geozone type: this should never happen!");
		}
	}
}