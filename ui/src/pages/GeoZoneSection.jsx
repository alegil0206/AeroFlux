import { useState, useEffect } from 'react';
import Grid from '@mui/material/Grid2';
import Box from '@mui/material/Box';
import { Button, Typography, Snackbar, Alert } from "@mui/material";
import GeoZoneDataGrid from "../components/GeoZone/GeoZoneDataGrid";
import GeoZoneActivationCard from "../components/GeoZone/GeoZoneActivationCard";
import GeoZoneFormDialog from '../components/GeoZone/EditDialog/GeoZoneFormDialog';
import GeoZonesMap from '../components/GeoZone/GeoZonesMap';
import SupportPointDataGrid from '../components/SupportPoint/SupportPointDataGrid';
import SupportPointFormDialog from '../components/SupportPoint/EditDialog/SupportPointFormDialog';

import { useGeoAwareness } from '../hooks/useGeoAwareness';

function GeoZoneSection() {

  const [isGeoZoneFormDialogOpen, setIsGeoZoneFormDialogOpen] = useState(false);
  const [isSupportPointFormDialogOpen, setIsSupportPointFormDialogOpen] = useState(false);
  const [editingGeoZone, setEditingGeoZone] = useState(null);
  const [editingSupportPoint, setEditingSupportPoint] = useState(null);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState(null);

  const {
  geoZones,
  error,
  fetchGeoZones,
  addGeoZone,
  updateGeoZone,
  deleteGeoZone,
  supportPoints,
  fetchSupportPoints,
  addSupportPoint,
  updateSupportPoint,
  deleteSupportPoint,
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
    fetchSupportPoints();
    const interval = setInterval(() => {
      fetchGeoZones();
      fetchSupportPoints();
    }, 10000);
    return () => clearInterval(interval);
  }, []);

  const handleAddOrEditGeoZone = async (geoZone) => {
    if (editingGeoZone) {
      const id = await updateGeoZone(geoZone);
      if (id)
        showSnackbar( "success", `GeoZone ${id} updated successfully.`);
    } else {
      const id = await addGeoZone(geoZone);
      if (id)
        showSnackbar("success", `GeoZone ${id} created successfully.`);
      }
      setIsGeoZoneFormDialogOpen(false);
      setEditingGeoZone(null);
      fetchGeoZones();
    };
  
  const handleDeleteGeoZone = async (id) => {
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

  const handleAddOrEditSupportPoint = async (supportPoint) => {
    if (editingSupportPoint) {
      const id = await updateSupportPoint(supportPoint);
      if (id)
        showSnackbar("success", `Support Point ${id} updated successfully.`);
    } else {
      const id = await addSupportPoint(supportPoint);
      if (id)
        showSnackbar("success", `Support Point ${id} created successfully.`);
    }
    setIsSupportPointFormDialogOpen(false);
    setEditingSupportPoint(null);
    fetchSupportPoints();
  }

  const handleDeleteSupportPoint = async (id) => {
    const idDeleted = await deleteSupportPoint(id);
    if (idDeleted)
      showSnackbar("success", `Support Point ${id} deleted successfully.`);
    fetchSupportPoints();
  }

  return (
    <Box sx={{ width: '100%', maxWidth: { sm: '100%', md: '1700px' } }}>
      {/* Cards */}
      <Grid container spacing={1} columns={12} sx={{ mb: (theme) => theme.spacing(2) }}>
        <Grid size={{ xs: 12, lg: 8 }}>
          <GeoZonesMap geoZones={ geoZones } supportPoints={ supportPoints }/>
        </Grid>
        <Grid size={{ xs: 12, lg: 4 }}>
          <Box sx={{ mb: 2, display: 'flex', justifyContent: 'space-between', gap: 1}}>
            <Button variant="contained" fullWidth onClick={() => setIsGeoZoneFormDialogOpen(true)}>
              Add New GeoZone
            </Button>
            <Button variant="contained" fullWidth onClick={() => setIsSupportPointFormDialogOpen(true)}>
              Add New Support Point
            </Button>
          </Box>
          <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
            GeoZone Activation
          </Typography>            
          <GeoZoneActivationCard data={geoZones} onToggleStatus={handleToggleStatus} />
          <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
            Support Points
          </Typography>  
          <SupportPointDataGrid data={supportPoints} openEditDialog={ (supportPoint) => {
            setEditingSupportPoint(supportPoint);
            setIsSupportPointFormDialogOpen(true);
          }
          } onDelete={handleDeleteSupportPoint} />
        </Grid>
      </Grid>

      {/* Details Section */}
      <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
        GeoZones Details
      </Typography>
      <GeoZoneDataGrid data={geoZones} openEditDialog={ (geoZone) => {
          setEditingGeoZone(geoZone);
          setIsGeoZoneFormDialogOpen(true);
        }}
        onDelete={handleDeleteGeoZone} />
      <GeoZoneFormDialog
          open={isGeoZoneFormDialogOpen}
          onClose={() => {
            setIsGeoZoneFormDialogOpen(false);
            setEditingGeoZone(null);
          }}
          onSave={handleAddOrEditGeoZone}
          initialData={editingGeoZone}
      />
      <SupportPointFormDialog
        open={isSupportPointFormDialogOpen}
        onClose={() => {
          setIsSupportPointFormDialogOpen(false);
          setEditingSupportPoint(null);
        }}
        onSave={handleAddOrEditSupportPoint}
        initialData={editingSupportPoint}
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
