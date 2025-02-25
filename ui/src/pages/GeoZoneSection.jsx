import { useState, useEffect } from 'react';
import Grid from '@mui/material/Grid2';
import Box from '@mui/material/Box';
import { Button, Typography, Snackbar, Alert } from "@mui/material";
import GeoZoneDataGrid from "../components/GeoZone/GeoZoneDataGrid";
import GeoZoneActivationCard from "../components/GeoZone/GeoZoneActivationCard";
import GeoZoneFormDialog from '../components/GeoZone/EditDialog/GeoZoneFormDialog';
import GeoZonesMap from '../components/GeoZone/GeoZonesMap';


import { fetchGeoZones, addGeoZone, updateGeoZone, deleteGeoZone } from '../services/geoZones';

function GeoZoneSection() {

  const [geoZones, setGeoZones] = useState([]);
  const [isDialogOpen, setIsFormOpen] = useState(false);
  const [editingGeoZone, setEditingGeoZone] = useState(null);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState(null);

  const showSnackbar = (type, message) => {
    setAlertMessage({ type: type, text: message });
    setSnackbarOpen(true);
  };

  const closeSnackbar = () => {
    setSnackbarOpen(false);
    setAlertMessage(null);
  };

  const loadGeoZones = async () => {
    try {
      const data = await fetchGeoZones();
      setGeoZones(data);
    } catch (error) {
      showSnackbar("error", error.message);
    }
  };

  useEffect(() => {
    loadGeoZones();
    const interval = setInterval(loadGeoZones, 10000);
    return () => clearInterval(interval);
  }, []);

  const handleAddOrEdit = async (geoZone) => {
    try {
      if (editingGeoZone) {
        const id = await updateGeoZone(geoZone);
        showSnackbar( "success", `GeoZone ${id} updated successfully.`);
      } else {
        const id = await addGeoZone(geoZone);
        showSnackbar("success", `GeoZone ${id} created successfully.`);
      }
      loadGeoZones();
      setIsFormOpen(false);
      setEditingGeoZone(null);
    } catch (error) {
      showSnackbar("error", `Error: ${error.message}`);
    }
  };

  const handleEdit = (zone) => {
    setEditingGeoZone(zone);
    setIsFormOpen(true);
  };
  
  const handleDelete = async (id) => {
    try {
      await deleteGeoZone(id);
      showSnackbar("success", `GeoZone ${id} deleted successfully.`);
      loadGeoZones();
    } catch (error) {
      showSnackbar("error", `Error: ${error.message}`);
    }
  };

  const handleToggleStatus = async (id) => {
    try {
      const zone = geoZones.find((z) => z.id === id);
      const updatedZone = { ...zone, status: zone.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE' };
      const id_response = await updateGeoZone(updatedZone);
      showSnackbar("success", `GeoZone ${id_response} status updated successfully.`);
      loadGeoZones();
    } catch (error) {
      showSnackbar("error", `Error: ${error.message}`);
    }
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
      <GeoZoneDataGrid data={geoZones} onEdit={handleEdit} onDelete={handleDelete} />

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
        <Alert severity={alertMessage?.type} onClose={closeSnackbar}>
          {alertMessage?.text}
        </Alert>
      </Snackbar>      
    </Box>
  );
}

export default GeoZoneSection;
