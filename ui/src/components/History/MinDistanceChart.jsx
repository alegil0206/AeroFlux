import React from 'react';
import { LineChart } from '@mui/x-charts';

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

    // Calcola la lunghezza massima
    const maxSteps = Math.max(...drones.map(d => d.positions.length));

    const results = {};

    drones.forEach((drone, index) => {
      const droneId = drone.positions[0].droneId;
      results[droneId] = [];

      for (let t = 0; t < maxSteps; t++) {
        // Se il drone ha meno posizioni, usa l'ultima
        const currentPos = drone.positions[Math.min(t, drone.positions.length - 1)].position;

        const distances = drones
          .filter((_, i) => i !== index)
          .map(other => {
            const otherPos = other.positions[Math.min(t, other.positions.length - 1)].position;
            return haversineDistance(currentPos, otherPos);
          });

        const minDist = distances.length > 0 ? Math.min(...distances) : 0;
        results[droneId].push({ time: t, distance: minDist });
      }
    });

    return results;
  }

  const data = computeMinDistances(simulation);

  const series = Object.entries(data).map(([droneId, values]) => ({
    id: droneId,
    label: droneId,
    data: values.map(p => p.distance),
    showMark: false
  }));

  const xAxis = [
    {
      id: 'time',
      data: data[Object.keys(data)[0]].map(p => p.time),
      scaleType: 'linear',
      label: 'Step',
    },
  ];

  return (
    <LineChart
      xAxis={xAxis}
      series={series}
      height={400}
      width={800}
      yAxis={[{ label: 'Minimum Distance (m)', scaleType: 'log', min: 1, }]}
    />
  );
};

export default MinDistanceChart;
