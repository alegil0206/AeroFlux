import {
  TextField,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Grid2 as Grid,
  FormHelperText,
  Typography,
  FormControlLabel,
  Checkbox
} from '@mui/material';
import PropTypes from 'prop-types';

export default function DroneDetailsForm({ drone, errors, handleChange }) {

  return (
    <div>
      <Typography component="h3" variant="h6">Drone Details</Typography>
      <FormControl fullWidth margin="normal" error={!!errors.name}>
        <TextField
          label="Name"
          name="name"
          value={drone.name}
          onChange={(e) => handleChange('name', e.target.value)}
          fullWidth
        />
        <FormHelperText>{errors.name}</FormHelperText>
      </FormControl>

      <FormControl fullWidth margin="normal" error={!!errors.model}>
        <TextField
          label="Model"
          name="model"
          value={drone.model}
          onChange={(e) => handleChange('model', e.target.value)}
          fullWidth
        />
        <FormHelperText>{errors.model}</FormHelperText>
      </FormControl>

      <FormControl fullWidth margin="normal" error={!!errors.operation_category}>
        <InputLabel>Operation Category</InputLabel>
        <Select
          name="operation_category"
          value={drone.operation_category}
          onChange={(e) => handleChange('operation_category', e.target.value)}
        >
          <MenuItem value="CERTIFIED">Certified</MenuItem>
          <MenuItem value="SPECIFIC">Specific</MenuItem>
        </Select>
        <FormHelperText>{errors.operation_category}</FormHelperText>
      </FormControl>

      <FormControl fullWidth margin="normal" error={!!errors.owner}>
        <TextField
          label="Owner"
          name="owner"
          value={drone.owner}
          onChange={(e) => handleChange('owner', e.target.value)}
          fullWidth
        />
        <FormHelperText>{errors.owner}</FormHelperText>
      </FormControl>

      <Typography component="h3" variant="h6">Drone Characteristics</Typography>
      <FormControl fullWidth margin="normal" error={!!errors.flight_autonomy}>
          <TextField
            fullWidth
            type="number"
            label="Flight Autonomy (minutes)"
            name="flight_autonomy"
            value={drone.flight_autonomy}
            onChange={(e) => handleChange(e.target.name, parseFloat(e.target.value))}
            inputProps={{ min: 0, step: 1 }}
          />
          <FormHelperText>{errors.flight_autonomy}</FormHelperText>
      </FormControl>

      <Grid container columns={12} >
        {[
          { key: 'safe_landing', label: 'Safe Landing' },
          { key: 'collision_avoidance', label: 'Collision Avoidance' },
          { key: 'geo_awareness', label: 'Geo Awareness' },
          { key: 'auto_authorization', label: 'Auto Authorization' },
          { key: 'flight_autonomy_management', label: 'Flight Autonomy Management' },
        ].map(({ key, label }) => (
          <Grid size={{ xs: 12, md: 6 }} key={key}>
            <FormControlLabel
              control={
                <Checkbox
                  checked={drone.adaptive_capabilities[key]}
                  onChange={(e) =>
                    handleChange('adaptive_capabilities', {
                      ...drone.adaptive_capabilities,
                      [key]: e.target.checked,
                    })
                  }                
                />
              }
              label={label}
            />
          </Grid>
        ))}
      </Grid>


      <Typography component="h3" variant="h6">Source Position</Typography>
      <Grid container columns={12} spacing={2} sx={{ mb: (theme) => theme.spacing(2) }}>
        <Grid size={{ xs: 12, md: 6 }}>
          <TextField
            label="Latitude"
            type="number"
            value={drone.source.latitude}
            onChange={(e) => handleChange('source', { latitude: parseFloat(e.target.value) })}
            fullWidth
            margin='normal'
            error={!!errors.source_latitude}
            helperText={errors.source_latitude}
          />
        </Grid>
        <Grid size={{ xs: 12, md: 6 }}>
          <TextField
            label="Longitude"
            type="number"
            value={drone.source.longitude}
            onChange={(e) => handleChange('source', { longitude: parseFloat(e.target.value) })}
            fullWidth
            margin='normal'
            error={!!errors.source_longitude}
            helperText={errors.source_longitude}
          />
        </Grid>
      </Grid>

      <Typography component="h3" variant="h6">Destination Position</Typography>
      <Grid container columns={12} spacing={2} sx={{ mb: (theme) => theme.spacing(2) }}>
        <Grid size={{ xs: 12, md: 6 }}>
          <TextField
            label="Latitude"
            type="number"
            value={drone.destination.latitude}
            onChange={(e) => handleChange('destination', { latitude: parseFloat(e.target.value) })}
            fullWidth
            margin='normal'
            error={!!errors.destination_latitude}
            helperText={errors.destination_latitude}
          />
        </Grid>
        <Grid size={{ xs: 12, md: 6 }}>
          <TextField
            label="Longitude"
            type="number"
            value={drone.destination.longitude}
            onChange={(e) => handleChange('destination', { longitude: parseFloat(e.target.value) })}
            fullWidth
            margin='normal'
            error={!!errors.destination_longitude}
            helperText={errors.destination_longitude}
          />
        </Grid>
      </Grid>
    </div>
  );
}

DroneDetailsForm.propTypes = {
  drone: PropTypes.object.isRequired,
  errors: PropTypes.object.isRequired,
  handleChange: PropTypes.func.isRequired,
};
