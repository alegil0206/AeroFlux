import Box from "@mui/material/Box";
import { useState, useEffect } from "react";
import Grid from "@mui/material/Grid2";
import WeatherMap from "../components/Weather/WeatherMap";
import WeatherSettingsCard from "../components/Weather/WeatherSettingsCard";
import { Snackbar, Alert } from "@mui/material";

import { useWeather } from "../hooks/useWeather";

function WeatherSection() {

  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState(null);

  const { 
    weather,
    weatherConfig,
    error, 
    fetchWeather, 
    fetchWeatherConfig, 
    updateWeatherConfig } = useWeather();

  const showSnackbar = (type, message) => {
    setAlertMessage({ type: type, text: message });
    setSnackbarOpen(true);
  }

  const closeSnackbar = () => {
    setSnackbarOpen(false);
    setAlertMessage(null);
  }

  useEffect(() => {
    if (error) showSnackbar("error", error.message);
  }, [error]);

  useEffect(() => {
    if (!weatherConfig)
      fetchWeatherConfig();
    const interval = setInterval(() => {
      if (!weatherConfig) {
        fetchWeatherConfig();
      } else {
        clearInterval(interval);
      }
    }, 5000);
    return () => clearInterval(interval);
  }, [weatherConfig]);

  useEffect(() => {
    fetchWeather();
    const interval = setInterval(fetchWeather, 5000);
    return () => clearInterval(interval);
  }, []);

  const handleEditConfig = async (config) => {
    const isOk =  await updateWeatherConfig(config);
    if (isOk)
      showSnackbar("success", "Weather config updated successfully.");
    fetchWeatherConfig();
  }

  return (
    <Box sx={{ width: "100%" }}>
      <Grid container spacing={1} columns={12}>
        <Grid size={{ xs: 12, lg: 7 }}>
          <WeatherMap
            weatherData={weather}
          />
        </Grid>
        
        <Grid size={{ xs: 12, lg: 5 }}>
          <WeatherSettingsCard weatherSettings = {weatherConfig} onSave = {handleEditConfig} />
        </Grid>          

      </Grid>

      <Snackbar
        open={snackbarOpen}
        autoHideDuration={5000}
        onClose={closeSnackbar}
        anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
      >
        <Alert severity={alertMessage?.type} onClose={closeSnackbar} variant="filled">
          {alertMessage?.text}
        </Alert>
      </Snackbar>
    </Box>
  );
}

export default WeatherSection;
