import React from 'react';
import { PieChart } from '@mui/x-charts';

function countFinalFlightModes(simulation) {
  const outcomes = {};

  simulation.drones.forEach(drone => {
    const positions = drone.positions;
    if (!positions.length) return;

    const lastMode = positions[positions.length - 1].flightMode;
    outcomes[lastMode] = (outcomes[lastMode] || 0) + 1;
  });

  return outcomes;
}

const FlightOutcomeCharts = ({ simulation }) => {
  const rawOutcomes = countFinalFlightModes(simulation);

  const pieData = Object.entries(rawOutcomes).map(([key, value]) => ({
    id: key,
    value,
    label: key,
  }));

  return (
        <PieChart
          margin={{ top: 10, bottom: 10, left: 10, right:200 }}
          series={[{ data: pieData,           
            highlightScope: { fade: 'global', highlight: 'item' },
            faded: { innerRadius: 30, additionalRadius: -30, color: 'gray' },
          }]}
          height={300}
          slotProps={{
            legend: {
              direction: 'column',
              position: {
                vertical: 'middle',
                horizontal: 'right',
              },
              padding: 0,
              itemMarkWidth: 10,
              itemMarkHeight: 10,
              markGap: 5,
              itemGap: 10,
              labelStyle: {
                fontSize: 14,            
              },
            }
        }}
        />

  );
};

export default FlightOutcomeCharts;
