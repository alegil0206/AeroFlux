import { Typography, Snackbar, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import Grid from "@mui/material/Grid2";
import Box from "@mui/material/Box";
import DronesPositionsCard from "../components/Home/DronesPositionsCard";
import DronesAuthorizationDataGrid from "../components/Home/DronesAuthorizationDataGrid";
import AuthorizationRequestCard from "../components/Home/AuthorizationRequestCard";
import FullMap from "../components/Home/FullMap";

import { useGeoAuthorization } from "../hooks/useGeoAuthorization";
import { useDroneIdentification } from "../hooks/useDroneIdentification";
import { useGeoAwareness } from "../hooks/useGeoAwareness";
import { useWeather } from "../hooks/useWeather";

function HomeSection() {

  const [snackbarOpen, setSnackbarOpen] = useState(false);
  const [alertMessage, setAlertMessage] = useState(null);

  const [dronesPositions, setDronesPositions] = useState([]);

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

  const { 
    weather,
    error: weatherError, 
    fetchWeather} = useWeather();

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
    if (weatherError) showSnackbar("error", weatherError.message);
  }, [geoZonesError, authorizationError, dronesError, weatherError]);

  useEffect(() => {
    const dronesWithPosition = drones.map((drone) => ({
      ...drone,
      position: { ...drone.source, height: 0 },
    }));
    setDronesPositions(dronesWithPosition);
  }, [drones]);

  useEffect(() => {
    const loadAllData = async () => {
      await Promise.all([
        fetchDrones(),
        fetchGeoZones(),
        fetchAuthorizations(),
        fetchWeather()
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
      <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
        Maps
      </Typography>
      <Grid container spacing={1} columns={12} sx={{ mb: (theme) => theme.spacing(2) }}
      >
        <Grid size={{ xs: 12, lg: 7 }}>
          <FullMap drones={ dronesPositions } geoZones={geoZones} weather={weather}/>
        </Grid>
        <Grid size={{ xs: 12, lg: 5 }}>
          <DronesPositionsCard data={dronesPositions} />
          <AuthorizationRequestCard drones={dronesPositions} geoZones={geoZones} onAdd = {handleAdd} />
        </Grid>
      </Grid>

      <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
        Authorization Requests
      </Typography>
      <DronesAuthorizationDataGrid drones={dronesPositions} geoZones={geoZones} authorization={authorization} onRevoke={handleRevoke} />

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
