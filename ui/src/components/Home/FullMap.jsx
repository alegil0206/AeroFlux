import { useState, useMemo, useEffect } from 'react';
import Map, { NavigationControl, FullscreenControl, ScaleControl, Marker, Popup, Source, Layer } from '@vis.gl/react-maplibre';
import PropTypes from 'prop-types';
import DronePin from '../Pin/DronePin';
import SourcePin from '../Pin/SourcePin';
import DestinationPin from '../Pin/DestinationPin';
import DronePopup from '../MapPopup/DronePopup';
import SourcePopup from '../MapPopup/SourcePopup';
import DestinationPopup from '../MapPopup/DestinationPopup';
import SupportPointPin from '../Pin/SupportPointPin';
import SupportPointPopup from '../MapPopup/SupportPointPopup';
import { getGeoZoneColor } from '../../utils/utils';
import { circle } from '@turf/circle';
import { lineOffset } from '@turf/turf';
import 'maplibre-gl/dist/maplibre-gl.css';
import { Card } from '@mui/material';

import { useMapSettings } from '../../hooks/useMapSettings';

function FullMap({ drones, geoZones, weather, supportPoints }) {
  const [popupInfo, setPopupInfo] = useState(null);

  const { initialViewState, mapBounds, maxPitch, sky } = useMapSettings();
  const routePolygonGeoJSON = useMemo(() => ({
    type: 'FeatureCollection',
    features: drones.flatMap((drone) => {
      const flightPlan = drone.status.flightPlan;
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
            id: `${drone.id}-segment-${index}`,
            color: color,
            base_altitude: 0,
            height: height,
          },
        };
      });
    }),
  }), [drones]);

  const routeLineGeoJSON = useMemo(() => ({
    type: 'FeatureCollection',
    features: drones.flatMap((drone) => {
      const flightPlan = drone.status.flightPlan;
      return flightPlan.slice(0, -1).map((point, index) => {
        const nextPoint = flightPlan[index + 1];
        const altitudeDifference = nextPoint.altitude - point.altitude;
        const color = altitudeDifference > 0 ? 'green' : altitudeDifference < 0 ? 'red' : 'blue';
  
        return {
          type: 'Feature',
          geometry: {
            type: 'LineString',
            coordinates: [
              [point.longitude, point.latitude],
              [nextPoint.longitude, nextPoint.latitude],
            ],
          },
          properties: {
            id: `${drone.id}-segment-${index}`,
            color,
          },
        };
      });
    }),
  }), [drones]);
  

  const actualPositionMarkers = useMemo(
    () => drones.map((drone) => (
      <Marker
        key={`marker-${drone.id}`}
        longitude={drone.status.position.longitude}
        latitude={drone.status.position.latitude}
        anchor="center"
        onClick={(e) => {
          e.originalEvent.stopPropagation();
          setPopupInfo({ type: 'drone', data: drone });
        }}
      >
        <DronePin flightMode={drone.status.flightMode} size={30} />
      </Marker>
    )),
    [drones]
  );

  useEffect(() => {
    if (popupInfo && popupInfo.type === 'drone') {
      const updatedDrone = drones.find(d => d.id === popupInfo.data.id);
      if (updatedDrone) {
        setPopupInfo({
          type: 'drone',
          data: updatedDrone,
        });
      }
    }
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

  const supportPointMarkers = useMemo(
    () => supportPoints.map((point) => (
      <Marker
        key={`marker-${point.id}-support`}
        longitude={point.longitude}
        latitude={point.latitude}
        anchor="center"
        onClick={(e) => {
          e.originalEvent.stopPropagation();
          setPopupInfo({ type: 'support', data: point });
        }
        }
      >
        <SupportPointPin size={30} />
      </Marker>
    )), [supportPoints]
  );

  const weatherGeoJsonData = {
    type: "FeatureCollection",
    features: weather.map((cell, index) => ({
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
        base_altitude: zone.altitude_limit_inferior,
        height: zone.altitude_limit_superior,
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
        canvasContextAttributes={{antialias: true}}
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


        <Source id="rain-cells" type="geojson" data={weatherGeoJsonData}>
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

        {/* Renderizza le rotte GeoJSON */}
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

        <Source id="flightplan-lines" type="geojson" data={routeLineGeoJSON}>
          <Layer
            id="flightplan-lines-layer"
            type="line"
            paint={{
              'line-color': ['get', 'color'],
              'line-width': 3,
            }}
          />
        </Source>


        {sourcePositionMarkers}
        {destinationPositionMarkers}
        {actualPositionMarkers}
        {supportPointMarkers}

        {popupInfo && (
          <Popup
            longitude={
              popupInfo.type === 'drone'
                ? popupInfo.data.status.position.longitude
                : popupInfo.type === 'source'
                ? popupInfo.data.source.longitude
                : popupInfo.type === 'destination'
                ? popupInfo.data.destination.longitude
                : popupInfo.type === 'support'
                ? popupInfo.data.longitude
                : 0
            }
            latitude={
              popupInfo.type === 'drone'
                ? popupInfo.data.status.position.latitude
                : popupInfo.type === 'source'
                ? popupInfo.data.source.latitude
                : popupInfo.type === 'destination'
                ? popupInfo.data.destination.latitude
                : popupInfo.type === 'support'
                ? popupInfo.data.latitude
                : 0
            }
            onClose={() => setPopupInfo(null)}
          >
            {popupInfo.type === 'drone' && <DronePopup drone={popupInfo.data} />}
            {popupInfo.type === 'source' && <SourcePopup source={popupInfo.data.source} droneName={popupInfo.data.name} />}
            {popupInfo.type === 'destination' && <DestinationPopup destination={popupInfo.data.destination} droneName={popupInfo.data.name} />}
            {popupInfo.type === 'support' && <SupportPointPopup {...popupInfo.data} />}
          </Popup>
        )}
      </Map>
    </Card>
  );
}

FullMap.propTypes = {
  drones: PropTypes.array.isRequired,
  geoZones: PropTypes.array.isRequired,
  weather: PropTypes.array.isRequired,
};

export default FullMap;
