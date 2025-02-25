import { Typography, Snackbar, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import Grid from "@mui/material/Grid2";
import Box from "@mui/material/Box";
import DronesPositionsCard from "../components/Home/DronesPositionsCard";
import DronesAuthorizationDataGrid from "../components/Home/DronesAuthorizationDataGrid";
import AuthorizationRequestCard from "../components/Home/AuthorizationRequestCard";
import FullMap from "../components/Home/FullMap";
import { fetchDrones } from "../services/drones";
import { fetchGeoZones } from "../services/geoZones";
import { fetchAuthorizations, addAuthorization, revokeAuthorization } from "../services/authorization";

function HomeSection() {

  const [drones, setDrones] = useState([]);
  const [geoZones, setGeoZones] = useState([]);
  const [authorization, setAuthorization] = useState([]);
  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState(null);

  const showSnackbar = (type, message) => {
    setAlertMessage({ type: type, text: message });
    setSnackbarOpen(true);
  }

  const closeSnackbar = () => {
    setSnackbarOpen(false);
    setAlertMessage(null);
  }

  const loadDrones = async () => {
    try {
      const data = await fetchDrones();
      const dronesWithPosition = data.map((drone) => ({
        ...drone,
        position: { ...drone.source, height: 0 },
      }));
      setDrones(dronesWithPosition);
    } catch (error) {
      showSnackbar("error", error.message);
    }
  };

  const loadGeoZones = async () => {
    try {
      const data = await fetchGeoZones();
      setGeoZones(data);
    } catch (error) {
      showSnackbar("error", error.message);
    }
  };

  const loadAuthorizations = async () => {
    try {
      const data = await fetchAuthorizations();
      setAuthorization(data);
    } catch (error) {
      showSnackbar("error", error.message);
    }
  }

  useEffect(() => {
    const loadAllData = async () => {
      await Promise.all([
        loadDrones(),
        loadGeoZones(),
        loadAuthorizations(),
      ]);
    };
  
    loadAllData();
  
    const interval = setInterval(loadAllData, 10000);
    return () => clearInterval(interval);
  }, []);

  const handleAdd = async (authorizationRequest) => {
    try {
      const id = await addAuthorization(authorizationRequest);
      showSnackbar("success", `Authorization ${id} requested successfully.`);
      loadAuthorizations();
    } catch (error) {
      showSnackbar("error", error.message);
    }
  };

  const handleRevoke = async (id) => {
    try {
      await revokeAuthorization(id);
      showSnackbar("success", `Authorization ${id} revoked successfully.`);
      loadAuthorizations();
    } catch (error) {
      showSnackbar("error", error.message);
    }
  };

  return (
    <Box sx={{ width: '100%', maxWidth: { sm: '100%', md: '1700px' } }}>
      {/* Cards */}
      <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
        Maps
      </Typography>
      <Grid container spacing={1} columns={12} sx={{ mb: (theme) => theme.spacing(2) }}
      >
        <Grid size={{ xs: 12, lg: 7 }}>
          <FullMap drones={ drones } geoZones={geoZones}/>
        </Grid>
        <Grid size={{ xs: 12, lg: 5 }}>
          <DronesPositionsCard data={drones} />
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

export default HomeSection;
