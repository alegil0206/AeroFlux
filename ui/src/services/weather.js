const API_ENDPOINT = window.env.WEATHER_ENDPOINT;

export const fetchWeatherByCoordinates = async (latitude, longitude) => {
  const response = await fetch(`${API_ENDPOINT}/weather/coordinates?latitude=${latitude}&longitude=${longitude}`);
  if (!response.ok) throw new Error(`Error fetching weather data: ${response.statusText}`);
  return response.json();
};
