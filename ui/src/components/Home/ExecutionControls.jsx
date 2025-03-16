import React from "react";
import Box from "@mui/material/Box";
import { Button } from "@mui/material";

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
      <Button onClick={() => sendCommand("start")} disabled={executionState === "running"}>
        Start
      </Button>
      <Button onClick={() => sendCommand("pause")} disabled={executionState !== "running"}>
        Pause
      </Button>
      <Button onClick={() => sendCommand("resume")} disabled={executionState !== "paused"}>
        Resume
      </Button>
      <Button onClick={() => sendCommand("stop")} disabled={executionState === "stopped"}>
        Stop
      </Button>
    </Box>
  );
};

export default ExecutionControls;
