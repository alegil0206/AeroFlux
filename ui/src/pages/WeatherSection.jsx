import Box from "@mui/material/Box";
import { useState, useEffect } from "react";
import Grid from "@mui/material/Grid2";
import WeatherMap from "../components/Weather/WeatherMap";
import Typography from "@mui/material/Typography";
import { fetchWeather } from "../services/weather";

function WeatherSection() {

  const [weatherData, setWeatherData] = useState([]);
  const [error, setError] = useState(null);

  const loadWeatherData = async () => {
    setError(null);
    try {
      const data = await fetchWeather();
      setWeatherData(data);
    } catch (err) {
      console.error("Error loading weather data:", err);
      setError("Failed to load weather data. Please try again.");
    }
  };

  useEffect(() => {
    loadWeatherData();
    const interval = setInterval(loadWeatherData, 10000);
    return () => clearInterval(interval);
  }, []);

  return (
    <Box sx={{ width: "100%", maxWidth: { sm: "100%", md: "1700px" } }}>
      <Grid container spacing={1} columns={12}>
        <Grid size={{ xs: 12, lg: 10 }}>
          <WeatherMap
            weatherData={weatherData}
          />
        </Grid>

        {error && (
          <Grid size={{ xs: 12, lg: 2 }}>
            <Typography color="error" variant="h6" sx={{ textAlign: "center" }}>
              {error}
            </Typography>
          </Grid>
        )}

      </Grid>
    </Box>
  );
}

export default WeatherSection;
