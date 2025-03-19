import PropTypes from 'prop-types';
import { DataGrid } from '@mui/x-data-grid';
import Button from '@mui/material/Button';
import Chip from '@mui/material/Chip';
import Tooltip from '@mui/material/Tooltip';
import DeleteIcon from '@mui/icons-material/Delete';
import PenIcon from '@mui/icons-material/Edit';

export default function DronesDataGrid({ data, openEditDialog, onDelete }) {
  // Funzione per il rendering della categoria operativa
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

  // Definizione delle colonne
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
      flex: 2,
      renderCell: (params) => (
        <Tooltip title={`Name: ${params.value}`} arrow>
          <span>{params.value}</span>
        </Tooltip>
      ),
    },
    {
      field: 'operation_category',
      headerName: 'Operation Category',
      flex: 2,
      renderCell: (params) => renderOperationCategory(params.value),
    },
    {
      field: 'plan_definition_timestamp',
      headerName: 'Plan Definition Date',
      flex: 2,
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
      flex: 2,
      renderCell: (params) => (
        <Tooltip title={`Owner: ${params.value}`} arrow>
          <span>{params.value}</span>
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
              style={{ minWidth: 'auto', padding: '4px' }} // Riduce il padding del bottone
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
      getRowHeight={() => 'auto'}
      rows={data}
      columns={columns}
      getRowClassName={(params) =>
        params.indexRelativeToCurrentPage % 2 === 0 ? 'even' : 'odd'
      }
      initialState={{
        pagination: { paginationModel: { pageSize: 20 } },
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
      }}    />
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
    })
  ).isRequired,
  openEditDialog: PropTypes.func.isRequired,
  onDelete: PropTypes.func.isRequired,
};
