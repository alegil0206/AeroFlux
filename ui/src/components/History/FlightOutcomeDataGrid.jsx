import React from "react";
import { DataGrid } from "@mui/x-data-grid";
import Tooltip from '@mui/material/Tooltip';

const relevantEvents = new Set([
  "GEOZONE_ENTERED",
  "BATTERY_CRITICAL",
  "RAIN_CELL_ENTERED",
  "INSUFFICIENT_BATTERY",
  "UNSAFE_FLIGHT"
]);

function extractReasonFromLogs(drone) {
  for (let i = drone.positions.length - 1; i >= 0; i--) {
    const stepLogs = drone.positions[i].logs || [];
    for (let j = stepLogs.length - 1; j >= 0; j--) {
      const log = stepLogs[j];
      if (relevantEvents.has(log.event)) {
        return {event: log.event, message: log.message};
      }
    }
  }
  return null;
}

function analyzeFlightOutcomes(simulation) {
  const outcomes = [];

  simulation.drones.forEach((drone) => {
    const lastPos = drone.positions[drone.positions.length - 1];
    const flightMode = lastPos.flightMode;

    let reason = null;
    if (flightMode === "EMERGENCY_LANDING" || flightMode === "REROUTE_FLIGHT") {
      reason = extractReasonFromLogs(drone);
    }

    outcomes.push({
      id: drone.positions[0].droneId,
      droneId: drone.positions[0].droneId,
      outcome: flightMode,
      reason: reason?.event ?? null,
      message: reason?.message ?? null
    });
  });

  return outcomes;
}

const FlightOutcomeDataGrid = ({ simulation }) => {
  const data = analyzeFlightOutcomes(simulation);

  const columns = [
    { field: "droneId", headerName: "Drone ID", flex: 1,
      renderCell: (params) => (
        <Tooltip title={`Drone ID: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      )
     },
    { field: "outcome", headerName: "Outcome", flex: 1,
      renderCell: (params) => (
        <Tooltip title={`Outcome: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      )
    },
    { field: "reason", headerName: "Annotations", flex: 1,
      renderCell: (params) => (
        <Tooltip title={`${params.value}: ${params.row.message}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      )
    },
  ];

  return (
    <DataGrid
      autoHeight
      rows={data} 
      columns={columns}
      pageSize={10} 
      disableColumnResize
    />
  );
};

export default FlightOutcomeDataGrid;
