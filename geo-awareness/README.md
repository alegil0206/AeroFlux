# Geo-awareness

## Purpose

Representation of three-dimensional spacial regions where drones need authorizations to access. 

## Geozones

### Properties
 - <code>id (string)</code>: id of the geozone.
 - <code>name (string)</code>: name of the geozone.
 - <code>type (string)</code>: type of the geozone. See [Type](#type) for details.
 - <code>category (string)</code>: category of the geozone. See [Category](#category) for details.
 - <code>status (string)</code>: status of the geozone. See [Status](#status) for details.
 - <code>altitude_level (string)</code>: altitude level of the geozone. See [Altitude level](#altitude-level) for details.
 - <code>altitude (double)</code>: starting altitude value of the geozone. See [Altitude Level](#altitude-level) for details.

### Type

#### Circular geozone
Geozone with a center and a radius.
##### Properties
- <code>latitude (double)</code>: latitude of the center.
- <code>longitude (double)</code>: longitude of the center.
- <code>radius (double)</code>: radius of the center in meters.
##### Example
```json
{
      "id": "string",
      "name": "string",
      "type": "CIRCULAR",
      "category": "string",
      "status": "string",
      "altitude_level": "string",
      "altitude": "double",
      "latitude": "double",
      "longitude": "double",
      "radius": "double",
    }
```

#### Polygonal geozone
Geozone delimited by multiple points (at least three).
##### Properties
- <code>coordintes</code>: array of pairs <code>(longitude, latitude)</code> of the delimiting points. There must be at least three points.
##### Example
```json
{
      "id": "string",
      "name": "string",
      "type": "POLYGONAL",
      "category": "string",
      "status": "string",
      "altitude_level": "string",
      "altitude": "double",
      "coordinates": [["double", "double"], ["double", "double"], "...", ["double", "double"]]
    }
```

### Category
#### Restricted
Drones need explicit authorizations to enter restricted geozones.
#### Excluded
Drones cannot enter excluded geozones.

### Status
#### Active
Geozone which is actually active.
#### Inactive
Geozone which is actually not active.

### Altitude level
Altitude levels represents the height at which geozones start. There are five levels:
 - <code>L0</code>: the geozone starts at 0 meters, drones cannot circulate without an authorization.
 - <code>L1</code>: the geozone starts at 25 meters, drones can circulate without an authorization at any height in the range $[0, 25]$.
 - <code>L2</code>: the geozone starts at 45 meters, drones can circulate without an authorization at any height in the range $[0, 45]$.
 - <code>L3</code>: the geozone starts at 60 meters, drones can circulate without an authorization at any height in the range $[0, 60]$.
 - <code>L4</code>: the geozone starts at 120 meters, drones can circulate without an authorization at any height in the range $[0, 120]$.

Drones cannot circulate at altitudes highers than 120 meters.

## API
Base URL: `/geozone`

### Type
#### Get all types
Retrieves a list of all the avaible types for geozones.
```
GET /geozone/type
```
###### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Array of geozones types
```json
[
  {
    "name": "string",
    "description": "string"
  },
  "...",
  {
    "name": "string",
    "description": "string"
  }
]
```

#### Get a specific type description
Retrieves the description of a specific type.
```
GET /geozone/type/{name}
```
###### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Description of the type specified
```json
{
    "name": "string",
    "description": "string"
}
```

### Category
#### Get all categories
Retrieves a list of all the avaible categories for geozones.
```
GET /geozone/category
```
###### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Array of geozones categories
```json
[
  {
    "name": "string",
    "description": "string"
  },
  "...",
  {
    "name": "string",
    "description": "string"
  }
]
```

#### Get a specific category description
Retrieves the description of a specific category.
```
GET /geozone/category/{name}
```
###### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Description of the category specified
```json
{
    "name": "string",
    "description": "string"
}
```

### Status
#### Get all statuses
Retrieves a list of all the avaible statuses for geozones.
```
GET /geozone/status
```
###### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Array of geozones statuses
```json
[
  {
    "name": "string",
    "description": "string"
  },
  "...",
  {
    "name": "string",
    "description": "string"
  }
]
```

#### Get a specific status description
Retrieves the description of a specific status.
```
GET /geozone/status/{name}
```
###### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Description of the status specified

```json
{
    "name": "string",
    "description": "string"
}
```

### Altitude level
#### Get all altitude levels
Retrieves a list of all the avaible altitude levels for geozones.
```
GET /geozone/altitude
```
###### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Array of geozones altitude levels

```json
[
  {
    "level": "string",
    "altitude": "double",
    "unit": "string"
  },
  "...",
  {
    "level": "string",
    "altitude": "double",
    "unit": "string"
  }
]
```
#### Get a specific altitude level description
Retrieves the description of a specific altitude level.
```
GET /geozone/altitude/{name}
```
###### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Description of the altitude level specified

```json
{
    "level": "string",
    "altitude": "double",
    "unit": "string"
}
```

### Geozone

#### Get All Geozones
Retrieves a list of all geozones.
```
GET /geozone
```
###### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Array of geozones objects
```json
[
  {
    "geozone 1"
  },
  {
    "geozone 2"
  },
  "...",
  {
    "geozone n"
  },
]
```

#### Create New Geozone
Creates a new geozone.
```
POST /geozone
```
##### Request
- **Content-Type**: application/json
- **Body**: geozone object
##### Response
- **Status Code**: 201 Created
- **Headers**: Location: /geozone/{id}
- **Content-Type**: application/json
- **Body**: Created geozone object
##### Error Responses
- **400 Bad Request**: If the request body is invalid
##### Payload
- It is possible to pass just one between <code>altitude</code> and <code>altitude_level</code>

#### Delete All Geozones
Deletes all geozones.
```
DELETE /geozone
```
##### Response
- **Status Code**: 204 No Content

#### Get Geozone by ID
Retrieves a specific geozone by its ID.
```
GET /geozone/{id}
```
##### Parameters
- **id** (path parameter, required): The ID of the geozone
##### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: geozone object
##### Error Responses
- **404 Not Found**: If the geozone with the specified ID does not exist

#### Update Geozone
Updates an existing geozone.
```
PUT /geozone/{id}
```
##### Parameters
- **id** (path parameter, required): The ID of the geozone to update
##### Request
- **Content-Type**: application/json
- **Body**: complete geozone object
##### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Updated geozone object
##### Notes:
- The geozone type is immutable. To modify it, you must delete the existing geozone and create a new one.

##### Error Responses
- **400 Bad Request**: If the request body is invalid
- **403 Forbidden**: If it is attempted to modify the geozone type 
- **404 Not Found**: If the geozone with the specified ID does not exist

#### Delete Geozone by ID
Deletes a specific geozone.
```
DELETE /geozone/{id}
```
##### Parameters
- **id** (path parameter, required): The ID of the geozone to delete
###### Response
- **Status Code**: 204 No Content
###### Error Responses
- **404 Not Found**: If the geozone with the specified ID does not exist

### Additional Notes

- All endpoints that modify data (POST, PUT, DELETE) will trigger a publication event on the message broker.
- The API uses standard HTTP response codes to indicate success or failure.
- All requests and responses are in JSON format.
- IDs are automatically generated for new geozones.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.