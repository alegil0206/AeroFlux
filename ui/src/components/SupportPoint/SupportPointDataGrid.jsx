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
            flex: 1.5,
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
          field: 'position',
          headerName: 'Position',
          flex: 3,
          renderCell: (params) => (
            <Tooltip
              title={`Lat: ${params.row.latitude}, Long: ${params.row.longitude}`}
              arrow
            >
              <span>
                {`${params.row.latitude.toFixed(6)}, ${params.row.longitude.toFixed(6)}`}
              </span>
            </Tooltip>
          ),
        },
        {
            field: 'action',
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
        />
    );
}

SupportPointDataGrid.propTypes = {
    data: PropTypes.array.isRequired,
    openEditDialog: PropTypes.func.isRequired,
    onDelete: PropTypes.func.isRequired,
};

