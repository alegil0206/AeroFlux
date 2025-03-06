import Map, { NavigationControl, FullscreenControl, ScaleControl, Source, Layer } from '@vis.gl/react-maplibre';
import PropTypes from 'prop-types';
import { getInitialViewState, getMapBounds } from '../../utils/mapSettings';
import Card from '@mui/material/Card';
import 'maplibre-gl/dist/maplibre-gl.css';

export default function WeatherMap({ weatherData }) {

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
        initialViewState={ getInitialViewState() }
        maxBounds={ getMapBounds() }
        mapStyle="https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json"
        style={{ width: '100%', height: 'calc(100vh - 77px)' }}
      >
        <FullscreenControl position="top-right" />
        <NavigationControl position="top-right" />
        <ScaleControl />


        <Source id="rain-cells" type="geojson" data={geoJsonData}>
          <Layer
            id="rain-layer"
            type="fill"
            paint={{
              'fill-color': '#007AFF',
              'fill-opacity': 0.5
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