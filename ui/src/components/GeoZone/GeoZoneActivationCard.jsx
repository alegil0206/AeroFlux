import PropTypes from 'prop-types';
import { DataGrid } from '@mui/x-data-grid';
import Switch from '@mui/material/Switch';
import Tooltip from '@mui/material/Tooltip';
import Chip from '@mui/material/Chip';


export default function GeoZoneActivationCard({ data, onToggleStatus }) {
  
  const renderAuthorizationCategory = (category) => {
    const colors = {
      EXCLUDED: 'error',
      RESTRICTED: 'warning',
    };

    return (
      <Tooltip title={`Category: ${category}`} arrow>
        <Chip label={" "} color={colors[category]} size="small" />
      </Tooltip>
    );
  };

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
      field: 'category',
      headerName: 'Cat.',
      flex: 1,
      renderCell: (params) => renderAuthorizationCategory(params.value),
    },
    {
      field: 'status',
      headerName: 'Activate/Deactivate',
      flex: 1.5,
      
      renderCell: (params) => (
        <Tooltip
          title={
            params.row.status === 'ACTIVE'
              ? 'Click to deactivate this GeoZone'
              : 'Click to activate this GeoZone'
          }
          arrow
        >
          <Switch
            checked={params.row.status === 'ACTIVE'}
            onChange={() => onToggleStatus(params.row.id)}
            color="primary"
          />
        </Tooltip>
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
      sx={{ mt: 2}}
    />
  );
}

GeoZoneActivationCard.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
      status: PropTypes.string.isRequired,
      category: PropTypes.string.isRequired,
    })
  ).isRequired,
  onToggleStatus: PropTypes.func.isRequired,
};
