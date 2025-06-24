import React from "react";
import { LineChart } from "@mui/x-charts";
import { axisClasses } from '@mui/x-charts/ChartsAxis';


const BatteryLevelChart = ({ simulation }) => {
  const { drones } = simulation;

  const series = drones.map((drone) => {
    const droneId = drone.positions[0].droneId;
    const data = drone.positions.map((step) => step.batteryLevel ?? null);

    return {
      id: droneId,
      label: droneId,
      data,
      showMark: false,
    };
  });

  const maxSteps = Math.max(...series.map(s => s.data.length));

  const xData = Array.from({ length: maxSteps }, (_, i) => i);

  const xAxis = [
    {
      id: "step",
      label: "Step",
      data: xData,
      scaleType: "linear",
      min: 0,
      max: maxSteps - 1,
    },
  ];

  return (
    <LineChart
      slotProps={{ legend: { hidden: true } }}
      xAxis={xAxis}
      series={series}
      height={400}
      yAxis={[
        {
          label: "Battery Level (mAh)",
          min: 1,
          scaleType: "log",
        },
      ]}
      sx={{
        [`& .${axisClasses.left} .${axisClasses.label}`]: {
          transform: 'translateX(-12px)',
        },
      }}
    />
  );
};

export default BatteryLevelChart;
