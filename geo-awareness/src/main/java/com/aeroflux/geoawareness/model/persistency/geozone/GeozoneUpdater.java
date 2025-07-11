package com.aeroflux.geoawareness.model.persistency.geozone;

import org.springframework.stereotype.Component;

import com.aeroflux.geoawareness.dto.CircularGeozoneDTO;
import com.aeroflux.geoawareness.dto.GeozoneDTO;
import com.aeroflux.geoawareness.dto.PolygonalGeozoneDTO;
import com.aeroflux.geoawareness.exception.geozone.ForbiddenGeozoneUpdateException;

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