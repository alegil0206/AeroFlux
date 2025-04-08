import {
  TextField,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Button,
  FormHelperText,
} from '@mui/material';
import PropTypes from 'prop-types';
import { useState, useEffect } from 'react';

export default function GeoZoneDetailsForm({ geoZone, errors, handleChange, handleTypeChange }) {

  const [coordinatesInput, setCoordinatesInput] = useState(JSON.stringify(geoZone.coordinates));

  useEffect(() => {
    setCoordinatesInput(JSON.stringify(geoZone.coordinates));
  }, [geoZone.coordinates]);

  const onTypeChange = (e) => {
    const newType = e.target.value;
    handleTypeChange(newType);
  };

  return (
    <div>
      <FormControl fullWidth margin="normal" error={!!errors.name}>
        <TextField
          label="Name"
          name="name"
          fullWidth
          value={geoZone.name}
          onChange={(e) => handleChange(e.target.name, e.target.value)}
        />
        <FormHelperText>{errors.name}</FormHelperText>
      </FormControl>
      <FormControl fullWidth margin="normal" error={!!errors.category}>
        <InputLabel>Category</InputLabel>
        <Select value={geoZone.category} name="category" onChange={(e) => handleChange(e.target.name, e.target.value)} >
          <MenuItem value="RESTRICTED">Restricted</MenuItem>
          <MenuItem value="EXCLUDED">Excluded</MenuItem>
        </Select>
        <FormHelperText>{errors.category}</FormHelperText>
      </FormControl>
      <FormControl fullWidth margin="normal">
        <InputLabel>Type</InputLabel>
        <Select value={geoZone.type} onChange={onTypeChange} disabled={geoZone.id}>
          <MenuItem value="CIRCULAR">Circular</MenuItem>
          <MenuItem value="POLYGONAL">Polygonal</MenuItem>
        </Select>
      </FormControl>
      {geoZone.type === 'CIRCULAR' && (
        <>
        <FormControl fullWidth margin="normal" error={!!errors.latitude}>
          <TextField
            label="Latitude"
            name="latitude"
            type="number"
            fullWidth
            value={geoZone.latitude}
            onChange={(e) => handleChange(e.target.name, parseFloat(e.target.value))}
            />
          <FormHelperText>{errors.latitude}</FormHelperText>
        </FormControl>
        <FormControl fullWidth margin="normal" error={!!errors.longitude}>
          <TextField
            label="Longitude"
            name="longitude"
            type="number"
            fullWidth
            value={geoZone.longitude}
            onChange={(e) => handleChange(e.target.name, parseFloat(e.target.value))}
            />
          <FormHelperText>{errors.longitude}</FormHelperText>
        </FormControl>
        <FormControl fullWidth margin="normal" error={!!errors.radius}>
          <TextField
            label="Radius (m)"
            name="radius"
            type="number"
            fullWidth
            value={geoZone.radius}
            onChange={(e) => handleChange(e.target.name, parseFloat(e.target.value))}
            />
          <FormHelperText>{errors.radius}</FormHelperText>
        </FormControl>
        </>
      )}
      {geoZone.type === 'POLYGONAL' && (
        <>
        <FormControl fullWidth margin="normal" error={!!errors.coordinates}>
          <TextField
          label="Coordinates"
          name="coordinates"
          fullWidth
          value={coordinatesInput}
          onChange={(e) => setCoordinatesInput(e.target.value)}
        />
          <FormHelperText>{errors.coordinates}</FormHelperText>
        </FormControl>
        <Button fullWidth onClick={() => handleChange('coordinates', JSON.parse(coordinatesInput))}>Confirm Coordinates</Button>
        </>
      )}
      <FormControl fullWidth margin="normal" error={!!errors.altitude_level}>
        <InputLabel>Min Altitude Level</InputLabel>
        <Select value={geoZone.altitude_level} name="altitude_level" onChange={(e) => handleChange(e.target.name, e.target.value)} >
          <MenuItem value="L0">L0 - 0 m</MenuItem>
          <MenuItem value="L1">L1 - 25 m</MenuItem>
          <MenuItem value="L2">L2 - 45 m</MenuItem>
          <MenuItem value="L3">L3 - 60 m</MenuItem>
          <MenuItem value="L4">L4 - 120 m</MenuItem>
        </Select>
        <FormHelperText>{errors.altitude_level}</FormHelperText>
      </FormControl>
    </div>
  );
}

GeoZoneDetailsForm.propTypes = {
    geoZone: PropTypes.object,
    errors: PropTypes.object,
    handleChange: PropTypes.func.isRequired,
    handleTypeChange: PropTypes.func.isRequired,
};
