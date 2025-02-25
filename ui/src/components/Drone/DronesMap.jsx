import { useState, useMemo } from 'react';
import Map, { NavigationControl, FullscreenControl, ScaleControl, Marker, Popup, Source, Layer } from '@vis.gl/react-maplibre';
import PropTypes from 'prop-types';
import SourcePin from '../Pin/SourcePin';
import DestinationPin from '../Pin/DestinationPin';
import SourcePopup from '../MapPopup/SourcePopup';
import DestinationPopup from '../MapPopup/DestinationPopup';
import Card from '@mui/material/Card';
import { getDefaultInitialViewState, getDefaultMapBounds } from '../../utils/utils';

function DronesMap({ drones }) {
  const [popupInfo, setPopupInfo] = useState(null);

  const lineGeoJSON = useMemo(() => {
    return {
      type: "FeatureCollection",
      features: drones.map((drone) => ({
        type: "Feature",
        geometry: {
          type: "LineString",
          coordinates: [
            [drone.source.longitude, drone.source.latitude],
            [drone.destination.longitude, drone.destination.latitude],
          ],
        },
        properties: {
          id: drone.id,
        },
      })),
    };
  }, [drones]);

  const sourcePositionMarkers = useMemo(
    () => drones.map((drone) => (
      <Marker
        key={`marker-${drone.id}-source`}
        longitude={drone.source.longitude}
        latitude={drone.source.latitude}
        anchor="center"
        onClick={(e) => {
          e.originalEvent.stopPropagation();
          setPopupInfo({ type: 'source', data: drone });
        }}
      >
        <SourcePin size={30} />
      </Marker>
    )),
    [drones]
  );

  const destinationPositionMarkers = useMemo(
    () => drones.map((drone) => (
      <Marker
        key={`marker-${drone.id}-destination`}
        longitude={drone.destination.longitude}
        latitude={drone.destination.latitude}
        anchor="center"
        onClick={(e) => {
          e.originalEvent.stopPropagation();
          setPopupInfo({ type: 'destination', data: drone });
        }}
      >
        <DestinationPin size={30} />
      </Marker>
    )),
    [drones]
  );

  return (
    <Card
      variant="outlined"
      sx={{ display: 'flex', flexDirection: 'column', gap: '8px', flexGrow: 1 }}
    >
      <Map
        initialViewState= {getDefaultInitialViewState()}
        mapStyle="https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json"
        maxBounds={getDefaultMapBounds()}
        style={{ width: '100%', height: 'calc(100vh - 77px)' }}
      >
        <FullscreenControl position="top-right" />
        <NavigationControl position="top-right" />
        <ScaleControl />

        {sourcePositionMarkers}
        {destinationPositionMarkers}

        {/* Renderizza le linee GeoJSON */}
        <Source id="lines" type="geojson" data={lineGeoJSON}>
          <Layer
            id="line-layer"
            type="line"
            paint={{
              "line-color": "blue",
              "line-width": 3,
            }}
          />
        </Source>

        {popupInfo && (
          <Popup
            longitude={
              popupInfo.type === 'source'
                ? popupInfo.data.source.longitude
                : popupInfo.type === 'destination'
                ? popupInfo.data.destination.longitude
                : 0
            }
            latitude={

              popupInfo.type === 'source'
                ? popupInfo.data.source.latitude
                : popupInfo.type === 'destination'
                ? popupInfo.data.destination.latitude
                : 0
            }
            onClose={() => setPopupInfo(null)}
          >
            {popupInfo.type === 'source' && <SourcePopup source={popupInfo.data.source} droneName={popupInfo.data.name} />}
            {popupInfo.type === 'destination' && <DestinationPopup destination={popupInfo.data.destination} droneName={popupInfo.data.name} />}
          </Popup>
        )}
      </Map>
    </Card>
  );
}

DronesMap.propTypes = {
  drones: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
      model: PropTypes.string.isRequired,
      operation_category: PropTypes.string.isRequired,
      plan_definition_timestamp: PropTypes.string.isRequired,
      owner: PropTypes.string.isRequired,
      source: PropTypes.shape({
        latitude: PropTypes.number.isRequired,
        longitude: PropTypes.number.isRequired,
      }).isRequired,
      destination: PropTypes.shape({
        latitude: PropTypes.number.isRequired,
        longitude: PropTypes.number.isRequired,
      }).isRequired,
    })
  ).isRequired,
};

export default DronesMap;
