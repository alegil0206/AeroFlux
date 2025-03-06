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
import { getInitialViewState, getMapBounds } from '../../../utils/mapSettings';


export default function DroneFormDialog({ onClose, onSave, initialData = null, open }) {

  const defaultMapCenter = getInitialViewState();

  const getDefaultDrone = () => ({
    id: '',
    name: '',
    model: '',
    operation_category: 'SPECIFIC',
    owner: '',
    source: { latitude: defaultMapCenter.latitude - 0.01, longitude: defaultMapCenter.longitude - 0.01 },
    destination: { latitude: defaultMapCenter.latitude + 0.01, longitude: defaultMapCenter.longitude + 0.01 },
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

    const bounds = getMapBounds();
    
    const validateCoordinates = (coord, type) => {
      if (coord.latitude < bounds[0][1] || coord.latitude > bounds[1][1]) {
        newErrors[`${type}_latitude`] = `Latitude must be between ${bounds[0][1]} and ${bounds[1][1]}.`;
      }
      if (coord.longitude < bounds[0][0] || coord.longitude > bounds[1][0]) {
        newErrors[`${type}_longitude`] = `Longitude must be between ${bounds[0][0]} and ${bounds[1][0]}.`;
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
