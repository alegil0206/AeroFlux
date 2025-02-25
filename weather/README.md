# Weather

## Purpose

Provides weather data based on city name or geographic coordinates (latitude and longitude).

## Weather Data

### Properties
- <code>location</code>: Information about the location (city and country).
- <code>current</code>: Current weather details such as temperature, humidity, wind speed, and visibility.
- <code>timestamp</code>: The timestamp of the weather data in ISO format.

#### Location
- <code>name (string)</code>: Name of the city.
- <code>country (string)</code>: Country of the city.
- <code>lat (double)</code>: Latitude of the city.
- <code>lng (double)</code>: Longitude of the city.

#### Current Weather
- <code>temperature (double)</code>: Current temperature in Celsius.
- <code>feels_like (double)</code>: Apparent temperature in Celsius, what the weather "feels like."
- <code>weather</code>: Weather conditions.
  - <code>main (string)</code>: General weather condition (e.g., Clear, Rain).
  - <code>description (string)</code>: Detailed description of the weather condition.
- <code>wind</code>: Wind data.
  - <code>speed (double)</code>: Wind speed in meters per second.
  - <code>direction (string)</code>: Wind direction in cardinal points (e.g., N, NE).
- <code>humidity (int)</code>: Humidity percentage.
- <code>visibility (int)</code>: Visibility in meters.
- <code>pressure (int)</code>: Atmospheric pressure in hPa.

### Example Response (Coordinates)

```json
{
  "location": {
    "name": "CityName",
    "country": "CountryName",
    "lat": 52.52,
    "lng": 13.405
  },
  "current": {
    "temperature": 18.5,
    "feels_like": 17.3,
    "weather": {
      "main": "Clear",
      "description": "clear sky"
    },
    "wind": {
      "speed": 3.5,
      "direction": "NNE"
    },
    "humidity": 60,
    "visibility": 10000,
    "pressure": 1012
  },
  "timestamp": "2025-01-21T15:30:00Z"
}
```

### Example Response (City Name)

```json
{
  "location": {
    "name": "CityName",
    "country": "CountryName",
    "lat": 52.52,
    "lng": 13.405
  },
  "current": {
    "temperature": 18.5,
    "feels_like": 17.3,
    "weather": {
      "main": "Clear",
      "description": "clear sky"
    },
    "wind": {
      "speed": 3.5,
      "direction": "NNE"
    },
    "humidity": 60,
    "visibility": 10000,
    "pressure": 1012
  },
  "timestamp": "2025-01-21T15:30:00Z"
}
```

## API

Base URL: `/weather`

### Coordinates

#### Get Weather by Coordinates
Retrieves weather data for a specific location by its geographic coordinates (latitude and longitude).

```
GET /weather/coordinates?longitude={longitude}&latitude={latitude}
```

##### Parameters
- **longitude** (query parameter, required): Longitude of the location.
- **latitude** (query parameter, required): Latitude of the location.

###### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Weather data for the specified coordinates.

##### Error Responses
- **500 Internal Server Error**: If the weather data retrieval fails.

```json
{
  "error": "Error message"
}
```

### City Name

#### Get Weather by City
Retrieves weather data for a specific location by its city name.

```
GET /weather/city/{city}
```

##### Parameters
- **city** (path parameter, required): Name of the city.

###### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Weather data for the specified city.

##### Error Responses
- **500 Internal Server Error**: If the weather data retrieval fails.

```json
{
  "error": "Error message"
}
```

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.