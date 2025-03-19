import React from "react";
import { useWebSocket } from "../../contexts/WebSocketContext";
import { DataGrid } from "@mui/x-data-grid";
import Tooltip from "@mui/material/Tooltip";

const LogViewer = () => {
  const { logs } = useWebSocket();

  const columns = [
    {
      field: "timestamp",
      headerName: "Timestamp",
      flex: 2,
      renderCell: (params) => (
        <Tooltip title={`Logged at: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: "category",
      headerName: "Category",
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`Category: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: "message",
      headerName: "Message",
      flex: 3,
      renderCell: (params) => (
        <Tooltip title={`Message: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
  ];

  const rows = logs.map((log, index) => ({
    id: index,
    timestamp: log.timestamp,
    category: log.category,
    message: log.message,
  }));

  return (
    <div style={{ display: 'flex', flexDirection: 'column',
      width: "100%", height: 'calc(100vh - 200px)' }}>

      <DataGrid
        getRowHeight={() => 'auto'}
        rows={rows} 
        columns={columns}
        getRowClassName={(params) =>
          params.indexRelativeToCurrentPage % 2 === 0 ? "even" : "odd"
        }
        initialState={{
          pagination: { paginationModel: { pageSize: 10 } },
        }}
        pageSizeOptions={[10, 20, 50]}
        disableColumnResize
        density="compact"
        sx={{
          '&.MuiDataGrid-root--densityCompact .MuiDataGrid-cell': {
            py: 1,
          },
          '&.MuiDataGrid-root--densityStandard .MuiDataGrid-cell': {
            py: '15px',
          },
          '&.MuiDataGrid-root--densityComfortable .MuiDataGrid-cell': {
            py: '22px',
          },
        }}      />
    </div>
  );
};

export default LogViewer;
