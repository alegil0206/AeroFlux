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
  FormHelperText
} from "@mui/material";

export default function WeatherSettingsCard({ weatherSettings, onSave }) {
  const [errors, setErrors] = useState({});
  const [settings, setSettings] = useState({
    windDirection: NaN,
    windIntensity: NaN,
    minClusters: NaN,
    maxClusters: NaN,
    maxClusterSize: NaN,
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
      newErrors.windDirection = "Wind direction is required and must be between 0 and 359";
    }
    if (isNaN(settings.windIntensity) || settings.windIntensity < 0) {
      newErrors.windIntensity = "Wind intensity is required and must be greater than 0";
    }
    if (isNaN(settings.minClusters) || settings.minClusters < 0) {
      newErrors.minClusters = "Min clusters is required and must be greater than 0";
    }
    if (isNaN(settings.maxClusters) || settings.maxClusters < settings.minClusters) {
      newErrors.maxClusters = "Max clusters is required and must be greater than min clusters";
    }
    if (isNaN(settings.maxClusterSize) || settings.maxClusterSize < 0) {
      newErrors.maxClusterSize = "Max cluster size is required and must be greater than 0";
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
       
        {/* Direzione del vento */}
        <FormControl fullWidth margin="normal" error={!!errors.windDirection}>
          <TextField
            fullWidth
            type="number"
            label="Wind Direction"
            name="windDirection"
            value={settings.windDirection}
            onChange= {(e) => handleChange(e.target.name, parseFloat(e.target.value))}
            inputProps={{ min: 0, step: 0.1, max: 360 }}
          />
          <FormHelperText>{errors.windDirection}</FormHelperText>
        </FormControl>

        {/* Intensità del vento */}
        <FormControl fullWidth margin="normal" error={!!errors.windIntensity}>
          <TextField
            fullWidth
            type="number"
            label="Wind Intensity"
            name="windIntensity"
            value={settings.windIntensity}
            onChange={(e) => handleChange(e.target.name, parseFloat(e.target.value))}
            inputProps={{ min: 0, step: 0.1 }}
          />
          <FormHelperText>{errors.windIntensity}</FormHelperText>
        </FormControl>

        {/* Numero minimo di cluster */}
        <FormControl fullWidth margin="normal" error={!!errors.minClusters}>
          <TextField
            fullWidth
            type="number"
            label="Min Clusters"
            name="minClusters"
            value={settings.minClusters}
            onChange={(e) => handleChange(e.target.name, parseInt(e.target.value))}
            inputProps={{ min: 0, step: 1 }}
          />
          <FormHelperText>{errors.minClusters}</FormHelperText>
        </FormControl>

        {/* Numero massimo di cluster */}
        <FormControl fullWidth margin="normal" error={!!errors.maxClusters}>
          <TextField
            fullWidth
            type="number"
            label="Max Clusters"
            name="maxClusters"
            value={settings.maxClusters}
            onChange={(e) => handleChange(e.target.name, parseInt(e.target.value))}
            inputProps={{ min: settings.minClusters, step: 1 }}
          />
          <FormHelperText>{errors.maxClusters}</FormHelperText>
        </FormControl>

        {/* Dimensione massima cluster */}
        <FormControl fullWidth margin="normal" error={!!errors.maxClusterSize}>
          <TextField
            fullWidth
            type="number"
            label="Max Cluster Size"
            name="maxClusterSize"
            value={settings.maxClusterSize}
            onChange={(e) => handleChange(e.target.name, parseInt(e.target.value))}
            inputProps={{ min: 0, step: 1 }}
          />
          <FormHelperText>{errors.maxClusterSize}</FormHelperText>
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
    maxClusterSize: PropTypes.number.isRequired,
  }).isRequired,
  onSave: PropTypes.func.isRequired,
};
