import PropTypes from 'prop-types';
import { DataGrid, GridToolbar } from '@mui/x-data-grid';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Tooltip from '@mui/material/Tooltip';
import DeleteIcon from '@mui/icons-material/Delete';
import PenIcon from '@mui/icons-material/Edit';

export default function DronesDataGrid({ data, openEditDialog, onDelete }) {
  const renderOperationCategory = (category) => {
    const colors = {
      CERTIFIED: 'error',
      SPECIFIC: 'warning',
    };

    return (
      <Tooltip title={`Category: ${category}`} arrow>
        <Chip label={category} color={colors[category]} size="small" />
      </Tooltip>
    );
  };

  const columns = [
    {
      field: 'id',
      headerName: 'ID',
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`ID: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'name',
      headerName: 'Name',
      flex: 1.5,
      renderCell: (params) => (
        <Tooltip title={`Name: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'operation_category',
      headerName: 'Operation',
      flex: 1.5,
      renderCell: (params) => renderOperationCategory(params.value),
    },
    {
      field: 'source',
      headerName: 'Source Coordinates',
      flex: 3,
      renderCell: (params) => (
        <Tooltip
          title={`Lat: ${params.row.source.latitude}, Long: ${params.row.source.longitude}`}
          arrow
        >
          <span>
            {`${params.row.source.latitude}, ${params.row.source.longitude}`}
          </span>
        </Tooltip>
      ),
    },
    {
      field: 'destination',
      headerName: 'Destination Coordinates',
      flex: 3,
      renderCell: (params) => (
        <Tooltip
          title={`Lat: ${params.row.destination.latitude}, Long: ${params.row.destination.longitude}`}
          arrow
        >
          <span>
            {`${params.row.destination.latitude}, ${params.row.destination.longitude}`}
          </span>
        </Tooltip>
      ),
    },
    {
      field: 'battery',
      headerName: 'Battery (mAh)',
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`Battery Capacity: ${params.value} mAh`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'plan_definition_timestamp',
      headerName: 'Plan Definition Date',
      flex: 1.5,
      renderCell: (params) => (
        <Tooltip
          title={`Plan Date: ${new Date(params.value).toLocaleString()}`}
          arrow
        >
          <span>{new Date(params.value).toLocaleString()}</span>
        </Tooltip>
      ),
    },
    {
      field: 'owner',
      headerName: 'Owner',
      flex: 1.5,
      renderCell: (params) => (
        <Tooltip title={`Owner: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'model',
      headerName: 'Model',
      flex: 1.5,
      renderCell: (params) => (
        <Tooltip title={`Model: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'actions',
      headerName: 'Actions',
      sortable: false,
      renderCell: (params) => (
        <div style={{ display: 'flex', alignItems: 'center' }}>
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
      disableRowSelectionOnClick
      slots={{ toolbar: GridToolbar }}
      getRowHeight={() => 'auto'}
      rows={data}
      columns={columns}
      getRowClassName={(params) =>
        params.indexRelativeToCurrentPage % 2 === 0 ? 'even' : 'odd'
      }
      initialState={{
        pagination: { paginationModel: { pageSize: 20 } },
        columns: {
          columnVisibilityModel: {
            id: true,
            name: true,
            model: false,
            operation_category: true,
            plan_definition_timestamp: false,
            owner: false,
            actions: true,
            source: true,
            destination: true,
            battery: true,
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

DronesDataGrid.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
      model: PropTypes.string.isRequired,
      operation_category: PropTypes.string.isRequired,
      plan_definition_timestamp: PropTypes.string.isRequired,
      owner: PropTypes.string.isRequired,
      source: PropTypes.shape({
        latitude: PropTypes.number.isRequired,
        longitude: PropTypes.number.isRequired,
      }).isRequired,
      destination: PropTypes.shape({
        latitude: PropTypes.number.isRequired,
        longitude: PropTypes.number.isRequired,
      }).isRequired,
    })
  ).isRequired,
  openEditDialog: PropTypes.func.isRequired,
  onDelete: PropTypes.func.isRequired,
};
