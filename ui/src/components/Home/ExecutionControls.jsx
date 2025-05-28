import { useEffect, useState } from "react";
import Box from "@mui/material/Box";
import { Button, FormControl, InputLabel, Select, MenuItem } from "@mui/material";

import RestartAltIcon from '@mui/icons-material/RestartAlt';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import PauseIcon from '@mui/icons-material/Pause';
import StopIcon from '@mui/icons-material/Stop';

import { useWebSocket } from "../../contexts/WebSocketContext";

const ExecutionControls = () => {
  const { executionState, executionSpeed, sendCommand } = useWebSocket();
  const [localSpeed, setLocalSpeed] = useState(1);

  useEffect(() => {
    if (executionSpeed != null) {
      setLocalSpeed(executionSpeed);
    }
  }, [executionSpeed]);

  return (
    <Box sx={{ mb: 2, display: "flex", justifyContent: "space-between" }}>
      <Button onClick={() => sendCommand("start", { execution_speed: localSpeed})} disabled={executionState === "RUNNING"} startIcon={<PlayArrowIcon />} variant="contained">
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
      <FormControl>
        <InputLabel id="execution-speed-label">Execution Speed</InputLabel>
        <Select
          labelId="execution-speed-label"
          value={localSpeed}
          label="Execution Speed"
          onChange={(e) => setLocalSpeed(e.target.value)}
          disabled={!["STOPPED", "unknown"].includes(executionState)}
        >
          <MenuItem value={1}>1x</MenuItem>
          <MenuItem value={2}>2x</MenuItem>
          <MenuItem value={3}>3x</MenuItem>
        </Select>
      </FormControl>
    </Box>
  );
};

export default ExecutionControls;
