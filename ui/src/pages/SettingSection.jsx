import Box from "@mui/material/Box";
import Grid from "@mui/material/Grid2";
import Card from "@mui/material/Card";
import { CardContent, CardHeader } from "@mui/material";
import Button from "@mui/material/Button";
import { deleteAllDrones } from "../services/drones";
import { deleteAllGeoZones } from "../services/geoZones";
import { deleteAllAuthorizations } from "../services/authorization";
import { Snackbar, Alert } from "@mui/material";
import { useState } from "react";


function SettingSetion() {

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
    
    const data = [
        {
            element: "Drones",
            dropFunction: async () => {
                try {
                    await deleteAllDrones();
                    showSnackbar("success", "Drones deleted successfully");
                } catch (error) {
                    showSnackbar("error", error.message);
                }
            }
        },
        {
            element: "GeoZones",
            dropFunction: async () => {
                try {
                    await deleteAllGeoZones();
                    showSnackbar("success", "GeoZones deleted successfully");
                } catch (error) {
                    showSnackbar("error", error.message);
                }
            }
        },
        {
            element: "Authorizations",
            dropFunction: async () => {
                try {
                    await deleteAllAuthorizations();
                    showSnackbar("success", "Authorizations deleted successfully");
                } catch (error) {
                    showSnackbar("error", error.message);
                }
            }
        }
    ];

    return (
        <Box sx={{ width: "100%", maxWidth: { sm: "100%", md: "1700px" } }}>
            <Grid container spacing={1} columns={12}>
                {data.map((item) => (
                    <Grid key={item.element} size={{ xs: 12, md: 4 }} >
                        <Card variant="outlined" sx={{ display: "flex", flexDirection: "column", gap: "8px", flexGrow: 1 }}>
                        <CardHeader title={`Delete All ${item.element}`} />
                        <CardContent>
                            <Button onClick={item.dropFunction} variant="contained">Confirm</Button>
                        </CardContent>
                        </Card>
                    </Grid>
                ))}
            </Grid>
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

export default SettingSetion;
