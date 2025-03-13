import PropTypes from 'prop-types';
import { useState } from 'react';
import {
  Card,
  CardContent,
  CardActions,
  Button,
  MenuItem,
  Select,
  InputLabel,
  FormControl,
  TextField,
  Typography,
} from '@mui/material';

export default function AuthorizationRequestCard({ drones, geoZones, onAdd }) {
  const [selectedDrone, setSelectedDrone] = useState('');
  const [selectedGeoZone, setSelectedGeoZone] = useState('');
  const [duration, setDuration] = useState('');

  const handleAddClick = () => {
    if (selectedDrone && selectedGeoZone && duration) {
      const authorizationRequest = {
        drone_id: selectedDrone,
        geozone_id: selectedGeoZone,
        duration: parseInt(duration, 10)
      };
      onAdd(authorizationRequest);
      setSelectedDrone('');
      setSelectedGeoZone('');
      setDuration('');
    }
  };

  return (
    <Card
      variant="outlined"
      sx={{ display: 'flex', flexDirection: 'column', gap: '8px', flexGrow: 1 }}
    >
      <CardContent>
        <Typography variant="h6" component="div" sx={{ marginBottom: 2 }}>
          Add Authorization
        </Typography>
        <FormControl fullWidth sx={{ marginBottom: 2 }}>
          <InputLabel id="select-drone-label">Select Drone</InputLabel>
          <Select
            labelId="select-drone-label"
            value={selectedDrone}
            onChange={(e) => setSelectedDrone(e.target.value)}
            label="Select Drone"
          >
            {drones.map((drone) => (
              <MenuItem key={drone.id} value={drone.id}>
                {`${drone.id} - ${drone.name}`}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <FormControl fullWidth sx={{ marginBottom: 2 }}>
          <InputLabel id="select-geoZone-label">Select GeoZone</InputLabel>
          <Select
            labelId="select-geoZone-label"
            value={selectedGeoZone}
            onChange={(e) => setSelectedGeoZone(e.target.value)}
            label="Select GeoZone"
          >
            {geoZones.map((geoZone) => (
              <MenuItem key={geoZone.id} value={geoZone.id}>
                {`${geoZone.id} - ${geoZone.name}`}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
        <TextField
          fullWidth
          type="number"
          label="Duration (in minutes)"
          value={duration}
          onChange={(e) => setDuration(e.target.value)}
          sx={{ marginBottom: 2 }}
          inputProps={{ min: 1 }}
        />
      </CardContent>
      <CardActions>
        <Button
          variant="contained"
          color="primary"
          fullWidth
          disabled={!selectedDrone || !selectedGeoZone || !duration || duration <= 0}
          onClick={handleAddClick}
        >
          Add Authorization
        </Button>
      </CardActions>
    </Card>
  );
}

AuthorizationRequestCard.propTypes = {
  drones: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
    })
  ).isRequired,
  geoZones: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
    })
  ).isRequired,
  onAdd: PropTypes.func.isRequired,
};
