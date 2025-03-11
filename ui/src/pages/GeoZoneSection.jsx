import { useState, useEffect } from 'react';
import Grid from '@mui/material/Grid2';
import Box from '@mui/material/Box';
import { Button, Typography, Snackbar, Alert } from "@mui/material";
import GeoZoneDataGrid from "../components/GeoZone/GeoZoneDataGrid";
import GeoZoneActivationCard from "../components/GeoZone/GeoZoneActivationCard";
import GeoZoneFormDialog from '../components/GeoZone/EditDialog/GeoZoneFormDialog';
import GeoZonesMap from '../components/GeoZone/GeoZonesMap';

import { useGeoAwareness } from '../hooks/useGeoAwareness';

function GeoZoneSection() {

  const [isDialogOpen, setIsFormOpen] = useState(false);
  const [editingGeoZone, setEditingGeoZone] = useState(null);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState(null);

  const {
  geoZones,
  error,
  fetchGeoZones,
  addGeoZone,
  updateGeoZone,
  deleteGeoZone,
  } = useGeoAwareness();

  const showSnackbar = (type, message) => {
    setAlertMessage({ type: type, text: message });
    setSnackbarOpen(true);
  };

  const closeSnackbar = () => {
    setSnackbarOpen(false);
    setAlertMessage(null);
  };

  useEffect(() => {
    if (error) showSnackbar("error", error.message);
  }, [error]);

  useEffect(() => {
    fetchGeoZones();
    const interval = setInterval(fetchGeoZones, 10000);
    return () => clearInterval(interval);
  }, []);

  const handleAddOrEdit = async (geoZone) => {
    if (editingGeoZone) {
      const id = await updateGeoZone(geoZone);
      if (id)
        showSnackbar( "success", `GeoZone ${id} updated successfully.`);
    } else {
      const id = await addGeoZone(geoZone);
      if (id)
        showSnackbar("success", `GeoZone ${id} created successfully.`);
      }
      setIsFormOpen(false);
      setEditingGeoZone(null);
      fetchGeoZones();
    };
  
  const handleDelete = async (id) => {
    const idDeleted = await deleteGeoZone(id);
    if (idDeleted)
      showSnackbar("success", `GeoZone ${id} deleted successfully.`);
    fetchGeoZones();

  };

  const handleToggleStatus = async (id) => {
    const zone = geoZones.find((z) => z.id === id);
    const updatedZone = { ...zone, status: zone.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE' };
    const idUpdated = await updateGeoZone(updatedZone);
    if (idUpdated)  
      showSnackbar("success", `GeoZone ${idUpdated} status updated successfully.`);
    fetchGeoZones();
  };

  return (
    <Box sx={{ width: '100%', maxWidth: { sm: '100%', md: '1700px' } }}>
      {/* Cards */}
      <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
        Maps
      </Typography>
      <Grid container spacing={1} columns={12} sx={{ mb: (theme) => theme.spacing(2) }}>
        <Grid size={{ xs: 12, lg: 9 }}>
          <GeoZonesMap geoZones={ geoZones }/>
        </Grid>
        <Grid size={{ xs: 12, lg: 3 }}>
          <Box sx={{ mb: 2, display: 'flex', justifyContent: 'space-between' }}>
            <Button variant="contained" fullWidth onClick={() => setIsFormOpen(true)}>
              Add New GeoZone
            </Button>
          </Box>
          <GeoZoneActivationCard data={geoZones} onToggleStatus={handleToggleStatus} />
        </Grid>
      </Grid>

      {/* Details Section */}
      <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
        GeoZones Details
      </Typography>
      <GeoZoneDataGrid data={geoZones} openEditDialog={ (geoZone) => {
          setEditingGeoZone(geoZone);
          setIsFormOpen(true);
        }}
        onDelete={handleDelete} />
      <GeoZoneFormDialog
          open={isDialogOpen}
          onClose={() => {
            setIsFormOpen(false);
            setEditingGeoZone(null);
          }}
          onSave={handleAddOrEdit}
          initialData={editingGeoZone}
      />
      <Snackbar
        open={snackbarOpen}
        autoHideDuration={5000}
        onClose={closeSnackbar}
        anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
      >
        <Alert severity={alertMessage?.type} onClose={closeSnackbar} variant="filled">
          {alertMessage?.text}
        </Alert>
      </Snackbar>      
    </Box>
  );
}

export default GeoZoneSection;
