import { Typography, Snackbar, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import Grid from "@mui/material/Grid2";
import Box from "@mui/material/Box";
import DronesAuthorizationDataGrid from "../components/Authorization/DronesAuthorizationDataGrid";
import AuthorizationRequestCard from "../components/Authorization/AuthorizationRequestCard";
import AuthorizationMap from "../components/Authorization/AuthorizationMap";

import { useGeoAuthorization } from "../hooks/useGeoAuthorization";
import { useDroneIdentification } from "../hooks/useDroneIdentification";
import { useGeoAwareness } from "../hooks/useGeoAwareness";

function AuthorizationSection() {

  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState(null);

  const { 
    geoZones,
    error: geoZonesError,
    fetchGeoZones
  } = useGeoAwareness();

  const {
    drones,
    error: dronesError,
    fetchDrones
  } = useDroneIdentification();

  const { 
    authorization,
    error: authorizationError,
    fetchAuthorizations,
    addAuthorization,
    revokeAuthorization,
  } = useGeoAuthorization();

  const showSnackbar = (type, message) => {
    setAlertMessage({ type: type, text: message });
    setSnackbarOpen(true);
  }

  const closeSnackbar = () => {
    setSnackbarOpen(false);
    setAlertMessage(null);
  }

  useEffect(() => {
    if (geoZonesError) showSnackbar("error", geoZonesError.message);
    if (authorizationError) showSnackbar("error", authorizationError.message);
    if (dronesError) showSnackbar("error", dronesError.message);
  }, [geoZonesError, authorizationError, dronesError]);

  useEffect(() => {
    const loadAllData = async () => {
      await Promise.all([
        fetchDrones(),
        fetchGeoZones(),
        fetchAuthorizations(),
      ]);
    };
  
    loadAllData();
    const interval = setInterval(loadAllData, 10000);
    return () => clearInterval(interval);
  }, []);

  const handleAdd = async (authorizationRequest) => {
    const id = await addAuthorization(authorizationRequest);
    if (id){
      showSnackbar("success", `Authorization ${id} requested successfully.`);
      fetchAuthorizations();
    }
  };

  const handleRevoke = async (id) => {
    
    const revokedId = await revokeAuthorization(id);
    if (revokedId){
      showSnackbar("success", `Authorization ${id} revoked successfully.`);
      fetchAuthorizations();
    }
  };

  return (
    <Box sx={{ width: '100%', maxWidth: { sm: '100%', md: '1700px' } }}>
      {/* Cards */}
      <Grid container spacing={1} columns={12} sx={{ mb: (theme) => theme.spacing(2) }}
      >
        <Grid size={{ xs: 12, lg: 7 }}>
          <AuthorizationMap drones={ drones } geoZones={geoZones} />
        </Grid>
        <Grid size={{ xs: 12, lg: 5 }}>
          <AuthorizationRequestCard drones={drones} geoZones={geoZones} onAdd = {handleAdd} />
        </Grid>
      </Grid>

      <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
        Authorization Requests
      </Typography>
      <DronesAuthorizationDataGrid drones={drones} geoZones={geoZones} authorization={authorization} onRevoke={handleRevoke} />

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

export default AuthorizationSection;
