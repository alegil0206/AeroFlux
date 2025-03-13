import PropTypes from 'prop-types';
import { DataGrid } from '@mui/x-data-grid';
import Chip from '@mui/material/Chip';
import Tooltip from '@mui/material/Tooltip';

export default function DronesPositionCard({ data }) {
  const columns = [
    {
      field: 'id',
      headerName: 'ID',
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`Drone ID: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'name',
      headerName: 'Name',
      flex: 2,
      renderCell: (params) => (
        <Tooltip title={`Drone Name: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'latitude',
      headerName: 'Latitude',
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`Latitude: ${params.value}`} arrow>
          <span>{params.value.toFixed(3)}</span>
        </Tooltip>
      ),
    },
    {
      field: 'longitude',
      headerName: 'Longitude',
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`Longitude: ${params.value}`} arrow>
          <span>{params.value.toFixed(3)}</span>
        </Tooltip>
      ),
    },
    {
      field: 'height',
      headerName: 'Flight Level',
      flex: 1,
      renderCell: (params) => (
        <Tooltip title={`Flight Level: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'status',
      headerName: 'Status',
      flex: 2,
      renderCell: (params) => {
        const isFlying = params.row.height > 0;
        const label = isFlying ? 'In Flight' : 'On Ground';
        return (
          <Tooltip title={label} arrow>
            <Chip
              label={label}
              color={isFlying ? 'success' : 'default'}
              size="small"
            />
          </Tooltip>
        );
      },
    },
  ];

  // Mappare i dati per separare la posizione in latitude, longitude e height
  const rows = data.map((item) => ({
    id: item.id,
    name: item.name,
    latitude: item.position.latitude,
    longitude: item.position.longitude,
    height: item.position.height,
    status: item.status,
  }));

  return (
    <DataGrid
      autoHeight
      rows={rows}
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

DronesPositionCard.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
      position: PropTypes.shape({
        latitude: PropTypes.number.isRequired,
        longitude: PropTypes.number.isRequired,
        height: PropTypes.number.isRequired,
      }).isRequired,
    })
  ).isRequired,
};
