import Map, { Marker } from '@vis.gl/react-maplibre';
import PropTypes from 'prop-types';
import Card from '@mui/material/Card';
import SourcePin from '../../Pin/SourcePin';
import DestinationPin from '../../Pin/DestinationPin';
import { getDefaultMapBounds } from '../../../utils/utils';

export default function DroneDrawMap({ drone, handleChange }) {
  return (
    <Card
    variant="outlined"
    sx={{ display: 'flex', flexDirection: 'column', gap: '8px', flexGrow: 1 }}
    >
      <Map
        initialViewState={{
          latitude: drone.source.latitude,
          longitude: drone.source.longitude,
          zoom: 1,
        }}
        maxBounds={ getDefaultMapBounds() }
        mapStyle="https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json"
        style={{ width: '100%', height: '40vh' }}
      >
        <Marker
          latitude={drone.source.latitude}
          longitude={drone.source.longitude}
          draggable
          onDrag={(e) =>
            handleChange('source', { latitude: e.lngLat.lat, longitude: e.lngLat.lng })
          }
        >
          <SourcePin />
        </Marker>
        <Marker
          latitude={drone.destination.latitude}
          longitude={drone.destination.longitude}
          draggable
          onDrag={(e) =>
            handleChange('destination', { latitude: e.lngLat.lat, longitude: e.lngLat.lng })
          }
        >
          <DestinationPin />
        </Marker>
      </Map>
    </Card>
  );
}

DroneDrawMap.propTypes = {
  drone: PropTypes.object.isRequired,
  handleChange: PropTypes.func.isRequired,
};
