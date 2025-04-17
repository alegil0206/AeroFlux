import { useState, useMemo } from 'react';
import Map, { NavigationControl, FullscreenControl, ScaleControl, Marker, Popup, Source, Layer } from '@vis.gl/react-maplibre';
import PropTypes from 'prop-types';
import SourcePin from '../Pin/SourcePin';
import DestinationPin from '../Pin/DestinationPin';
import SourcePopup from '../MapPopup/SourcePopup';
import DestinationPopup from '../MapPopup/DestinationPopup';
import { getGeoZoneColor } from '../../utils/utils';
import { circle } from '@turf/circle';
import 'maplibre-gl/dist/maplibre-gl.css';
import { Card } from '@mui/material';

import { useMapSettings } from '../../hooks/useMapSettings';

function AuthorizationMap({ drones, geoZones }) {
  const [popupInfo, setPopupInfo] = useState(null);
  const { initialViewState, mapBounds, maxPitch, sky } = useMapSettings();

  const routeLineGeoJSON = useMemo(() => ({
    type: 'FeatureCollection',
    features: drones.map((drone) => ({
      type: 'Feature',
      geometry: {
        type: 'LineString',
        coordinates: [
          [drone.source.longitude, drone.source.latitude],
          [drone.destination.longitude, drone.destination.latitude],
        ],
      },
      properties: {
        id: drone.id,
      },
    })),
  }), [drones]);

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

  const geoZoneGeoJsonData = {
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

        <Source id="geoZones" type="geojson" data={geoZoneGeoJsonData}>
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

        {/* Renderizza le rotte GeoJSON */}
        <Source id="lines" type="geojson" data={routeLineGeoJSON}>
          <Layer
            id="line-layer"
            type="line"
            paint={{
              "line-color": "blue",
              "line-width": 3,
            }}
          />
        </Source>

        {sourcePositionMarkers}
        {destinationPositionMarkers}

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
                : popupInfo.data.latitude
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

AuthorizationMap.propTypes = {
  drones: PropTypes.array.isRequired,
  geoZones: PropTypes.array.isRequired,
};

export default AuthorizationMap;
