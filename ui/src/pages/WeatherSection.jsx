import Box from "@mui/material/Box";
import { useState, useEffect } from "react";
import Grid from "@mui/material/Grid2";
import WeatherMap from "../components/Weather/WeatherMap";
import StatCard from "../components/Weather/StatCard";
import LocationCard from "../components/Weather/LocationCard";
import WeatherCard from "../components/Weather/WeatherCard";
import CircularProgress from "@mui/material/CircularProgress";
import Typography from "@mui/material/Typography";
import { fetchWeatherByCoordinates } from "../services/weather";

function WeatherSection() {
  const [coordinates, setCoordinates] = useState({
    latitude: 45.523901,
    longitude: 9.219752,
  });

  const [weatherData, setWeatherData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const loadWeatherData = async (latitude, longitude) => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchWeatherByCoordinates(latitude, longitude);
      setWeatherData(data);
    } catch (err) {
      console.error("Error loading weather data:", err);
      setError("Failed to load weather data. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadWeatherData(coordinates.latitude, coordinates.longitude);
  }, []);

  useEffect(() => {
    loadWeatherData(coordinates.latitude, coordinates.longitude);
  }, [coordinates]);

  const handleCoordinatesChange = (newCoordinates) => {
    setCoordinates(newCoordinates);
  };


  return (
    <Box sx={{ width: "100%", maxWidth: { sm: "100%", md: "1700px" } }}>
      <Grid container spacing={1} columns={12}>
        <Grid size={{ xs: 12, lg: 6 }}>
          <WeatherMap
            coordinates={coordinates}
            onCoordinatesChange={handleCoordinatesChange}
          />
        </Grid>

        {error && (
          <Grid size={{ xs: 12, lg: 6 }}>
            <Typography color="error" variant="h6" sx={{ textAlign: "center" }}>
              {error}
            </Typography>
          </Grid>
        )}

        {loading && (
          <Grid size={{ xs: 12, lg: 6 }}>
            <Box
              sx={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                height: "100vh",
              }}
            >
              <CircularProgress />
            </Box>
          </Grid>
        )}

        {weatherData && (
          <Grid size={{ xs: 12, lg: 6 }}>
            <Grid container spacing={1} columns={12}>
              <Grid size={{ xs: 12 }}>
                <LocationCard
                  data={{
                    title: "Location",
                    value: `${weatherData.location.name}, ${weatherData.location.country}`,
                    coordinates: `${weatherData.location.lat}, ${weatherData.location.lng}`,
                  }}
                />
              </Grid>
              <Grid size={{ xs: 12 }}>
                <StatCard
                    data={{
                      title: "Last Updated",
                      value: `${new Date(weatherData.timestamp).toLocaleString()}`,
                    }}
                />
              </Grid>
              <Grid size={{ xs: 6 }}>
                <StatCard
                  data={{
                    title: "Temperature",
                    value: `${weatherData.current.temperature}°C`,
                  }}
                />
              </Grid>
              <Grid size={{ xs: 6 }}>
                <StatCard
                  data={{
                    title: "Feels Like",
                    value: `${weatherData.current.feels_like}°C`,
                  }}
                />
              </Grid>
              <Grid size={{ xs: 12 }}>
                <WeatherCard
                  data={{
                    title: "Weather",
                    value: weatherData.current.weather,
                  }}
                />
              </Grid>
              <Grid size={{ xs: 6 }}>
                <StatCard
                  data={{
                    title: "Wind",
                    value: `${weatherData.current.wind.speed} km/h`,
                  }}
                />
              </Grid>
              <Grid size={{ xs: 6 }}>
                <StatCard
                  data={{
                    title: "Wind Direction",
                    value: weatherData.current.wind.direction,
                  }}
                />
              </Grid>
              <Grid size={{ xs: 6 }}>
                <StatCard
                  data={{
                    title: "Humidity",
                    value: `${weatherData.current.humidity}%`,
                  }}
                />
              </Grid>
              <Grid size={{ xs: 6 }}>
                <StatCard
                  data={{
                    title: "UV Index",
                    value: weatherData.current.uv_index || "N/A",
                  }}
                />
              </Grid>
              <Grid size={{ xs: 6 }}>
                <StatCard
                  data={{
                    title: "Visibility",
                    value: `${weatherData.current.visibility / 1000} km`,
                  }}
                />
              </Grid>
              <Grid size={{ xs: 6 }}>
                <StatCard
                  data={{
                    title: "Pressure",
                    value: `${weatherData.current.pressure} hPa`,
                  }}
                />
              </Grid>
            </Grid>
          </Grid>
        )}
      </Grid>
    </Box>
  );
}

export default WeatherSection;
