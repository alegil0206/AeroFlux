import PropTypes from 'prop-types';
import { DataGrid } from '@mui/x-data-grid';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Tooltip from '@mui/material/Tooltip';
import DeleteIcon from '@mui/icons-material/Delete';
import PenIcon from '@mui/icons-material/Edit';

export default function GeoZoneDataGrid({ data, openEditDialog, onDelete }) {
  // Funzione per il rendering dello stato
  const renderStatus = (status) => {
    const colors = {
      'ACTIVE': 'success',
      'INACTIVE': 'default',
    };

    return (
      <Tooltip title={`Status: ${status}`} arrow>
        <Chip label={status} color={colors[status]} size="small" />
      </Tooltip>
    );
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

  // Definizione delle colonne
  const columns = [
    {
      field: 'id',
      headerName: 'ID',
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`GeoZone ID: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'name',
      headerName: 'Name',
      flex: 2,
      renderCell: (params) => (
        <Tooltip title={`GeoZone Name: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'status',
      headerName: 'Status',
      flex: 1,
      renderCell: (params) => renderStatus(params.value),
    },
    {
      field: 'type',
      headerName: 'Type',
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`GeoZone Type: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'category',
      headerName: 'Category',
      flex: 1,
      renderCell: (params) => renderAuthorizationCategory(params.value),
    },
    {
      field: 'altitude_level',
      headerName: 'Altitude Level',
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`Altitude Level: ${params.row.altitude_level} - Altitude: ${params.row.altitude} m`} arrow>
          <span>{`${params.row.altitude_level} - ${params.row.altitude} m`}</span>
        </Tooltip>
      ),
    },
    {
      field: 'actions',
      headerName: ' ',
      sortable: false,
      renderCell: (params) => (
        <div style={{ display: 'flex', gap: '4px', alignItems: 'center' }}>
          <Tooltip title="Edit" arrow>
            <Button
              size="small"
              color="primary"
              onClick={() => openEditDialog(params.row)}
              style={{ minWidth: 'auto', padding: '4px' }}
            >
              <PenIcon />
            </Button>
          </Tooltip>
          <Tooltip title="Delete" arrow>
            <Button
              size="small"
              color="secondary"
              onClick={() => onDelete(params.row.id)}
              style={{ minWidth: 'auto', padding: '4px' }} 
            >
              <DeleteIcon />
            </Button>
          </Tooltip>
        </div>
      ),
    },
  ];

  return (
    <DataGrid
      autoHeight
      rows={data}
      columns={columns}
      getRowClassName={(params) =>
        params.indexRelativeToCurrentPage % 2 === 0 ? 'even' : 'odd'
      }
      initialState={{
        pagination: { paginationModel: { pageSize: 20 } },
      }}
      pageSizeOptions={[10, 20, 50]}
      disableColumnResize
      density="compact"
    />
  );
}

GeoZoneDataGrid.propTypes = {
  data: PropTypes.array.isRequired,
  openEditDialog: PropTypes.func.isRequired,
  onDelete: PropTypes.func.isRequired,
};
