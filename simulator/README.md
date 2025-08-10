# Simulator

## Purpose

Manages simulation control (start, pause, resume, stop), external service configuration, and access to historical simulation data.

---

## Service Configuration API

**Base URL**: `/setting`

### Get All Registered Services

Retrieves a map of all registered services and their corresponding URLs.

```
GET /setting/service
```

###### Response

* **Status Code**: 200 OK
* **Content-Type**: application/json
* **Body**: Map of service names and their URLs

```json
{
  "service": "string",
  "service": "string"
}
```

##### Error Responses

* **500 Internal Server Error**: If an error occurs while retrieving service data.

---

### Update Service URL

Updates the URL associated with a registered service.

```
PUT /setting/service/{serviceName}?newUrl={newUrl}
```

###### Parameters

* `serviceName (string)`: Name of the service to update.
* `newUrl (string)`: New URL for the service.

###### Response

* **Status Code**: 200 OK
* **Content-Type**: empty

##### Error Responses

* **500 Internal Server Error**: If an error occurs while updating the service URL.

---

## Simulation Control API (WebSocket)

**Base WebSocket Endpoint**: `/app`
**Response Topic**: `/topic/status`

### Start Simulation

Starts the simulation with the specified execution speed.

```
MESSAGE /app/start
```

###### Request (JSON Body)

```json
{
  "execution_speed": "integer"
}
```

###### Response (`/topic/status`)

```json
{
  "execution_state": "RUNNING",
  "execution_speed": "integer"
}
```

---

### Stop Simulation

Stops the current simulation.

```
MESSAGE /app/stop
```

###### Response (`/topic/status`)

```json
{
  "execution_state": "STOPPED",
  "execution_speed": "integer"
}
```

---

### Pause Simulation

Pauses the currently running simulation.

```
MESSAGE /app/pause
```

###### Response (`/topic/status`)

```json
{
    "execution_state": "PAUSED",
    "execution_speed": "integer"
}
```

---

### Resume Simulation

Resumes a paused simulation.

```
MESSAGE /app/resume
```

###### Response (`/topic/status`)

```json
{
    "execution_state": "RUNNING",
    "execution_speed": "integer"
}
```

---

### Get Simulation Status

Retrieves the current status of the simulation.

```
MESSAGE /app/status
```

###### Response (`/topic/status`)

```json
{
    "execution_state": "status",
    "execution_speed": "integer"
}
```

---

## Simulation History API

**Base URL**: `/simulation-history`

### Get Simulation History

Returns a list of past simulations.

```
GET /simulation-history
```

###### Response

* **Status Code**: 200 OK
* **Content-Type**: application/json
* **Body**: List of simulations

```json
[
  {
    "id": "string",
    "date": "YYYY-MM-DDTHH:MM:SSZ",
  },
  {
    "id": "124",
    "date": "YYYY-MM-DDTHH:MM:SSZ",
  }
]
```

---

### Get Simulation Details

Returns full details for a specific simulation.

```
GET /simulation-history/{id}
```

###### Path Variable

* `id (string)`: ID of the simulation to retrieve.

###### Response

* **Status Code**: 200 OK
* **Content-Type**: application/json
* **Body**: Full simulation details

```json
{
  "id": "string",
  "date": "YYYY-MM-DDTHH:MM:SSZ",
  "duration": "long",
  "executionSpeed": "integer",
  "logs": [
    {
      "system_id": "string",
      "level": "string",
      "component": "string",
      "event": "string",
      "message": "string",
      "timestamp": "YYYY-MM-DDTHH:MM:SSZ"
    },
  ],
  "drones": [
    {
      "drone_properties": {
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
      },
      "drone_status": [
        {
          "droneId": "string",
          "position": {
            "longitude": "double",
            "latitude": "double",
            "altitude": "double"
          },
          "battery_level": "integer",
          "flight_mode": "string",
          "flight_plan": [
            {
              "longitude": "double",
              "latitude": "double",
              "altitude": "double"
            }
          ],
          "logs": [
            {
              "system_id": "string",
              "level": "string",
              "component": "string",
              "event": "string",
              "message": "string",
              "timestamp": "YYYY-MM-DDTHH:MM:SSZ"
            }
          ]
        }
      ]
    }
  ]
}
```

---

## License

This project is licensed under the Apache License 2.0 – see the [LICENSE](LICENSE) file for details.

---