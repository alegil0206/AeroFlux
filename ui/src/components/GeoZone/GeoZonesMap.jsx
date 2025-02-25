import Map, { NavigationControl, FullscreenControl, ScaleControl, Source, Layer } from '@vis.gl/react-maplibre';
import PropTypes from 'prop-types';
import { getGeoZoneColor, getDefaultInitialViewState, getDefaultMapBounds } from '../../utils/utils';
import { circle } from '@turf/circle';
import 'maplibre-gl/dist/maplibre-gl.css';
import { Card } from '@mui/material';


function GeoZonesMap({ geoZones }) {

  const geoJsonData = {
    type: 'FeatureCollection',
    features: geoZones.map((zone) => ({
      type: 'Feature',
      geometry: {
        type: 'Polygon',
        coordinates: zone.type === 'CIRCULAR'
          ? circle([zone.longitude, zone.latitude], zone.radius, { steps: 128, units: 'kilometers' }).geometry.coordinates
          : [zone.coordinates],
      },
      properties: {
        id: zone.id,
        color: getGeoZoneColor(zone),
      },
    })),
  };
  

  return (
    <Card
    variant="outlined"
    sx={{ display: 'flex', flexDirection: 'column', gap: '8px', flexGrow: 1 }}
    >
      <Map
      initialViewState={ getDefaultInitialViewState() }
      mapStyle="https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json"
      maxBounds={ getDefaultMapBounds() }
      style={{ width: '100%', height: 'calc(100vh - 77px)' }}
    >
      <FullscreenControl position="top-right" />
      <NavigationControl position="top-right" />
      <ScaleControl />
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
    </Map>
  </Card>
  );
}

GeoZonesMap.propTypes = {
  geoZones: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      type: PropTypes.oneOf(['CIRCULAR', 'POLYGONAL']).isRequired,
      status: PropTypes.string.isRequired,
      category: PropTypes.string.isRequired,
      longitude: PropTypes.number,
      latitude: PropTypes.number,
      radius: PropTypes.number,
      coordinates: PropTypes.arrayOf(PropTypes.arrayOf(PropTypes.number)),
    })
  ).isRequired,
};

export default GeoZonesMap;