import { getEndpoint } from "./apiEndpoints";

export const fetchWeather = async () => {
  const endpoint = getEndpoint("weather");
  if (!endpoint) throw new Error("Weather API endpoint not set");

  const response = await fetch(`${endpoint}/weather/rain-cell`);
  if (!response.ok) throw new Error(`Error fetching weather data: ${response.statusText}`);
  return response.json();
};

export const fetchWeatherConfig = async () => {
  const endpoint = getEndpoint("weather");
  if (!endpoint) throw new Error("Weather API endpoint not set");

  const response = await fetch(`${endpoint}/weather/config`);
  if (!response.ok) throw new Error(`Error fetching weather config: ${response.statusText}`);
  return response.json();
};

export const updateWeatherConfig = async (config) => {
  const endpoint = getEndpoint("weather");
  if (!endpoint) throw new Error("Weather API endpoint not set");

  const response = await fetch(`${endpoint}/weather/config`, {
    method: 'PUT',
    headers: {'Content-Type': 'application/json',},
    body: JSON.stringify(config),
  });
  if (!response.ok) throw new Error(`Error updating weather config: ${response.statusText}`);
  return response.json();
}

export const updateGridSettings = async (gridSettings) => {
  const endpoint = getEndpoint("weather");
  if (!endpoint) throw new Error("Weather API endpoint not set");

  const response = await fetch(`${endpoint}/grid`, {
    method: 'PUT',
    headers: {'Content-Type': 'application/json',},
    body: JSON.stringify(gridSettings),
  });
  if (!response.ok) throw new Error(`Error updating grid settings: ${response.statusText}`);
  return response.json();
}