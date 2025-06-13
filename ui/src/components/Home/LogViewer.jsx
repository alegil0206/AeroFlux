import React from "react";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import Tooltip from "@mui/material/Tooltip";
import PropTypes from "prop-types";

const LogViewer = ({logs}) => {

  const columns = [
    {
      field: "timestamp",
      headerName: "Timestamp",
      flex: 1,
      hideable: false,
      renderCell: (params) => (
        <Tooltip title={`Logged at: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: "system",
      headerName: "System",
      flex: 1,
      hideable: false,
      renderCell: (params) => (
        <Tooltip title={`System: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: "level",
      headerName: "Level",
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`Level: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: "component",
      headerName: "Component",
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`Component: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: "event",
      headerName: "Event",
      flex: 1,
      hideable: false,
      renderCell: (params) => (
        <Tooltip title={`Event: ${params.value}`} arrow>
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
    system: log.systemId,
    level: log.level,
    component: log.component,
    event: log.event,
    message: log.message,
  }));

  return (
    <div style={{ display: 'flex', flexDirection: 'column',
      width: "100%", height: 'calc(100vh - 200px)' }}>

      <DataGrid
        getRowHeight={() => 'auto'}
        disableRowSelectionOnClick
        slots={{ toolbar: GridToolbar }}
        rows={rows} 
        columns={columns}
        getRowClassName={(params) =>
          params.indexRelativeToCurrentPage % 2 === 0 ? "even" : "odd"
        }
        initialState={{
          pagination: { paginationModel: { pageSize: 20 } },
          columns: {
            columnVisibilityModel: {
              id: false,
              timestamp: true,
              system: true,
              level: false,
              component: false,
              event: true,
              message: false,
            },
          },
        }}
        pageSizeOptions={[10, 20, 50]}
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

LogViewer.propTypes = {
  logs: PropTypes.arrayOf(
    PropTypes.shape({
      timestamp: PropTypes.string.isRequired,
      systemId: PropTypes.string.isRequired,
      level: PropTypes.string.isRequired,
      component: PropTypes.string.isRequired,
      event: PropTypes.string.isRequired,
      message: PropTypes.string.isRequired,
    }).isRequired
  )
};

export default LogViewer;
