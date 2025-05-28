import React, { createContext, useContext, useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import { useSettings } from "../contexts/SettingContext";

const WebSocketContext = createContext(null);

export const WebSocketProvider = ({ children }) => {
  const [client, setClient] = useState(null);
  const [logs, setLogs] = useState([]);
  const [executionState, setExecutionState] = useState("unknown");
  const [dronesStatus, setDronesStatus] = useState({});
  const [executionSpeed, setExecutionSpeed] = useState(1);

  const { mainService } = useSettings();

  const sendCommand = (command, data = null) => {
    if (client && client.connected) {
      client.publish({ 
        destination: `/app/${command}`,
        body: JSON.stringify(data),});
    }
    if (command === "start") {
      setLogs([]);
      setDronesStatus({});
    }
  };

  useEffect(() => {
    if (!mainService) return;
    const stompClient = new Client({
      brokerURL: `ws://${mainService}/websocket`,
      reconnectDelay: 5000, 
      onConnect: () => {
        console.log("WebSocket connected!");

        stompClient.subscribe("/topic/logs", (message) => {
          setLogs((prevLogs) => [...prevLogs, JSON.parse(message.body)]);
        });

        stompClient.subscribe("/topic/status", (message) => {
          const newStatus = JSON.parse(message.body);
          setExecutionState(newStatus.execution_state);
          setExecutionSpeed(newStatus.execution_speed);
        });

        stompClient.subscribe("/topic/drone-status", (message) => {
          const newStatus = JSON.parse(message.body);
          setDronesStatus((prevStatus) => ({
            ...prevStatus,
            [newStatus.droneId]: newStatus,
          }));
        });

        stompClient.publish({ destination: "/app/status" });

        setClient(stompClient);
      },
    });

    stompClient.activate();

    return () => {
      stompClient.deactivate();
    };
  }, [mainService]);

  return (
    <WebSocketContext.Provider value={{ client, logs, executionState, executionSpeed, dronesStatus, sendCommand }}>
      {children}
    </WebSocketContext.Provider>
  );
};



export const useWebSocket = () => useContext(WebSocketContext);
