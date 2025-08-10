# Weather

## Purpose

Provides weather data, including information about rain clusters and configuration for weather simulation, such as wind direction and intensity.

## Rain Cell

### Properties
- `coordinates`: List of coordinates of the rain cell.

### Example

```json
{
  "coordinates": [
    ["double", "double"],
    ["double", "double"],
    ["double", "double"],
    ["double", "double"],
    ["double", "double"]
  ]
}
```

## Weather Configuration

### Properties
- `windDirection (double)`: Wind direction (North azimuth degrees).
- `windIntensity (double)`: Wind intensity affecting rain movement (km/h).
- `minClusters (int)`: Minimum number of rain clusters.
- `maxClusters (int)`: Maximum number of rain clusters.
- `maxClusterRadius (double)`: Maximum radius of a rain cluster (km).


### Example

```json
{
  "windDirection": "double",
  "windIntensity": "double",
  "minClusters": "integer",
  "maxClusters": "integer",
  "maxClusterRadius": "double"
}
```

## Simulation Configuration

### Properties
- `latitude (double)`: latitude of the center of simulation area.
- `longitude (double)`: longitude of the center of simulation area.

### Example

```json
{
  "latitude": "double",
  "longitude": "double"
}
```

## Weather API

Base URL: `/weather`

### Rain Cells

#### Get RainCells
Retrieves information about the currently active rain cells in the simulation.

```
GET /weather/rain-cell
```

###### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: List of rain cells with coordinates.

```json
[
  {
    "coordinates": [
      ["double", "double"],
      ["double", "double"],
      ["double", "double"],
      ["double", "double"],
      ["double", "double"]
    ]
  },
  {
    "coordinates": [
      ["double", "double"],
      ["double", "double"],
      ["double", "double"],
      ["double", "double"],
      ["double", "double"]
    ]
  }
]
```

##### Error Responses
- **500 Internal Server Error**: If there is an issue retrieving rain cells.


---

### Weather Configuration


#### Get Weather Configuration
Retrieves the current weather simulation configuration (wind direction, wind intensity, rain clusters, and maximum cluster size).

```
GET /weather/config
```

###### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Current weather simulation configuration.

```json
{
  "windDirection": "double",
  "windIntensity": "double",
  "minClusters": "integer",
  "maxClusters": "integer",
  "maxClusterRadius": "double"
}
```

##### Error Responses
- **500 Internal Server Error**: If there is an issue retrieving the weather configuration.

#### Update Weather Configuration
Allows for updating the weather simulation configuration (wind direction, wind intensity, number of clusters, etc.).

```
PUT /weather/config
```

###### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Updated weather simulation configuration.

```json
{
  "windDirection": "double",
  "windIntensity": "double",
  "minClusters": "integer",
  "maxClusters": "integer",
  "maxClusterRadius": "double"
}
```

##### Error Responses
- **500 Internal Server Error**: If there is an issue updating the weather configuration.


### Simulation Configuration API

Base URL: `/setting`

#### Get Simulation Configuration
Retrieves the current simulation configuration (latitude and longitude of the center of the simulation area).

```
GET /setting/coordinates
```

###### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Current simulation configuration.

```json
{
  "latitude": "double",
  "longitude": "double"
}
```

##### Error Responses
- **500 Internal Server Error**: If there is an issue retrieving the simulation configuration.

#### Update Simulation Configuration
Allows for updating the simulation configuration (latitude and longitude of the center of the simulation area).

```
PUT /setting/coordinates
```

###### Request
- **Content-Type**: application/json
- **Body**: Simulation configuration object.


##### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Updated simulation configuration.

##### Error Responses
- **500 Internal Server Error**: If there is an issue updating the simulation configuration.


## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.