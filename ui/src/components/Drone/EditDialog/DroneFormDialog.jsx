import { useState, useEffect } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
} from '@mui/material';
import PropTypes from 'prop-types';
import DroneDetailsForm from './DroneDetailsForm';
import DroneDrawMap from './DroneDrawMap';

import { useMapSettings } from '../../../hooks/useMapSettings';

export default function DroneFormDialog({ onClose, onSave, initialData = null, open }) {

  const { initialViewState, mapBounds } = useMapSettings();

  const getDefaultDrone = () => ({
    id: '',
    name: '',
    model: '',
    operation_category: 'SPECIFIC',
    owner: '',
    flight_autonomy: 0,
    adaptive_capabilities: {
      safe_landing: false,
      collision_avoidance: false,
      geo_awareness: false,
      auto_authorization: false,
      flight_autonomy_management: false,
    },
    source: { latitude: initialViewState.latitude - 0.01, longitude: initialViewState.longitude - 0.01 },
    destination: { latitude: initialViewState.latitude + 0.01, longitude: initialViewState.longitude + 0.01 },
  });

  const [drone, setDrone] = useState(getDefaultDrone());
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (open) {
      setDrone(initialData || getDefaultDrone());
      setErrors({});
    } else {
      setDrone(getDefaultDrone());
      setErrors({});
    }
  }, [open, initialData]);

  const handleChange = (field, value) => {
    setDrone((prev) => ({
      ...prev,
      [field]: typeof value === 'object' ? { ...prev[field], ...value } : value,
    }));
  };

  const validateFields = () => {
    const newErrors = {};

    if (!drone.name) newErrors.name = 'Name is required.';
    if (!drone.model) newErrors.model = 'Model is required.';
    if (!drone.owner) newErrors.owner = 'Owner is required.';
    if (drone.flight_autonomy <= 0) newErrors.flight_autonomy = 'Flight autonomy must be greater than 0.';
    const validateCoordinates = (coord, type) => {
      if (coord.latitude < mapBounds[0][1] || coord.latitude > mapBounds[1][1]) {
        newErrors[`${type}_latitude`] = `Latitude must be between ${mapBounds[0][1]} and ${mapBounds[1][1]}.`;
      }
      if (coord.longitude < mapBounds[0][0] || coord.longitude > mapBounds[1][0]) {
        newErrors[`${type}_longitude`] = `Longitude must be between ${mapBounds[0][0]} and ${mapBounds[1][0]}.`;
      }
    };

    validateCoordinates(drone.source, 'source');
    validateCoordinates(drone.destination, 'destination');

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSave = () => {
    if (validateFields()) {
      onSave({ ...drone });
    }
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
      <DialogTitle>{initialData ? 'Edit Drone' : 'Add New Drone'}</DialogTitle>
      <DialogContent>
        <DroneDetailsForm
          drone={drone}
          errors={errors}
          handleChange={handleChange}
        />
        <DroneDrawMap
          drone={drone}
          handleChange={handleChange}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose} color="secondary">
          Cancel
        </Button>
        <Button onClick={handleSave} color="primary">
          Save
        </Button>
      </DialogActions>
    </Dialog>
  );
}

DroneFormDialog.propTypes = {
  onClose: PropTypes.func.isRequired,
  onSave: PropTypes.func.isRequired,
  initialData: PropTypes.object,
  open: PropTypes.bool.isRequired,
};
