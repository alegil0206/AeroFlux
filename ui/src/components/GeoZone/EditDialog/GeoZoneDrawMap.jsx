import Map, { NavigationControl, FullscreenControl, ScaleControl, Source, Layer, Marker } from '@vis.gl/react-maplibre';
import PropTypes from 'prop-types';
import { getGeoZoneColor } from '../../../utils/utils';
import { getInitialViewState, getMapBounds } from '../../../utils/mapSettings';
import { circle } from '@turf/circle';
import { Card } from '@mui/material';
import { useCallback } from 'react';
import DrawControl from './draw-control';
import '@mapbox/mapbox-gl-draw/dist/mapbox-gl-draw.css';
import 'maplibre-gl/dist/maplibre-gl.css';

function GeoZoneDrawMap({ geoZone, handleChange }) {

  const onMarkerDrag = useCallback((event) => {
    handleChange('latitude', event.lngLat.lat);
    handleChange('longitude', event.lngLat.lng);
  }, []);

  const geoJsonData = {
    type: 'Feature',
    geometry: {
      type: 'Polygon',
      coordinates: geoZone.type === 'CIRCULAR' && geoZone.latitude && geoZone.longitude && geoZone.radius
      ? circle([geoZone.longitude, geoZone.latitude], geoZone.radius, { steps: 128, units: 'kilometers' }).geometry.coordinates
      : geoZone.type === 'POLYGONAL' && geoZone.coordinates.length > 0
      ? [geoZone.coordinates]    
      : []    
    },
        properties: {    
      id: geoZone.id,
      color: getGeoZoneColor(geoZone),
    },
    
  };

  const extractPolygonCoordinates = (feature) => {
    if (feature.geometry?.type === 'Polygon') {
      return feature.geometry.coordinates[0];
    }
    return [];
  };

  const onUpdatePolygon = useCallback(
    (e) => {
      for (const f of e.features) {
        const vertices = extractPolygonCoordinates(f);
        handleChange('coordinates', vertices);
      }
    },
    [handleChange]
  );

  const onDeletePolygon = useCallback(
    () => {
      handleChange('coordinates', []);
    },
    [handleChange]
  );


  return (
    <Card
    variant="outlined"
    sx={{ display: 'flex', flexDirection: 'column', gap: '8px', flexGrow: 1 }}
    >
      <Map
      initialViewState={ getInitialViewState() }
      mapStyle="https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json"
      maxBounds={ getMapBounds() }
      style={{ width: '100%', height: 'calc(40vh)' }}
    >
      <FullscreenControl position="top-right" />
      <NavigationControl position="top-right" />
      <ScaleControl />

      { geoZone.type === 'CIRCULAR' && (
        <>
          <Source id="geoZones" type="geojson" data={geoJsonData}>
            <Layer 
              id="geoZones-layer-lines"
              type="line"
              paint={{
                'line-color': ['get', 'color'],
                'line-width': 2,
              }}
            />
            <Layer
              id="geoZones-layer-fill"
              type="fill"
              paint={{
                'fill-color': ['get', 'color'],
                'fill-opacity': 0.5,
              }}
            />
          </Source>
          <Marker
              longitude={geoZone.longitude}
              latitude={geoZone.latitude}
              draggable
              onDrag={onMarkerDrag}
            >
          </Marker>        
        </>
      )}
      
      { geoZone.type === 'POLYGONAL' && (
        
        <DrawControl
          position="top-left"
          displayControlsDefault={false}
          controls={{
            polygon: true,
            trash: true,
          }}
          defaultMode="simple_select"
          onCreate={onUpdatePolygon}
          onUpdate={onUpdatePolygon}
          onDelete={onDeletePolygon}
          initialGeoJson={geoJsonData}
        />
      )}
    </Map>
    
  </Card>
  );
}

GeoZoneDrawMap.propTypes = {
    geoZone: PropTypes.shape({
      id: PropTypes.string.isRequired,
      type: PropTypes.oneOf(['CIRCULAR', 'POLYGONAL']).isRequired,
      status: PropTypes.string.isRequired,
      category: PropTypes.string.isRequired,
      longitude: PropTypes.number,
      latitude: PropTypes.number,
      radius: PropTypes.number,
      coordinates: PropTypes.arrayOf(PropTypes.arrayOf(PropTypes.number))
    }).isRequired,
    handleChange: PropTypes.func.isRequired
};

export default GeoZoneDrawMap;