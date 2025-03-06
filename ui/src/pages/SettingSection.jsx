import Box from "@mui/material/Box";
import Grid from "@mui/material/Grid2";
import Card from "@mui/material/Card";
import { CardContent, CardHeader } from "@mui/material";
import Button from "@mui/material/Button";
import { deleteAllDrones } from "../services/drones";
import { deleteAllGeoZones } from "../services/geoZones";
import { deleteAllAuthorizations } from "../services/authorization";
import { Snackbar, Alert, TextField } from "@mui/material";
import { useState } from "react";
import { getEndpoint, setEndpoint } from "../services/apiEndpoints";
import { setCoordinates, getCoordinates, updateMapBounds } from "../utils/mapSettings";
import { updateGridSettings } from "../services/weather";

function SettingsSection() {
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

    // Stato locale per gli endpoint
    const [endpoints, setEndpoints] = useState({
        drone_identification: getEndpoint("drone_identification") || "",
        geo_awareness: getEndpoint("geo_awareness") || "",
        geo_authorization: getEndpoint("geo_authorization") || "",
        weather: getEndpoint("weather") || ""
    });

    const [mapCoordinates, setMapCoordinates] = useState(getCoordinates());

    const handleCoordinateChange = (e) => {
        const { name, value } = e.target;
        setMapCoordinates((prev) => ({
            ...prev,
            [name]: parseFloat(value),
        }));
    };

    const handleSaveCoordinates = () => {
        setCoordinates(mapCoordinates.longitude, mapCoordinates.latitude);
        updateMapBounds();
        try {
            updateGridSettings(mapCoordinates);
            showSnackbar("success", "Map coordinates updated successfully");
        }
        catch (error) {
            showSnackbar("error", error.message);
        }
    };

    const handleEndpointChange = (key, value) => {
        setEndpoints((prev) => ({ ...prev, [key]: value }));
    };

    const handleSave = (key) => {
        setEndpoint(key, endpoints[key]);
        showSnackbar("success", `${key} endpoint updated successfully`);
    };

    const data = [
        {
            element: "Drone Identification",
            key: "drone_identification",
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
            element: "Geo Awareness",
            key: "geo_awareness",
            dropFunction: async () => {
                try {
                    await deleteAllGeoZones();
                    showSnackbar("success", "Geo Awareness zones deleted successfully");
                } catch (error) {
                    showSnackbar("error", error.message);
                }
            }
        },
        {
            element: "Geo Authorization",
            key: "geo_authorization",
            dropFunction: async () => {
                try {
                    await deleteAllAuthorizations();
                    showSnackbar("success", "Authorizations deleted successfully");
                } catch (error) {
                    showSnackbar("error", error.message);
                }
            }
        },
        {
            element: "Weather",
            key: "weather",
            dropFunction: null // Nessuna funzione di reset per il meteo
        }
    ];

    return (
        <Box sx={{ width: "100%", maxWidth: { sm: "100%", md: "1700px" } }}>
            <Grid container spacing={2} columns={12}>
                {data.map((item) => (
                    <Grid key={item.element} xs={12} md={4}>
                        <Card variant="outlined" sx={{ display: "flex", flexDirection: "column", gap: "8px", flexGrow: 1, p: 2 }}>
                            <CardHeader title={`${item.element} Microservice`} />
                            <CardContent sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                                {/* Campo di input per l'endpoint */}
                                <TextField
                                    label={`Endpoint`}
                                    value={endpoints[item.key]}
                                    onChange={(e) => handleEndpointChange(item.key, e.target.value)}
                                    fullWidth
                                />
                                <Button variant="outlined" onClick={() => handleSave(item.key)}>Save Endpoint</Button>

                                {/* Pulsante per eliminare tutti gli elementi (se disponibile) */}
                                {item.dropFunction && (
                                    <Button onClick={item.dropFunction} variant="contained" color="error">
                                        Restore Microservice
                                    </Button>
                                )}
                            </CardContent>
                        </Card>
                    </Grid>
                ))}

                {/* Card per le coordinate della mappa */}
                <Grid xs={12} md={4}>
                    <Card variant="outlined" sx={{ display: "flex", flexDirection: "column", gap: "8px", flexGrow: 1, p: 2 }}>
                        <CardHeader title="Map Center Coordinates" />
                        <CardContent sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                            <TextField
                                label="Longitude"
                                type="number"
                                name="longitude"
                                value={mapCoordinates.longitude}
                                onChange={handleCoordinateChange}
                                fullWidth
                            />
                            <TextField
                                label="Latitude"
                                type="number"
                                name="latitude"
                                value={mapCoordinates.latitude}
                                onChange={handleCoordinateChange}
                                fullWidth
                            />
                            <Button variant="outlined" onClick={handleSaveCoordinates}>Save Coordinates</Button>
                        </CardContent>
                    </Card>
                </Grid>


            </Grid>

            {/* Snackbar per notifiche */}
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

export default SettingsSection;
