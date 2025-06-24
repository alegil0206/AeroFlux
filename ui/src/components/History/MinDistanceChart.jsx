import React from 'react';
import { LineChart } from '@mui/x-charts';
import { axisClasses } from '@mui/x-charts/ChartsAxis';

const MinDistanceChart = ({ simulation }) => {
  function haversineDistance(coord1, coord2) {
    const toRad = deg => (deg * Math.PI) / 180;
    const R = 6371000;
    const dLat = toRad(coord2.latitude - coord1.latitude);
    const dLon = toRad(coord2.longitude - coord1.longitude);
    const lat1 = toRad(coord1.latitude);
    const lat2 = toRad(coord2.latitude);
    const a =
      Math.sin(dLat / 2) ** 2 +
      Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) ** 2;

    return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  }

  function computeMinDistances(simulation) {
    const { drones } = simulation;
    const maxSteps = Math.max(...drones.map(d => d.positions.length));
    const results = {};

    drones.forEach((drone, index) => {
      const droneId = drone.positions[0].droneId;
      results[droneId] = [];

      for (let t = 0; t < maxSteps; t++) {
        if (t >= drone.positions.length) {
          results[droneId].push({ time: t, distance: null });
          continue;
        }

        const currentPos = drone.positions[t].position;

        const distances = drones
          .filter((_, i) => i !== index && t < drones[i].positions.length)
          .map(other => {
            const otherPos = other.positions[t].position;
            return haversineDistance(currentPos, otherPos);
          });

        const minDist = distances.length > 0 ? Math.min(...distances) : null;
        results[droneId].push({ time: t, distance: minDist });
      }
    });

    return results;
  }

  const data = computeMinDistances(simulation);

  const rawSeries = Object.entries(data).map(([droneId, values]) => ({
    id: droneId,
    label: droneId,
    data: values.map(p => p.distance),
    showMark: false
  }));

  const lastValidIndex = rawSeries[0].data.map((_, i) => i)
    .reverse()
    .find(i => rawSeries.some(s => s.data[i] != null));

  const truncatedSeries = rawSeries.map(s => ({
    ...s,
    data: s.data.slice(0, lastValidIndex + 1)
  }));

  const xData = Array.from({ length: lastValidIndex + 1 }, (_, i) => i);

  const xAxis = [
    {
      id: 'time',
      data: xData,
      scaleType: 'linear',
      label: 'Step',
      min: 0,
      max: lastValidIndex
    },
  ];

  return (
    <LineChart
      slotProps={{ legend: { hidden: true } }}
      xAxis={xAxis}
      series={truncatedSeries}
      height={400}
      yAxis={[{ label: 'Minimum Distance (m)', scaleType: 'log', min: 1 }]}
      sx={{
        [`& .${axisClasses.left} .${axisClasses.label}`]: {
          transform: 'translateX(-12px)',
        },
      }}
    />
  );
};

export default MinDistanceChart;
