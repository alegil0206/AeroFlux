import { useState, useEffect } from "react";
import Grid from "@mui/material/Grid2";
import Box from "@mui/material/Box";
import { Typography, Button, Snackbar, Alert } from "@mui/material";
import DronesMap from "../components/Drone/DronesMap";
import DronesDataGrid from "../components/Drone/DronesDataGrid";
import DronesRoutesCard from "../components/Drone/DronesRoutesCard";
import DronesFormDialog from "../components/Drone/EditDialog/DroneFormDialog";

import { fetchDrones, addDrone, updateDrone, deleteDrone } from "../services/drones";

function DroneSection() {
  const [drones, setDrones] = useState([]);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingDrone, setEditingDrone] = useState(null);
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

  useEffect(() => {
    loadDrones();
    const interval = setInterval(loadDrones, 10000);
    return () => clearInterval(interval);
  }, []);

  const handleAddOrEdit = async (drone) => {
    try {
      if (editingDrone) {
        const id = await updateDrone(editingDrone.id, drone);
        showSnackbar("success", `Drone ${id} updated successfully.`);
      } else {
        const id = await addDrone(drone);
        showSnackbar("success", `Drone ${id} added successfully.`);
      }
      setIsDialogOpen(false);
      setEditingDrone(null);
      loadDrones();
    } catch (error) {
      showSnackbar("error", error.message);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteDrone(id);
      showSnackbar("success", `Drone ${id} deleted successfully.`);
      loadDrones();
    } catch (error) {
      setAlertMessage({ type: "error", text: error.message });
    }
  };

  return (
    <Box sx={{ width: "100%", maxWidth: { sm: "100%", md: "1700px" } }}>
      <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
        Maps
      </Typography>
      <Grid container spacing={1} columns={12} sx={{ mb: (theme) => theme.spacing(2) }}>
        <Grid size={{ xs: 12, lg: 7 }}>
          <DronesMap drones={drones} />
        </Grid>
        <Grid size={{ xs: 12, lg: 5 }}>
          <Box sx={{ mb: 2, display: "flex", justifyContent: "space-between" }}>
            <Button variant="contained" fullWidth onClick={() => setIsDialogOpen(true)}>
              Add New Drone
            </Button>
          </Box>
          <DronesRoutesCard data={drones} />
        </Grid>
      </Grid>
      <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
        Details
      </Typography>
      <DronesDataGrid data={drones} openEditDialog={(drone) => {
        setEditingDrone(drone);
        setIsDialogOpen(true);
      }} onDelete={handleDelete} />

      <DronesFormDialog
          open={isDialogOpen}
          onClose={() => {
            setIsDialogOpen(false);
            setEditingDrone(null);
          }}
          onSave={handleAddOrEdit}
          initialData={editingDrone}
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

export default DroneSection;
