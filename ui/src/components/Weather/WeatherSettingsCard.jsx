import PropTypes from "prop-types";
import { useState, useEffect } from "react";
import {
  Card,
  CardContent,
  CardActions,
  Button,
  TextField,
  Typography,
  FormControl,
  FormHelperText,
  InputAdornment,
} from "@mui/material";

export default function WeatherSettingsCard({ weatherSettings, onSave }) {
  const [errors, setErrors] = useState({});
  const [settings, setSettings] = useState({
    windDirection: '',
    windIntensity: '',
    minClusters: '',
    maxClusters: '',
    maxClusterRadius: '',
  });

  useEffect(() => {
    if (weatherSettings)
      setSettings(weatherSettings);
  }, [weatherSettings]);

  const handleChange = (name, value) => {
    setSettings({
      ...settings,
      [name]: value,
    });
  };

  const handleSaveClick = () => {
    if (validateFields()) {
      onSave({ ...settings });
    }
  };

  const validateFields = () => {
    const newErrors = {};
    if (isNaN(settings.windDirection) || settings.windDirection<0 || settings.windDirection>359) {
      newErrors.windDirection = "Wind direction is required and must be between 0° and 359°";
    }
    if (isNaN(settings.windIntensity) || settings.windIntensity < 0) {
      newErrors.windIntensity = "Wind speed is required and must be greater than 0";
    }
    if (isNaN(settings.minClusters) || settings.minClusters < 0) {
      newErrors.minClusters = "Minimum clusters is required and must be greater than 0";
    }
    if (isNaN(settings.maxClusters) || settings.maxClusters < settings.minClusters) {
      newErrors.maxClusters = "Maximum clusters is required and must be greater than min clusters";
    }
    if (isNaN(settings.maxClusterRadius) || settings.maxClusterRadius < 0) {
      newErrors.maxClusterRadius = "Maximum cluster radius is required and must be greater than 0";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  }

  return (
    <Card variant="outlined" 
    sx={{ display: 'flex', flexDirection: 'column', gap: '8px', flexGrow: 1 }}>
      <CardContent>
        <Typography variant="h6" component="div" sx={{ mb: 2 }}>
          Weather Settings
        </Typography>
       
        <FormControl fullWidth margin="normal" error={!!errors.windDirection}>
          <TextField
            fullWidth
            type="number"
            label="Wind Direction (North azimuth degrees)"
            name="windDirection"
            value={settings.windDirection}
            onChange={(e) => handleChange(e.target.name, parseFloat(e.target.value))}
            inputProps={{ min: 0, step: 0.1, max: 359 }}
            slotProps={{
              input: {
                  endAdornment: <InputAdornment position="end">°</InputAdornment>,
                },
            }}
          />
          <FormHelperText>{errors.windDirection}</FormHelperText>
        </FormControl>

        <FormControl fullWidth margin="normal" error={!!errors.windIntensity}>
          <TextField
            fullWidth
            type="number"
            label="Wind Speed"
            name="windIntensity"
            value={settings.windIntensity}
            onChange={(e) => handleChange(e.target.name, parseFloat(e.target.value))}
            inputProps={{ min: 0, step: 0.1 }}
            slotProps={{
              input: {
                  endAdornment: <InputAdornment position="end">km/h</InputAdornment>,
                },
            }}
          />
          <FormHelperText>{errors.windIntensity}</FormHelperText>
        </FormControl>

        <FormControl fullWidth margin="normal" error={!!errors.minClusters}>
          <TextField
            fullWidth
            type="number"
            label="Minimum number of clusters"
            name="minClusters"
            value={settings.minClusters}
            onChange={(e) => handleChange(e.target.name, parseInt(e.target.value))}
            inputProps={{ min: 0, step: 1 }}
          />
          <FormHelperText>{errors.minClusters}</FormHelperText>
        </FormControl>

        <FormControl fullWidth margin="normal" error={!!errors.maxClusters}>
          <TextField
            fullWidth
            type="number"
            label="Maximum number of clusters"
            name="maxClusters"
            value={settings.maxClusters}
            onChange={(e) => handleChange(e.target.name, parseInt(e.target.value))}
            inputProps={{ min: settings.minClusters, step: 1 }}
          />
          <FormHelperText>{errors.maxClusters}</FormHelperText>
        </FormControl>

        <FormControl fullWidth margin="normal" error={!!errors.maxClusterRadius}>
          <TextField
            fullWidth
            type="number"
            label="Maximum Cluster Radius"
            name="maxClusterRadius"
            value={settings.maxClusterRadius}
            onChange={(e) => handleChange(e.target.name, parseInt(e.target.value))}
            inputProps={{ min: 0, step: 1 }}
            slotProps={{
              input: {
                  endAdornment: <InputAdornment position="end">km</InputAdornment>,
                },
            }}
          />
          <FormHelperText>{errors.maxClusterRadius}</FormHelperText>
        </FormControl>

      </CardContent>

      <CardActions>
        <Button variant="contained" color="primary" fullWidth onClick={handleSaveClick}>
          Save Settings
        </Button>
      </CardActions>
    </Card>
  );
}

WeatherSettingsCard.propTypes = {
  weatherSettings: PropTypes.shape({
    windDirection: PropTypes.number.isRequired,
    windIntensity: PropTypes.number.isRequired,
    minClusters: PropTypes.number.isRequired,
    maxClusters: PropTypes.number.isRequired,
    maxClusterRadius: PropTypes.number.isRequired,
  }).isRequired,
  onSave: PropTypes.func.isRequired,
};
