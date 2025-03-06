import { useState, useMemo } from 'react';
import Map, { NavigationControl, FullscreenControl, ScaleControl, Marker, Popup, Source, Layer } from '@vis.gl/react-maplibre';
import PropTypes from 'prop-types';
import DronePin from '../Pin/DronePin';
import SourcePin from '../Pin/SourcePin';
import DestinationPin from '../Pin/DestinationPin';
import DronePopup from '../MapPopup/DronePopup';
import SourcePopup from '../MapPopup/SourcePopup';
import DestinationPopup from '../MapPopup/DestinationPopup';
import { getGeoZoneColor } from '../../utils/utils';
import { getInitialViewState, getMapBounds } from '../../utils/mapSettings';
import { circle } from '@turf/circle';
import 'maplibre-gl/dist/maplibre-gl.css';
import { Card } from '@mui/material';

function FullMap({ drones, geoZones }) {
  const [popupInfo, setPopupInfo] = useState(null);

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

  const actualPositionMarkers = useMemo(
    () => drones.map((drone) => (
      <Marker
        key={`marker-${drone.id}`}
        longitude={drone.position.longitude}
        latitude={drone.position.latitude}
        anchor="center"
        onClick={(e) => {
          e.originalEvent.stopPropagation();
          setPopupInfo({ type: 'drone', data: drone });
        }}
      >
        <DronePin height={drone.position.height} size={30} />
      </Marker>
    )),
    [drones]
  );

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
        initialViewState={ getInitialViewState() } 
        mapStyle="https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json"
        maxBounds={ getMapBounds() }
        style={{ width: '100%', height: 'calc(100vh - 77px)' }}
      >
        <FullscreenControl position="top-right" />
        <NavigationControl position="top-right" />
        <ScaleControl />

        <Source id="geoZones" type="geojson" data={geoZoneGeoJsonData}>
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
        {actualPositionMarkers}

        {popupInfo && (
          <Popup
            longitude={
              popupInfo.type === 'drone'
                ? popupInfo.data.position.longitude
                : popupInfo.type === 'source'
                ? popupInfo.data.source.longitude
                : popupInfo.type === 'destination'
                ? popupInfo.data.destination.longitude
                : 0
            }
            latitude={
              popupInfo.type === 'drone'
                ? popupInfo.data.position.latitude
                : popupInfo.type === 'source'
                ? popupInfo.data.source.latitude
                : popupInfo.type === 'destination'
                ? popupInfo.data.destination.latitude
                : popupInfo.data.latitude
            }
            onClose={() => setPopupInfo(null)}
          >
            {popupInfo.type === 'drone' && <DronePopup drone={popupInfo.data} />}
            {popupInfo.type === 'source' && <SourcePopup source={popupInfo.data.source} droneName={popupInfo.data.name} />}
            {popupInfo.type === 'destination' && <DestinationPopup destination={popupInfo.data.destination} droneName={popupInfo.data.name} />}
          </Popup>
        )}
      </Map>
    </Card>
  );
}

FullMap.propTypes = {
  drones: PropTypes.array.isRequired,
  geoZones: PropTypes.array.isRequired,
};

export default FullMap;
