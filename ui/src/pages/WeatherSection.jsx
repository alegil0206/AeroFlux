import Box from "@mui/material/Box";
import { useState, useEffect } from "react";
import Grid from "@mui/material/Grid2";
import WeatherMap from "../components/Weather/WeatherMap";
import WeatherSettingsCard from "../components/Weather/WeatherSettingsCard";
import { Snackbar, Alert } from "@mui/material";
import { fetchWeather, fetchWeatherConfig, updateWeatherConfig } from "../services/weather";

function WeatherSection() {

  const [weatherData, setWeatherData] = useState([]);
  const [weatherSettings, setWeatherSettings] = useState(null);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState(null);

  const showSnackbar = (type, message) => {
    setAlertMessage({ type: type, text: message });
    setSnackbarOpen(true);
  }

  const closeSnackbar = () => {
    setSnackbarOpen(false);
    setAlertMessage(null);
  }

  const loadWeatherSettings = async () => {
    try {
      const data = await fetchWeatherConfig();
      setWeatherSettings(data);
    } catch (error) {
      showSnackbar("error", error.message);
    }
  }

  useEffect(() => {
    if (!weatherSettings)
      loadWeatherSettings();
    const interval = setInterval(() => {
      if (!weatherSettings) {
        loadWeatherSettings();
      } else {
        clearInterval(interval);
      }
    }, 5000);
    return () => clearInterval(interval);
  }, [weatherSettings]);

  const loadWeatherData = async () => {
    try {
      const data = await fetchWeather();
      setWeatherData(data);
    } catch (err) {
      showSnackbar("error", error.message);
    }
  };

  useEffect(() => {
    loadWeatherData();
    const interval = setInterval(loadWeatherData, 5000);
    return () => clearInterval(interval);
  }, []);

  const handleEditConfig = async (config) => {
    try {
      await updateWeatherConfig(config);
      showSnackbar("success", "Weather config updated successfully.");
      setWeatherSettings(config);
    } catch (error) {
      showSnackbar("error", error.message);
    }
  }

  return (
    <Box sx={{ width: "100%", maxWidth: { sm: "100%", md: "1700px" } }}>
      <Grid container spacing={1} columns={12}>
        <Grid size={{ xs: 12, lg: 8 }}>
          <WeatherMap
            weatherData={weatherData}
          />
        </Grid>
        
        <Grid size={{ xs: 12, lg: 4 }}>
          <WeatherSettingsCard weatherSettings = {weatherSettings} onSave = {handleEditConfig} />
        </Grid>          

      </Grid>

      <Snackbar
        open={snackbarOpen}
        autoHideDuration={5000}
        onClose={closeSnackbar}
        anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
      >
        <Alert severity={alertMessage?.type} onClose={closeSnackbar}>
          {alertMessage?.text}
        </Alert>
      </Snackbar>
    </Box>
  );
}

export default WeatherSection;
