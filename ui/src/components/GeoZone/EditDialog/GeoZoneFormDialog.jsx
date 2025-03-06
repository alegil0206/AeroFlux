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
import { getInitialViewState, getMapBounds } from '../../../utils/mapSettings';


export default function GeoZoneFormDialog({ onClose, onSave, initialData = null, open }) {

  const defaultMapCenter = getInitialViewState();

  const defaultGeoZoneArea = {
    latitude: defaultMapCenter.latitude,
    longitude: defaultMapCenter.longitude,
    radius: 20,
    coordinates: [
      [defaultMapCenter.longitude, defaultMapCenter.latitude],
      [defaultMapCenter.longitude + 0.1, defaultMapCenter.latitude + 0.1],
      [defaultMapCenter.longitude + 0.1, defaultMapCenter.latitude - 0.1],
      [defaultMapCenter.longitude, defaultMapCenter.latitude - 0.01],
    ],
  };

  const getDefaultGeoZone = () => ({
    id: '',
    name: '',
    status: 'ACTIVE',
    category: '',
    type: 'CIRCULAR',
    ...defaultGeoZoneArea,
    altitude_level: '',
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
    const bounds = getMapBounds();
    if (!geoZone.name) newErrors.name = 'Name is required.';
    if (!geoZone.category) newErrors.category = 'Category is required.';
    if (!geoZone.type) newErrors.type = 'Type is required.';
    if (!geoZone.altitude_level) newErrors.altitude_level = 'Altitude level is required.';

    if (geoZone.type === 'CIRCULAR') {
      if (geoZone.radius <= 0) newErrors.radius = 'Radius must be greater than 0.';
      if (geoZone.latitude < bounds[0][1] || geoZone.latitude > bounds[1][1]) {
        newErrors.latitude = `Latitude must be between ${bounds[0][1]} and ${bounds[1][1]}.`;
      }
      if (geoZone.longitude < bounds[0][0] || geoZone.longitude > bounds[1][0]) {
        newErrors.longitude = `Longitude must be between ${bounds[0][0]} and ${bounds[1][0]}.`;
      }
    } else {
      if (geoZone.coordinates.length < 3) newErrors.coordinates = 'Polygon must have at least 3 coordinates.';
      geoZone.coordinates.forEach((coord, idx) => {
        if (coord[1] < bounds[0][1] || coord[1] > bounds[1][1]) {
          newErrors.coordinate = `Latitude ${idx+1} must be between ${bounds[0][1]} and ${bounds[1][1]}.`;
        }
        if (coord[0] < bounds[0][0] || coord[0] > bounds[1][0]) {
          newErrors.coordinates =  `Longitude ${idx+1} must be between ${bounds[0][0]} and ${bounds[1][0]}.`;
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
        altitude_level: geoZone.altitude_level,
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