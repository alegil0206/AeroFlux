const API_ENDPOINT = window.env.WEATHER_ENDPOINT;

export const fetchWeather = async () => {
  const response = await fetch(`${API_ENDPOINT}/weather/rain-cell`);
  if (!response.ok) throw new Error(`Error fetching weather data: ${response.statusText}`);
  return response.json();
};

export const fetchWeatherConfig = async () => {
  const response = await fetch(`${API_ENDPOINT}/weather/config`);
  if (!response.ok) throw new Error(`Error fetching weather config: ${response.statusText}`);
  return response.json();
};

export const updateWeatherConfig = async (config) => {
  const response = await fetch(`${API_ENDPOINT}/weather/config`, {
    method: 'PUT',
    headers: {'Content-Type': 'application/json',},
    body: JSON.stringify(config),
  });
  if (!response.ok) throw new Error(`Error updating weather config: ${response.statusText}`);
  return response.json();
}