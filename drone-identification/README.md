# Drone Identification

## Purpose

Management and identification of drones and their operation categories through a RESTful API.

## Drones

### Properties
- `id (string)`: id of the drone.
- `name (string)`: name of the drone.
- `model (string)`: model of the drone.
- `owner (string)`: owner of the drone.
- `operation_category (string)`: operation category of the drone. See [Operation Category](#operation-category) for details.
- `plan_definition_timestamp (string)`: timestamp of the last time the drone was updated. It does not have to be specified in POST and PUT requests.
- `adaptive_capabilities (object)`: list of adaptive capabilities of the drone, such as "battery_management", "collision_avoidance".
- `battery (double)`: battery capacity of the drone in mAh.
- `source (string)`: geographical source of the drone.
- `destination (string)`: geographical destination of the drone.



### Example
```json
{
    "id": "string",
    "name": "string",
    "model": "string",
    "owner": "string",
    "operation_category": "string",
    "plan_definition_timestamp": "string",
    "adaptive_capabilities": {
      "collision_avoidance": true,
      "geo_awareness": true,
      "auto_authorization": true,
      "battery_management": true
    },
    "battery": "double",
    "source": {
      "longitude": "double",
      "latitude": "double",
      "altitude": "double"
    },
    "destination": {
      "longitude": "double",
      "latitude": "double",
      "altitude": "double"
    }
}
```

### Operation category
#### Certified
Certified drones have less probability of obtaining authorizations for geozones.
#### Specific
Specific drones have more probability of obtaining authorizations for geozones.

## API

Base URL: `/drone`

### Drone

#### Get All Drones
Retrieves a list of all drones.
```
GET /drone
```
##### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Array of drone objects
```json
[
  {
    "drone_1"
  },
  {
    "drone_2"
  },
  "...",
  {
    "drone_n"
  }
]
```

#### Create New Drone
Creates a new drone.
```
POST /drone
```
##### Request
- **Content-Type**: application/json
- **Body**: drone object
##### Response
- **Status Code**: 201 Created
- **Headers**: Location: /drone/{id}
- **Content-Type**: application/json
- **Body**: Created drone object
##### Error Responses
- **400 Bad Request**: If the request body is invalid
##### Payload
- `plan_definition_timestamp` does not need to be specified: if provided, it will be ignored. 

#### Delete All Drones
Deletes all drones.
```
DELETE /drone
```
##### Response
- **Status Code**: 204 No Content

#### Get Drone by ID
Retrieves a specific drone by its ID.
```
GET /drone/{id}
```
##### Parameters
- **id** (path parameter, required): The ID of the drone
##### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: drone object
##### Error Responses
- **404 Not Found**: If the drone with the specified ID does not exist

#### Update Drone
Updates an existing drone.
```
PUT /drone/{id}
```
##### Parameters
- **id** (path parameter, required): The ID of the drone to update
##### Request
- **Content-Type**: application/json
- **Body**: complete drone object
##### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Updated drone object
##### Error Responses
- **400 Bad Request**: If the request body is invalid
- **404 Not Found**: If the drone with the specified ID does not exist
##### Payload
- `plan_definition_timestamp` does not need to be specified: if provided, it will be ignored.

#### Delete Drone by ID
Deletes a specific drone.
```
DELETE /drone/{id}
```
##### Parameters
- **id** (path parameter, required): The ID of the drone to delete
##### Response
- **Status Code**: 204 No Content
##### Error Responses
- **404 Not Found**: If the drone with the specified ID does not exist

### Operation Category

Base URL: `/drone/operation`

#### Get All Operation Categories
Retrieves a list of all available operation categories.
```
GET /drone/operation
```
##### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Array of operation category objects
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

#### Get Operation Category by Name
Retrieves details of a specific operation category.
```
GET /drone/operation/{name}
```
##### Parameters
- **name** (path parameter, required): The name of the operation category
##### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Operation category object
```json
{
    "name": "string",
    "description": "string"
}
```
##### Error Responses
- **404 Not Found**: If the operation category with the specified name does not exist

### Additional Notes

- All endpoints that modify data (POST, PUT, DELETE) will trigger a publication event on the message broker.
- The API uses standard HTTP response codes to indicate success or failure.
- All requests and responses are in JSON format.
- IDs are automatically generated for new drones.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.