import Map, { NavigationControl, FullscreenControl, ScaleControl, Source, Layer } from '@vis.gl/react-maplibre';
import PropTypes from 'prop-types';
import { getGeoZoneColor } from '../../utils/utils';
import { circle } from '@turf/circle';
import 'maplibre-gl/dist/maplibre-gl.css';
import { Card } from '@mui/material';

import { useMapSettings } from '../../hooks/useMapSettings';


function GeoZonesMap({ geoZones }) {

  const { initialViewState, mapBounds, maxPitch, sky } = useMapSettings();

  const geoJsonData = {
    type: 'FeatureCollection',
    features: geoZones.map((zone) => ({
      type: 'Feature',
      geometry: {
        type: 'Polygon',
        coordinates: zone.type === 'CIRCULAR'
          ? circle([zone.longitude, zone.latitude], zone.radius/1000, { steps: 128, units: 'kilometers' }).geometry.coordinates
          : [zone.coordinates],
      },
      properties: {
        id: zone.id,
        color: getGeoZoneColor(zone),
        base_altitude: zone.altitude,
        height: 120,
      },
    })),
  };
  

  return (
    <Card
    variant="outlined"
    sx={{ display: 'flex', flexDirection: 'column', gap: '8px', flexGrow: 1 }}
    >
      <Map
        initialViewState={ initialViewState }
        mapStyle="https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json"
        maxBounds={ mapBounds }
        maxPitch={ maxPitch }
        sky={ sky }
        style={{ width: '100%', height: 'calc(100vh - 77px)' }}
      >
        <FullscreenControl position="top-right" />
        <NavigationControl position="top-right" visualizePitch={true} />

        <ScaleControl />
        <Source id="geoZones" type="geojson" data={geoJsonData}>
          <Layer
            id="geoZones-layer-extrusion"
            type="fill-extrusion"
            paint={{
              'fill-extrusion-color': ['get', 'color'],
              'fill-extrusion-opacity': 0.5,
              'fill-extrusion-height': ['get', 'height'],
              'fill-extrusion-base': ['get', 'base_altitude'],
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
      altitude: PropTypes.number,
      longitude: PropTypes.number,
      latitude: PropTypes.number,
      radius: PropTypes.number,
      coordinates: PropTypes.arrayOf(PropTypes.arrayOf(PropTypes.number)),
    })
  ).isRequired,
};

export default GeoZonesMap;