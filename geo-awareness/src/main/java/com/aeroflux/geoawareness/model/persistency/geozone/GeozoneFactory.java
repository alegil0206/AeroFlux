package com.aeroflux.geoawareness.model.persistency.geozone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;

import com.aeroflux.geoawareness.dto.CircularGeozoneDTO;
import com.aeroflux.geoawareness.dto.GeozoneDTO;
import com.aeroflux.geoawareness.dto.PolygonalGeozoneDTO;
import com.aeroflux.geoawareness.exception.geozone.IllegalGeozoneException;
import com.aeroflux.geoawareness.model.persistency.Altitude;
import com.aeroflux.geoawareness.model.persistency.Type;
import com.aeroflux.geoawareness.util.Constants;

/**
 * Factory class for creating Geozone instances from GeozoneDTO instances and vice versa.
 * If the type of Geozone keeps increasing, consider to use the Factory Method pattern. 
 */
@Component
public class GeozoneFactory {
		
	public GeozoneFactory() {
	}
	
	private CircularGeozone createGeozone(final CircularGeozoneDTO circularGeozoneDTO) {
		assert circularGeozoneDTO != null;
		
		return new CircularGeozone(
				circularGeozoneDTO.getName(), 
				Constants.CIRCULAR_GEOZONE_TYPE, 
				circularGeozoneDTO.getCategory(),
				circularGeozoneDTO.getStatus(), 
				new Altitude(
						circularGeozoneDTO.getAltitudeLevelLimitInferior(),
						circularGeozoneDTO.getAltitudeLimitInferior()
						),
				new Altitude(
						circularGeozoneDTO.getAltitudeLevelLimitSuperior(),
						circularGeozoneDTO.getAltitudeLimitSuperior()
						),			
				new GeoJsonPoint(
						circularGeozoneDTO.getLongitude(), 
						circularGeozoneDTO.getLatitude()
						),
				circularGeozoneDTO.getRadius()
				);
	}
	
	private PolygonalGeozone createGeozone(final PolygonalGeozoneDTO polygonalGeozoneDTO) {
		assert polygonalGeozoneDTO != null;
		assert polygonalGeozoneDTO.getCoordinates() != null;
		assert polygonalGeozoneDTO.getCoordinates().size() > 2;
		
		List<GeoJsonPoint> coordinates = new ArrayList<>(polygonalGeozoneDTO.getCoordinates().size());
		for (int i = 0; i < polygonalGeozoneDTO.getCoordinates().size(); ++i) {
			coordinates.add(new GeoJsonPoint(
					polygonalGeozoneDTO.getCoordinates().get(i)[0],
					polygonalGeozoneDTO.getCoordinates().get(i)[1])
					);
		}
		
		return new PolygonalGeozone(
				polygonalGeozoneDTO.getName(), 
				Constants.POLYGONAL_GEOZONE_TYPE, 
				polygonalGeozoneDTO.getCategory(),
				polygonalGeozoneDTO.getStatus(), 
				new Altitude(
						polygonalGeozoneDTO.getAltitudeLevelLimitInferior(),
						polygonalGeozoneDTO.getAltitudeLimitInferior()
						),
				new Altitude(
						polygonalGeozoneDTO.getAltitudeLevelLimitSuperior(),
						polygonalGeozoneDTO.getAltitudeLimitSuperior()
						),
                coordinates
				);
	}
		
	public Geozone createGeozone(final GeozoneDTO geozoneDTO) throws IllegalGeozoneException {
		assert geozoneDTO != null;
			
		if (geozoneDTO instanceof CircularGeozoneDTO) {	
			return createGeozone((CircularGeozoneDTO) geozoneDTO);
		} 
		if (geozoneDTO instanceof PolygonalGeozoneDTO) {
			return createGeozone((PolygonalGeozoneDTO) geozoneDTO);
		}
		
		throw new RuntimeException("Unknown GeozoneDTO type: this should never happen!");		
    }
		
	private CircularGeozoneDTO createGeozoneDTO(final CircularGeozone circularGeozone) {
		assert circularGeozone != null;
		assert circularGeozone.getAltitudeLimitInferior() != null;
		assert circularGeozone.getCenter() != null;
		
        return new CircularGeozoneDTO(
				circularGeozone.getId(), 
				circularGeozone.getName(), 
				circularGeozone.getCategory(), 
				circularGeozone.getStatus(),
				circularGeozone.getAltitudeLimitInferior().getName(), 
				circularGeozone.getAltitudeLimitInferior().getValue().doubleValue(),
				circularGeozone.getAltitudeLimitSuperior().getName(),
				circularGeozone.getAltitudeLimitSuperior().getValue().doubleValue(),
				circularGeozone.getCenter().getX(), 
				circularGeozone.getCenter().getY(),
				circularGeozone.getRadius()
				);
	}
	
	private PolygonalGeozoneDTO createGeozoneDTO(final PolygonalGeozone polygonalGeozone) {
		assert polygonalGeozone != null;
		assert polygonalGeozone.getAltitudeLimitInferior() != null;
		
		List<double[]> coordinates = new ArrayList<>(polygonalGeozone.getCoordinates().size());
		for (GeoJsonPoint coord : polygonalGeozone.getCoordinates()) {
            coordinates.add(new double[] {coord.getX(), coord.getY()});
		}
		
		return new PolygonalGeozoneDTO(
				polygonalGeozone.getId(), 
				polygonalGeozone.getName(),
				polygonalGeozone.getCategory(), 
				polygonalGeozone.getStatus(), 
				polygonalGeozone.getAltitudeLimitInferior().getName(),
				polygonalGeozone.getAltitudeLimitInferior().getValue().doubleValue(), 
				polygonalGeozone.getAltitudeLimitSuperior().getName(),
				polygonalGeozone.getAltitudeLimitSuperior().getValue().doubleValue(),
				coordinates
				);
	}
	
	public GeozoneDTO createGeozoneDTO(final Geozone geozone) {
		assert geozone != null;
		
		if (geozone instanceof CircularGeozone) {
			return createGeozoneDTO((CircularGeozone) geozone);
        }
		if (geozone instanceof PolygonalGeozone) {
			return createGeozoneDTO((PolygonalGeozone) geozone);
		}
        
		throw new RuntimeException("Unknown Geozone type: this should never happen!");
	}
	
	public GeozoneDTO createExampleGeozoneDTO(Type type) {
		assert type != null;
		GeozoneDTO exampleGeozoneDTO = null;
		
		switch(type.getName()) {
		case Constants.CIRCULAR_GEOZONE_TYPE:
			exampleGeozoneDTO =  new CircularGeozoneDTO(
					"6772c84661b1895d72494ef8", 
					"Your first circular geozone", 
					"RESTRICTED", 
					"ACTIVE", 
					"L3", 
					60.0,
					"L4",
					120.0,
					45.52365, 
					9.21949, 
					100.0
					);
			break;
		case Constants.POLYGONAL_GEOZONE_TYPE:
			exampleGeozoneDTO =  new PolygonalGeozoneDTO(
					"6772c84661b1895d72494ef8", 
					"Your first polygonal geozone", 
					"RESTRICTED", 
					"ACTIVE", 
					"L3", 
					60.0,
					"L4",
					120.0,
					Arrays.asList(
						    new double[]{45.52394, 9.21892},
						    new double[]{45.52347, 9.22014},
						    new double[]{45.52338, 9.22007},
						    new double[]{45.52383, 9.21884}
						    )
					);
		    break;
	    default:
	    	throw new RuntimeException("Unknown Geozone type: this should never happen!");
		}
		
		return exampleGeozoneDTO;
	}
}