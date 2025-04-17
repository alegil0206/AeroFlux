import { useEffect, useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
} from '@mui/material';
import PropTypes from 'prop-types';
import GeoZoneDetailsForm from './GeoZoneDetailsForm';
import GeoZoneDrawMap from './GeoZoneDrawMap';

import { useMapSettings } from '../../../hooks/useMapSettings';


export default function GeoZoneFormDialog({ onClose, onSave, initialData = null, open }) {

  const { initialViewState, mapBounds, maxPitch, sky } = useMapSettings();

  const defaultGeoZoneArea = {
    latitude: initialViewState.latitude,
    longitude: initialViewState.longitude,
    radius: 5000,
    coordinates: [
      [initialViewState.longitude, initialViewState.latitude],
      [initialViewState.longitude + 0.1, initialViewState.latitude + 0.1],
      [initialViewState.longitude + 0.1, initialViewState.latitude - 0.1],
      [initialViewState.longitude, initialViewState.latitude - 0.01],
    ],
  };

  const getDefaultGeoZone = () => ({
    id: '',
    name: '',
    status: 'ACTIVE',
    category: '',
    type: 'CIRCULAR',
    ...defaultGeoZoneArea,
    altitude_limit_inferior: '',
    altitude_limit_superior: '',
  });

  const [geoZone, setGeoZone] = useState(getDefaultGeoZone());
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (open) {
      setGeoZone(initialData || getDefaultGeoZone());
      setErrors({});
    } else {
      setGeoZone(getDefaultGeoZone());
      setErrors({});
    }
  }, [open, initialData]);


  const handleChange = (name, value) => {
    setGeoZone((prev) => ({ ...prev, [name]: value }));
  };

  const handleTypeChange = (newType) => {
    setGeoZone((prev) => ({
      ...prev,
      type: newType,
      radius: defaultGeoZoneArea.radius,
      latitude: defaultGeoZoneArea.latitude,
      longitude: defaultGeoZoneArea.longitude,
      coordinates: defaultGeoZoneArea.coordinates
    }));
  };

  const validateFields = () => {
    const newErrors = {};
    if (!geoZone.name) newErrors.name = 'Name is required.';
    if (!geoZone.category) newErrors.category = 'Category is required.';
    if (!geoZone.type) newErrors.type = 'Type is required.';
    if (!geoZone.altitude_limit_inferior) newErrors.altitude_limit_inferior = 'Altitude Limit Inferior is required.';
    if (!geoZone.altitude_limit_superior) newErrors.altitude_limit_superior = 'Altitude Limit Superior is required.';

    if (parseFloat(geoZone.altitude_limit_inferior) >= parseFloat(geoZone.altitude_limit_superior)) {
      newErrors.altitude_limit_inferior = 'Altitude Limit Inferior must be less than Altitude Limit Superior.';
      newErrors.altitude_limit_superior = 'Altitude Limit Superior must be greater than Altitude Limit Inferior.';
    }

    if (geoZone.type === 'CIRCULAR') {
      if (geoZone.radius <= 0) newErrors.radius = 'Radius must be greater than 0.';
      if (geoZone.latitude < mapBounds[0][1] || geoZone.latitude > mapBounds[1][1]) {
        newErrors.latitude = `Latitude must be between ${mapBounds[0][1]} and ${mapBounds[1][1]}.`;
      }
      if (geoZone.longitude < mapBounds[0][0] || geoZone.longitude > mapBounds[1][0]) {
        newErrors.longitude = `Longitude must be between ${mapBounds[0][0]} and ${mapBounds[1][0]}.`;
      }
    } else {
      if (geoZone.coordinates.length < 3) newErrors.coordinates = 'Polygon must have at least 3 coordinates.';
      geoZone.coordinates.forEach((coord, idx) => {
        if (coord[1] < mapBounds[0][1] || coord[1] > mapBounds[1][1]) {
          newErrors.coordinates = `Latitude ${idx+1} must be between ${mapBounds[0][1]} and ${mapBounds[1][1]}.`;
        }
        if (coord[0] < mapBounds[0][0] || coord[0] > mapBounds[1][0]) {
          newErrors.coordinates =  `Longitude ${idx+1} must be between ${mapBounds[0][0]} and ${mapBounds[1][0]}.`;
      }});
    };
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSave = () => {
    if (validateFields()) {
      const geoZoneToSave = {
        ...(geoZone.id ? { id: geoZone.id } : {}),
        name: geoZone.name,
        status: geoZone.status,
        category: geoZone.category,
        type: geoZone.type,
        altitude_limit_inferior: parseFloat(geoZone.altitude_limit_inferior),
        altitude_limit_superior: parseFloat(geoZone.altitude_limit_superior),
        ...(geoZone.type === 'CIRCULAR'
          ? { latitude: geoZone.latitude, longitude: geoZone.longitude, radius: geoZone.radius }
          : { coordinates: geoZone.coordinates })
      };
      onSave(geoZoneToSave);
    }
  };

  return (
    <Dialog
      open={open}
      onClose={onClose}
      maxWidth="sm"
      fullWidth
    >
      <DialogTitle>{initialData ? 'Edit GeoZone' : 'Add New GeoZone'}</DialogTitle>
      <DialogContent>
        <GeoZoneDetailsForm geoZone={geoZone} errors={errors} handleChange={handleChange} handleTypeChange={handleTypeChange} />
        <GeoZoneDrawMap geoZone={geoZone} handleChange={handleChange} />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="secondary">
          Cancel
        </Button>
        <Button onClick={() => handleSave()} color="primary">
          Save
        </Button>
      </DialogActions>
    </Dialog>
  );
}

GeoZoneFormDialog.propTypes = {
  onClose: PropTypes.func.isRequired,
  onSave: PropTypes.func.isRequired,
  initialData: PropTypes.object,
  open: PropTypes.bool.isRequired,
};