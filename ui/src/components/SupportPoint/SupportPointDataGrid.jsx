import PropTypes from 'prop-types';
import { DataGrid } from '@mui/x-data-grid';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import DeleteIcon from '@mui/icons-material/Delete';
import PenIcon from '@mui/icons-material/Edit';

export default function SupportPointDataGrid({ data, openEditDialog, onDelete }) {

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
            field: 'action',
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

SupportPointDataGrid.propTypes = {
    data: PropTypes.array.isRequired,
    openEditDialog: PropTypes.func.isRequired,
    onDelete: PropTypes.func.isRequired,
};

