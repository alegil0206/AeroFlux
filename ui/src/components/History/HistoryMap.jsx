import { useState, useMemo } from 'react';
import Map, { NavigationControl, FullscreenControl, ScaleControl, Marker, Popup, Source, Layer } from '@vis.gl/react-maplibre';
import PropTypes from 'prop-types';
import SourcePin from '../Pin/SourcePin';
import DestinationPin from '../Pin/DestinationPin';
import DronePin from '../Pin/DronePin';
import SourcePopup from '../MapPopup/SourcePopup';
import DestinationPopup from '../MapPopup/DestinationPopup';
import DronePopup from '../MapPopup/DronePopup';
import { lineOffset } from '@turf/turf';
import 'maplibre-gl/dist/maplibre-gl.css';
import { Card } from '@mui/material';

import { useMapSettings } from '../../hooks/useMapSettings';

function HistoryMap({ simulation }) {
  const [popupInfo, setPopupInfo] = useState(null);

  const { initialViewState, mapBounds, maxPitch, sky } = useMapSettings();
  const routePolygonGeoJSON = useMemo(() => ({
    type: 'FeatureCollection',
    features: simulation.drones.flatMap((drone) => {
      const status = drone.positions[drone.positions.length - 1];
      const flightPlan = status.flightPlan;
      return flightPlan.slice(0, -1).map((point, index) => {
        const nextPoint = flightPlan[index + 1];
        const altitudeDifference = nextPoint.altitude - point.altitude;
        const color = altitudeDifference > 0 ? 'green' : altitudeDifference < 0 ? 'red' : 'blue';
        const height = Math.max(point.altitude, nextPoint.altitude);
  
        const line = {
          type: 'Feature',
          geometry: {
            type: 'LineString',
            coordinates: [
              [point.longitude, point.latitude],
              [nextPoint.longitude, nextPoint.latitude],
            ],
          },
        };
  
        const offsetLineLeft = lineOffset(line, 10, { units: 'meters' });
        const offsetLineRight = lineOffset(line, -10, { units: 'meters' });
  
        const coordinates = [
          ...offsetLineLeft.geometry.coordinates,
          ...offsetLineRight.geometry.coordinates.reverse(),
          offsetLineLeft.geometry.coordinates[0],
        ];

        return {
          type: 'Feature',
          geometry: {
            type: 'Polygon',
            coordinates: [coordinates],
          },
          properties: {
            id: `${status.id}-segment-${index}`,
            color: color,
            base_altitude: 0,
            height: height,
          },
        };
      });
    }),
  }), [simulation]);
  
  const linearRouteGeoJSON = useMemo(() => {
    return {
      type: "FeatureCollection",
      features: simulation.drones.map((drone) => ({
        type: "Feature",
        geometry: {
          type: "LineString",
          coordinates: [
            [drone.droneProperties.source.longitude, drone.droneProperties.source.latitude],
            [drone.droneProperties.destination.longitude, drone.droneProperties.destination.latitude],
          ],
        },
        properties: {
          id: drone.droneProperties.id,
        },
      })),
    };
  }, [simulation]);

  const actualPositionMarkers = useMemo(
    () => simulation.drones.map((drone) => {
      const stepData = drone.positions[drone.positions.length - 1];
      return (
        <Marker
          key={`marker-${drone.droneProperties.id}-actual`}
          longitude={stepData.position.longitude}
          latitude={stepData.position.latitude}
          anchor="center"
          onClick={(e) => {
            e.originalEvent.stopPropagation();
            setPopupInfo({ type: 'drone', data: {...drone.droneProperties, status: stepData } });
          }}
        >
          <DronePin flightMode={stepData.flightMode} size={30} />
        </Marker>
      );
    }),
    [simulation]
  );

  const sourcePositionMarkers = useMemo(
    () => simulation.drones.map((drone) => (
      <Marker
        key={`marker-${drone.droneProperties.id}-source`}
        longitude={drone.droneProperties.source.longitude}
        latitude={drone.droneProperties.source.latitude}
        anchor="center"
        onClick={(e) => {
          e.originalEvent.stopPropagation();
          setPopupInfo({ type: 'source', data: drone.droneProperties });
        }}
      >
        <SourcePin size={30} />
      </Marker>
    )),
    [simulation]
  );

  const destinationPositionMarkers = useMemo(
    () => simulation.drones.map((drone) => (
      <Marker
        key={`marker-${drone.droneProperties.id}-destination`}
        longitude={drone.droneProperties.destination.longitude}
        latitude={drone.droneProperties.destination.latitude}
        anchor="center"
        onClick={(e) => {
          e.originalEvent.stopPropagation();
          setPopupInfo({ type: 'destination', data: drone.droneProperties });
        }}
      >
        <DestinationPin size={30} />
      </Marker>
    )),
    [simulation]
  );
  
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
        canvasContextAttributes={{antialias: true}}
      >
        <FullscreenControl position="top-right" />
        <NavigationControl position="top-right" visualizePitch={true} />
        <ScaleControl />
     
        <Source id="lines" type="geojson" data={linearRouteGeoJSON}>
          <Layer
            id="line-layer"
            type="line"
            paint={{
              "line-color": "green",
              "line-width": 3,
            }}
          />
        </Source>

        <Source id="routes" type="geojson" data={routePolygonGeoJSON}>
          <Layer
            id="routes-layer-extrusion"
            type="fill-extrusion"
            paint={{
              'fill-extrusion-color': ['get', 'color'],
              'fill-extrusion-opacity': 0.6,
              'fill-extrusion-height': ['get', 'height'],
              'fill-extrusion-base': ['get', 'base_altitude']
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
                ? popupInfo.data.status.position.longitude
                : popupInfo.type === 'source'
                ? popupInfo.data.source.longitude
                : popupInfo.type === 'destination'
                ? popupInfo.data.destination.longitude
                : 0
            }
            latitude={
              popupInfo.type === 'drone'
                ? popupInfo.data.status.position.latitude
                : popupInfo.type === 'source'
                ? popupInfo.data.source.latitude
                : popupInfo.type === 'destination'
                ? popupInfo.data.destination.latitude
                : 0
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

HistoryMap.propTypes = {
  simulation: PropTypes.array.isRequired,
};

export default HistoryMap;
