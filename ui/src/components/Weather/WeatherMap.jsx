import Map, { NavigationControl, FullscreenControl, ScaleControl, Source, Layer } from '@vis.gl/react-maplibre';
import PropTypes from 'prop-types';
import Card from '@mui/material/Card';
import 'maplibre-gl/dist/maplibre-gl.css';

import { useMapSettings } from '../../hooks/useMapSettings';

export default function WeatherMap({ weatherData }) {

  const { initialViewState, mapBounds, maxPitch, sky } = useMapSettings();

  const geoJsonData = {
    type: "FeatureCollection",
    features: weatherData.map((cell, index) => ({
      type: "Feature",
      geometry: {
        type: "Polygon",
        coordinates: [
          cell.coordinates.map(([lat, lon]) => [lon, lat]) 
        ]
      },
      properties: { id: index }
    }))
  };

  return (
    <Card
    variant="outlined"
    sx={{ display: 'flex', flexDirection: 'column', gap: '8px', flexGrow: 1 }}
    >
      <Map
        initialViewState={ initialViewState }
        maxBounds={ mapBounds }
        maxPitch={ maxPitch }
        sky={ sky }
        mapStyle="https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json"
        style={{ width: '100%', height: 'calc(100vh - 77px)' }}
      >
        <FullscreenControl position="top-right" />
        <NavigationControl position="top-right" visualizePitch={true} />

        <ScaleControl />


        <Source id="rain-cells" type="geojson" data={geoJsonData}>
        <Layer
            id="rain-layer-extrusion"
            type="fill-extrusion"
            paint={{
              'fill-extrusion-color': '#007AFF',
              'fill-extrusion-opacity': 0.5,
              'fill-extrusion-height': 130,
              'fill-extrusion-base': 0,
            }}
          />
        </Source>

      </Map>
    </Card>
  );
}

WeatherMap.propTypes = {
  weatherData: PropTypes.object.isRequired,
};