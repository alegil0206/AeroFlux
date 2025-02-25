import PropTypes from 'prop-types';
import { DataGrid } from '@mui/x-data-grid';
import Tooltip from '@mui/material/Tooltip';

export default function DronesRoutesCard({ data }) {
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
      field: 'source',
      headerName: 'Source',
      flex: 3,
      renderCell: (params) => (
        <Tooltip
          title={`Lat: ${params.row.source.latitude}, Long: ${params.row.source.longitude}`}
          arrow
        >
          <span>
            {`(${params.row.source.latitude.toFixed(3)}, ${params.row.source.longitude.toFixed(3)})`}
          </span>
        </Tooltip>
      ),
    },
    {
      field: 'destination',
      headerName: 'Destination',
      flex: 3,
      renderCell: (params) => (
        <Tooltip
          title={`Lat: ${params.row.destination.latitude}, Long: ${params.row.destination.longitude}`}
          arrow
        >
          <span>
            {`(${params.row.destination.latitude.toFixed(3)}, ${params.row.destination.longitude.toFixed(3)})`}
          </span>
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
    />
  );
}

DronesRoutesCard.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      id: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
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
};
