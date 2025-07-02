import PropTypes from 'prop-types';
import { DataGrid, GridToolbar } from '@mui/x-data-grid';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Tooltip from '@mui/material/Tooltip';
import DeleteIcon from '@mui/icons-material/Delete';
import PenIcon from '@mui/icons-material/Edit';

export default function GeoZoneDataGrid({ data, openEditDialog, onDelete }) {
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

  const renderGeozoneCategory = (category) => {
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

  const renderShapeDescription = (row) => {
    if (row.type === 'CIRCULAR') {
      return `The geo-zone has center (${row.latitude}, ${row.longitude}) and radius ${row.radius} m`;
    }
    if (row.coordinates) {
      return `The geo-zone is bounded by the following coordinates: ${row.coordinates.map(coord => `(${coord[0]}, ${coord[1]})`).join(', ')}`;
    }
    return 'Shape information not available';
  };

  const columns = [
    {
      field: 'id',
      headerName: 'ID',
      flex: 1,
      hideable: false,
      renderCell: (params) => (
        <Tooltip title={`GeoZone ID: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'name',
      headerName: 'Name',
      flex: 1,
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
      renderCell: (params) => renderGeozoneCategory(params.value),
    },
    {
      field: 'altitude_level_limit_inferior',
      headerName: 'Altitude Level Limit Inferior',
      flex: 1,
      valueGetter: (value, row) => `${row.altitude_level_limit_inferior} - ${row.altitude_limit_inferior} m`,
      renderCell: (params) => (
        <Tooltip title={`Altitude Level: ${params.row.altitude_level_limit_inferior} - Altitude: ${params.row.altitude_limit_inferior} m`} arrow>
          <span>{`${params.row.altitude_level_limit_inferior} - ${params.row.altitude_limit_inferior} m`}</span>
        </Tooltip>
      ),
    },
    {
      field: 'altitude_level_limit_superior',
      headerName: 'Altitude Level Limit Superior',
      flex: 1,
      valueGetter: (value, row) => `${row.altitude_level_limit_superior} - ${row.altitude_limit_superior} m`,
      renderCell: (params) => (
        <Tooltip title={`Altitude Level: ${params.row.altitude_level_limit_superior} - Altitude: ${params.row.altitude_limit_superior} m`} arrow>
          <span>{`${params.row.altitude_level_limit_superior} - ${params.row.altitude_limit_superior} m`}</span>
        </Tooltip>
      ),
    },
    {
      field: 'shape_description',
      headerName: 'Description',
      flex: 3,
      valueGetter: (value, row) => renderShapeDescription(row),
      renderCell: (params) => (
        <Tooltip title={renderShapeDescription(params.row)} arrow>
          <span>{renderShapeDescription(params.row)}</span>
        </Tooltip>
      ),
    },
    {
      field: 'actions',
      headerName: 'Actions',
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
      disableRowSelectionOnClick
      slots={{ toolbar: GridToolbar }}
      slotProps={{ toolbar: { csvOptions: { allColumns: true } } }}
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
            status: true,
            type: true,
            category: true,
            altitude_level_limit_inferior: true,
            altitude_level_limit_superior: true,
            shape_description: false,
            actions: true,
          },
        },
      }}
      pageSizeOptions={[10, 20, 50]}
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
      }}
    />
  );
}

GeoZoneDataGrid.propTypes = {
  data: PropTypes.array.isRequired,
  openEditDialog: PropTypes.func.isRequired,
  onDelete: PropTypes.func.isRequired,
};
