import { Map, NavigationControl, FullscreenControl, ScaleControl } from '@vis.gl/react-maplibre';
import PropTypes from 'prop-types';
import Card from '@mui/material/Card';
import 'maplibre-gl/dist/maplibre-gl.css';
import '@maplibre/maplibre-gl-geocoder/dist/maplibre-gl-geocoder.css';
import { useEffect, useRef } from 'react';

import GeocoderControl from './geocoder-control';
import { Marker } from '@vis.gl/react-maplibre';


export default function SettingMap({ coordinates, onCoordinatesChange }) {

  const mapRef = useRef();

  useEffect(() => {
    mapRef.current?.flyTo({center: [coordinates.longitude, coordinates.latitude], duration: 2000});
  }, [coordinates]);

  return (
    <Card
    variant="outlined"
    sx={{ display: 'flex', flexDirection: 'column', gap: '8px', flexGrow: 1 }}
    >
      <Map
        ref = {mapRef}
        initialViewState={{
          latitude: coordinates.latitude,
          longitude: coordinates.longitude,
          zoom: 10,
        }}        
        mapStyle="https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json"
        style={{ width: '100%', height: 'calc(50vh)' }}
      >
        <FullscreenControl position="top-right" />
        <NavigationControl position="top-right" visualizePitch={true} />
        <ScaleControl />
        <GeocoderControl position="top-left"
          coordinates={coordinates}
          onResult={(newCoordinates) => {
              onCoordinatesChange(newCoordinates);
            }
          }
        />
        <Marker longitude={coordinates.longitude} latitude={coordinates.latitude}/>
      </Map>
    </Card>
  );
}

SettingMap.propTypes = {
  coordinates: PropTypes.object.isRequired,
  onCoordinatesChange: PropTypes.func.isRequired,
};