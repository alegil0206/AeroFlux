# Geo-authorization

## Purpose

Management of drone authorizations for geozones through a RESTful API.

## Authorizations

### Properties
- `id (long)`: unique identifier of the authorization.
- `drone_id (string)`: identifier of the drone requesting authorization.
- `geozone_id (string)`: identifier of the geozone.
- `duration_type (string)`: type of duration. See [Authorization durations](#authorization-durations) for details.
- `duration (integer)`: value of the specified `duration_type`. See [Authorization durations](#authorization-durations) for details.
- `start_time (string)`: timestamp indicating when the authorization starts.
- `end_time (string)`: timestamp indicating when the authorization ends (optional, depending on authorization type).
- `revocation_time (string)`: timestamp indicating when the authorization was revoked (only for revoked authorizations).
- `reason (string)`: reason for denial (only for denied authorizations).

### Example

#### **Authorization Granted**

```json
{
  "status": "GRANTED",
  "id": 1,
  "drone_id": "drone123",
  "drone_operation_category": "CERTIFIED",
  "geozone_id": "geozone456",
  "geozone_category": "RESTRICTED",
  "duration_type": "STANDARD",
  "duration": 60,
  "start_time": "2025-01-21T12:00:00Z",
  "end_time": "2025-01-21T13:00:00Z"
}
```

#### **Authorization Denied**

```json
{
  "status": "DENIED",
  "id": 2,
  "drone_id": "drone124",
  "drone_operation_category": "SPECIFIC",
  "geozone_id": "geozone457",
  "geozone_category": "RESTRICTED",
  "duration_type": "EXTENDED",
  "duration": 120,
  "start_time": "2025-01-21T13:00:00Z",
  "reason": "Unauthorized operation in a restricted zone"
}
```

#### **Authorization Expired**

```json
{
  "status": "EXPIRED",
  "id": 3,
  "drone_id": "drone125",
  "drone_operation_category": "CERTIFIED",
  "geozone_id": "geozone458",
  "geozone_category": "RESTRICTED",
  "duration_type": "TEST",
  "duration": 1,
  "start_time": "2025-01-21T10:00:00Z",
  "end_time": "2025-01-21T10:01:00Z"
}
```

#### **Authorization Revoked**

```json
{
  "status": "REVOKED",
  "id": 4,
  "drone_id": "drone126",
  "drone_operation_category": "SPECIFIC",
  "geozone_id": "geozone459",
  "geozone_category": "RESTRICTED",
  "duration_type": "SHORT",
  "duration": 30,
  "start_time": "2025-01-21T14:00:00Z",
  "end_time": "2025-01-21T14:30:00Z",
  "revocation_time": "2025-01-21T14:10:00Z"
}
```

#### **Authorization Request**

```json
{
  "drone_id": "drone127",
  "geozone_id": "geozone460",
  "duration_type": "STANDARD",
  "duration": 60
}
```

### Authorization durations
#### Test
Authorization with a duration of 1 minute. Use only for testing.
#### Short
Authorization with a duration of 30 minutes.
#### Standard
Authorization with a duration of 60 minute.
#### Extended
Authorization with a duration of 120 minutes.

## API

Base URL: `/authorization`

### Authorization

#### Get All Authorizations
Retrieves a list of all authorizations.
```
GET /authorization
```
##### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Array of authorization objects
```json
[
    {
        "status": "GRANTED",
        "id": 1,
        "drone_id": "drone123",
        "drone_operation_category": "CERTIFIED",
        "geozone_id": "geozone456",
        "geozone_category": "RESTRICTED",
        "duration_type": "STANDARD",
        "duration": 60,
        "start_time": "2025-01-21T12:00:00Z",
        "end_time": "2025-01-21T13:00:00Z"
    },
        "..."
]
```

#### Request New Authorization
Creates a new authorization for a drone in a geozone.
```
POST /authorization
```
##### Request
- **Content-Type**: application/json
- **Body**: Authorization request object
```json
{
    "drone_id": "drone123",
    "geozone_id": "geozone456",
    "duration_type": "STANDARD",
    "duration": 60
}
```
##### Response
- **Status Code**: 201 Created
- **Headers**: Location: /authorization/{id}
- **Content-Type**: application/json
- **Body**: Created authorization object
##### Error Responses
- **400 Bad Request**: If the request body is invalid.
- **403 Forbidden**: If the authorization cannot be granted due to business rules.
- **404 Not Found**: If the drone or geozone does not exist.
##### Payload
Both `duration_type` and `duration` are optional:
- `duration_type` has priority over `duration` if both are provided.
- if neither `duration_type` nor `duration` are provided, a `standard` duration is used. 

#### Delete All Authorizations
Deletes all authorizations.
```
DELETE /authorization
```
##### Response
- **Status Code**: 204 No Content

#### Get Authorization by ID
Retrieves a specific authorization by its ID.
```
GET /authorization/{id}
```
##### Parameters
- **id** (path parameter, required): The ID of the authorization
##### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Authorization object
##### Error Responses
- **404 Not Found**: If the authorization with the specified ID does not exist.

#### Get Authorizations by Drone
Retrieves all authorizations associated with a specific drone.
```
GET /authorization/drone/{droneId}
```
##### Parameters
- **droneId** (path parameter, required): The ID of the drone
##### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Array of authorization objects
##### Error Responses
- **404 Not Found**: If the drone with the specified ID does not exist.

#### Get Authorizations by Geozone
Retrieves all authorizations associated with a specific geozone.
```
GET /authorization/geozone/{geozoneId}
```
##### Parameters
- **geozoneId** (path parameter, required): The ID of the geozone
##### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Array of authorization objects
##### Error Responses
- **404 Not Found**: If the geozone with the specified ID does not exist.

#### Revoke Authorization
Revokes an existing authorization by its ID.
```
POST /authorization/revoked/{id}
```
##### Parameters
- **id** (path parameter, required): The ID of the authorization to revoke
##### Response
- **Status Code**: 201 Created
- **Headers**: Location: /authorization/{id}
- **Content-Type**: application/json
- **Body**: Revoked authorization object
##### Error Responses
- **403 Forbidden**: If the authorization cannot be revoked.
- **404 Not Found**: If the authorization with the specified ID does not exist.

### Duration

Base URL: `/authorization/duration`

#### Get All Duration Types
Retrieves a list of all available duration types.
```
GET /authorization/duration
```
##### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Array of duration type objects
```json
[
  {
    "name": "TEST",
    "description": "..."
  },
  {
    "name": "STANDARD",
    "description": "..."
  }
]
```

#### Get Duration Type by Name
Retrieves details of a specific duration type.
```
GET /authorization/duration/{name}
```
##### Parameters
- **name** (path parameter, required): The name of the duration type
##### Response
- **Status Code**: 200 OK
- **Content-Type**: application/json
- **Body**: Duration type object
```json
{
    "name": "EXTENDED",
    "description": "..."
}
```
##### Error Responses
- **404 Not Found**: If the duration type with the specified name does not exist.

### Additional Notes
- The API uses standard HTTP response codes to indicate success or failure.
- All requests and responses are in JSON format.
- IDs are automatically generated for new authorizations.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.