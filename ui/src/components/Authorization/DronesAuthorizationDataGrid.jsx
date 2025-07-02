import PropTypes from 'prop-types';
import { DataGrid, GridToolbar } from '@mui/x-data-grid';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import DoNotDisturbOnIcon from '@mui/icons-material/DoNotDisturbOn';
import Tooltip from '@mui/material/Tooltip';

export default function DronesAuthorizationDataGrid({ authorization, onRevoke, drones, geoZones }) {
  const renderAuthorizationStatus = (status) => {
    const colors = {
      GRANTED: 'success',
      DENIED: 'error',
      REVOKED: 'warning',
      EXPIRED: 'default',
    };

    return <Chip label={status} color={colors[status]} size="small" />;
  };

  const renderOperationCategory = (category) => {
    const colors = {
      CERTIFIED: 'error',
      SPECIFIC: 'warning',
    };

    return <Chip label={category} color={colors[category]} size="small" />;
  };

  const renderAuthorizationCategory = (category) => {
    const colors = {
      EXCLUDED: 'error',
      RESTRICTED: 'warning',
    };

    return (
      <Tooltip title={`Category: ${category}`} arrow>
        <Chip label={category} color={colors[category]} size="small" />
      </Tooltip>
    );
  };

  const rows = authorization.map((auth) => {
    const drone = drones.find((d) => d.id === auth.drone_id) || {};
    const geoZone = geoZones.find((g) => g.id === auth.geozone_id) || {};

    return {
      id: auth.id,
      droneId: drone.id,
      droneName: drone.name,
      operationCategory: drone.operation_category,
      geoZoneId: geoZone.id,
      geoZoneName: geoZone.name,
      geoZoneCategory: geoZone.category,
      status: auth.status,
      createdAt: new Date(auth.start_time).toLocaleString(),
      expiresAt: auth.revocation_time
      ? new Date(auth.revocation_time).toLocaleString()
      : auth.end_time
      ? new Date(auth.end_time).toLocaleString()
      : '-',
      reason: auth.reason,
    };
  });

  const columns = [
    {
      field: 'droneId',
      headerName: 'Drone ID',
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`Drone ID: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'droneName',
      headerName: 'Drone Name',
      flex: 1.5,
      renderCell: (params) => (
        <Tooltip title={`Drone Name: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'operationCategory',
      headerName: 'Operation',
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`Operation Category: ${params.value}`} arrow>
          {renderOperationCategory(params.value)}
        </Tooltip>
      ),
    },
    {
      field: 'geoZoneId',
      headerName: 'Geozone ID',
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`Geozone ID: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'geoZoneName',
      headerName: 'Geozone Name',
      flex: 1.5,
      renderCell: (params) => (
        <Tooltip title={`Geozone Name: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'geoZoneCategory',
      headerName: 'Ctegory',
      flex: 1,
      renderCell: (params) => renderAuthorizationCategory(params.value),
    },
    {
      field: 'status',
      headerName: 'Status',
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`Authorization Status: ${params.value}`} arrow>
          {renderAuthorizationStatus(params.value)}
        </Tooltip>
      ),
    },
    {
      field: 'createdAt',
      headerName: 'Created At',
      flex: 1.5,
      renderCell: (params) => (
        <Tooltip title={`Created At: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'expiresAt',
      headerName: 'Expires At',
      flex: 1.5,
      renderCell: (params) => (
        <Tooltip title={`Expires At: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'reason',
      headerName: 'Annotation',
      flex: 2.5,
      renderCell: (params) => (
        <Tooltip title={`Annotation: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'actions',
      headerName: 'Actions',
      sortable: false,
      renderCell: (params) => (
        <div style={{ display: 'flex'}}>
          <Tooltip title="Revoke" arrow>
            <span>
              <Button
                size="small"
                color="secondary"
                onClick={() => onRevoke(params.row.id)}
                style={{ minWidth: 'auto' }}
                disabled={params.row.status !== 'GRANTED'}
              >
                <DoNotDisturbOnIcon />
              </Button>
            </span>
          </Tooltip>
        </div>
      ),
    },
  ];

  return (
    <DataGrid
      autoHeight
      disableRowSelectionOnClick
      slots={{ toolbar: GridToolbar }}
      slotProps={{ toolbar: { csvOptions: { allColumns: true } } }}
      getRowHeight={() => 'auto'}
      rows={rows}
      columns={columns}
      getRowClassName={(params) =>
        params.indexRelativeToCurrentPage % 2 === 0 ? 'even' : 'odd'
      }
      initialState={{
        pagination: { paginationModel: { pageSize: 10 } },
        columns: {
          columnVisibilityModel: {
            id: false,
            droneId: true,
            droneName: true,
            operationCategory: true,
            geoZoneId: true,
            geoZoneName: true,
            geoZoneCategory: true,
            status: true,
            createdAt: true,
            expiresAt: true,
            reason: false,
            actions: true,
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
      }}
    />
  );
}

DronesAuthorizationDataGrid.propTypes = {
  authorization: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.number.isRequired,
      drone_id: PropTypes.string.isRequired,
      geozone_id: PropTypes.string.isRequired,
      status: PropTypes.oneOf(['GRANTED', 'DENIED', 'REVOKED', 'EXPIRED']).isRequired,
      start_time: PropTypes.string.isRequired,
      end_time: PropTypes.string,
      revocation_time: PropTypes.string,
      reason: PropTypes.string,
    })
  ).isRequired,
  onRevoke: PropTypes.func.isRequired,
  drones: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
      operation_category: PropTypes.string.isRequired,
    })
  ).isRequired,
  geoZones: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
      status: PropTypes.string.isRequired,
    })
  ).isRequired,
};
