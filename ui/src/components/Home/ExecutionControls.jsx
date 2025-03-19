import React from "react";
import Box from "@mui/material/Box";
import { Button } from "@mui/material";

import RestartAltIcon from '@mui/icons-material/RestartAlt';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import PauseIcon from '@mui/icons-material/Pause';
import StopIcon from '@mui/icons-material/Stop';

import { useWebSocket } from "../../contexts/WebSocketContext";

const ExecutionControls = () => {
  const { client, executionState } = useWebSocket();

  const sendCommand = (command) => {
    if (client && client.connected) {
      client.publish({ destination: `/app/${command}` });
    }
  };

  return (
    <Box sx={{ mb: 2, display: "flex", justifyContent: "space-between" }}>
      <Button onClick={() => sendCommand("start")} disabled={executionState === "RUNNING"} startIcon={<PlayArrowIcon />} variant="contained">
        Start
      </Button>
      <Button onClick={() => sendCommand("pause")} disabled={executionState !== "RUNNING"} startIcon={<PauseIcon />} variant="contained">
        Pause
      </Button>
      <Button onClick={() => sendCommand("resume")} disabled={executionState !== "PAUSED"} startIcon={<RestartAltIcon />} variant="contained">
        Resume
      </Button>
      <Button onClick={() => sendCommand("stop")} disabled={executionState === "STOPPED"} startIcon={<StopIcon />} variant="contained">
        Stop
      </Button>
    </Box>
  );
};

export default ExecutionControls;
