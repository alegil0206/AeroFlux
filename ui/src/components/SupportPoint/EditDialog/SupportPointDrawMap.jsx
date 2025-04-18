import Map, { Marker } from '@vis.gl/react-maplibre';
import PropTypes from 'prop-types';
import Card from '@mui/material/Card';
import SupportPointPin from '../../Pin/SupportPointPin';

import { useMapSettings } from '../../../hooks/useMapSettings';

export default function SupportPointDrawMap({ supportPoint, handleChange }) {

    const { initialViewState, mapBounds, maxPitch, sky } = useMapSettings();
    
    return (
        <Card
        variant="outlined"
        sx={{ display: 'flex', flexDirection: 'column', gap: '8px', flexGrow: 1 }}
        >
        <Map
            initialViewState={initialViewState}
            maxBounds={mapBounds}
            maxPitch={maxPitch}
            sky={sky}
            mapStyle="https://basemaps.cartocdn.com/gl/voyager-gl-style/style.json"
            style={{ width: '100%', height: '40vh' }}
        >
            <Marker
                latitude={supportPoint.latitude}
                longitude={supportPoint.longitude}
                draggable
                onDrag={(e) => {
                    handleChange('latitude', e.lngLat.lat); 
                    handleChange('longitude', e.lngLat.lng);
                    }
                }
            >
                <SupportPointPin />
            </Marker>
        </Map>
        </Card>
    );
}

SupportPointDrawMap.propTypes = {
    supportPoint: PropTypes.object.isRequired,
    handleChange: PropTypes.func.isRequired,
};