import MapboxDraw from '@mapbox/mapbox-gl-draw';
import {useControl} from '@vis.gl/react-maplibre';
import PropTypes from 'prop-types'; 

MapboxDraw.constants.classes.CONTROL_BASE = 'maplibregl-ctrl';
MapboxDraw.constants.classes.CONTROL_PREFIX = 'maplibregl-ctrl-';
MapboxDraw.constants.classes.CONTROL_GROUP = 'maplibregl-ctrl-group';

export default function DrawControl(props) {
  const draw = new MapboxDraw({
    ...props,
    styles: [
      // ACTIVE (being drawn)
      // line stroke
      {
          "id": "gl-draw-line",
          "type": "line",
          "filter": ["all", ["==", "$type", "LineString"]],
          "layout": {
            "line-cap": "round",
            "line-join": "round"
          },
          "paint": {
            "line-color": "#D20C0C",
            "line-dasharray": [0.2, 2],
            "line-width": 2
          }
      },
      // polygon fill
      {
        "id": "gl-draw-polygon-fill",
        "type": "fill",
        "filter": ["all", ["==", "$type", "Polygon"]],
        "paint": {
          "fill-color": "#D20C0C",
          "fill-outline-color": "#D20C0C",
          "fill-opacity": 0.1
        }
      },
      // polygon mid points
      {
        'id': 'gl-draw-polygon-midpoint',
        'type': 'circle',
        'filter': ['all',
          ['==', '$type', 'Point'],
          ['==', 'meta', 'midpoint']],
        'paint': {
          'circle-radius': 3,
          'circle-color': '#D20C0C'
        }
      },
      // polygon outline stroke
      // This doesn't style the first edge of the polygon, which uses the line stroke styling instead
      {
        "id": "gl-draw-polygon-stroke-active",
        "type": "line",
        "filter": ["all", ["==", "$type", "Polygon"]],
        "layout": {
          "line-cap": "round",
          "line-join": "round"
        },
        "paint": {
          "line-color": "#D20C0C",
          "line-dasharray": [0.2, 2],
          "line-width": 2
        }
      },
      // vertex point halos
      {
        "id": "gl-draw-polygon-and-line-vertex-halo-active",
        "type": "circle",
        "filter": ["all", ["==", "meta", "vertex"], ["==", "$type", "Point"]],
        "paint": {
          "circle-radius": 5,
          "circle-color": "#FFF"
        }
      },
      // vertex points
      {
        "id": "gl-draw-polygon-and-line-vertex-active",
        "type": "circle",
        "filter": ["all", ["==", "meta", "vertex"], ["==", "$type", "Point"]],
        "paint": {
          "circle-radius": 3,
          "circle-color": "#D20C0C",
        }
      },
    ]  
    });

  useControl(
    () => draw,
    ({map}) => {
      map.on('draw.create', props.onCreate);
      map.on('draw.update', props.onUpdate);
      map.on('draw.delete', props.onDelete);

      if (props.initialGeoJson) {
        if (draw.getAll().features.length === 0) {
          draw.add(props.initialGeoJson);
        }
        }
    },
    ({map}) => {
      map.off('draw.create', props.onCreate);
      map.off('draw.update', props.onUpdate);
      map.off('draw.delete', props.onDelete);
    },
    {
      position: props.position
    }
  );

  return null;
}

DrawControl.propTypes = {
  position: PropTypes.oneOf(['top-left', 'top-right', 'bottom-left', 'bottom-right']),
  onCreate: PropTypes.func,
  onUpdate: PropTypes.func,
  onDelete: PropTypes.func,
  controls: PropTypes.object,
  defaultMode: PropTypes.string,
  initialGeoJson: PropTypes.object
};
