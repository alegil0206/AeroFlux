import Box from "@mui/material/Box";
import Grid from "@mui/material/Grid2";
import Card from "@mui/material/Card";
import { CardContent, CardHeader } from "@mui/material";
import Button from "@mui/material/Button";
import { Snackbar, Alert, TextField, Typography } from "@mui/material";
import { useState, useEffect } from "react";

import { useSettings } from "../contexts/SettingContext";
import { useDroneIdentification } from "../hooks/useDroneIdentification";
import { useGeoAwareness } from "../hooks/useGeoAwareness";
import { useGeoAuthorization } from "../hooks/useGeoAuthorization";
import SettingMap from "../components/Setting/SettingMap";

function SettingsSection() {
    const { coordinates, services, error: settingError, updateCoordinates, updateServiceUrl, fetchSettings } = useSettings();
    
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [alertMessage, setAlertMessage] = useState(null);

    const { deleteAllDrones, error: errorDrones } = useDroneIdentification();
    const { deleteAllGeoZones, deleteAllSupportPoints, error: errorGeoZones } = useGeoAwareness();
    const { deleteAllAuthorizations, error: errorAuthorizations } = useGeoAuthorization();

    useEffect(() => {
        if (errorDrones) showSnackbar("error", errorDrones.message);
        if (errorGeoZones) showSnackbar("error", errorGeoZones.message);
        if (errorAuthorizations) showSnackbar("error", errorAuthorizations.message);
        if (settingError) showSnackbar("error", settingError.message);
    }, [errorDrones, errorGeoZones, errorAuthorizations, settingError]);
    
    const showSnackbar = (type, message) => {
        setAlertMessage({ type: type, text: message });
        setSnackbarOpen(true);
    };

    const closeSnackbar = () => {
        setSnackbarOpen(false);
        setAlertMessage(null);
    };

    const [tempCoordinates, setTempCoordinates] = useState(coordinates);
    const [tempEndpoints, setTempEndpoints] = useState(services);

    const handleCoordinateChange = (e) => {
        const { name, value } = e.target;
        setTempCoordinates((prev) => ({
            ...prev,
            [name]: parseFloat(value),
        }));
    };

    const handleCoordinatesChangeOnMap = (newCoordinates) => {
        setTempCoordinates(newCoordinates);
    };

    const handleSaveCoordinates = async () => {
        const responseOk = await updateCoordinates(tempCoordinates);
        if (responseOk)
            showSnackbar("success", "Map coordinates updated successfully");
        fetchSettings();
    };

    const handleEndpointChange = (key, value) => {
        setTempEndpoints((prev) => ({ ...prev, [key]: value }));
    };

    const handleSaveEndpoint = async (key) => {
        const responseOk = await updateServiceUrl(key, tempEndpoints[key]);
        if (responseOk)
            showSnackbar("success", `${key} endpoint updated successfully`);
        fetchSettings();
    };

    const data = [
        { element: "Drone Identification", key: "drone_identification", dropFunction: deleteAllDrones },
        { element: "Geo Awareness", key: "geo_awareness", dropFunction: () => { deleteAllGeoZones(); deleteAllSupportPoints(); } },
        { element: "Geo Authorization", key: "geo_authorization", dropFunction: deleteAllAuthorizations },
        { element: "Weather", key: "weather", dropFunction: null }
    ];

    return (
        <Box sx={{ width: "100%", maxWidth: { sm: "100%", md: "1700px" } }}>
            <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
                Microservices Settings
            </Typography>
            <Grid container spacing={1} columns={12} sx={{ mb: (theme) => theme.spacing(2) }} >
                {data.map((item) => (
                    <Grid key={item.element} size={{ xs: 12, md: 6, lg: 4 }}>
                        <Card variant="outlined" 
                            sx={{ display: 'flex', flexDirection: 'column', gap: '8px', flexGrow: 1 }}
                        >
                            <CardHeader title={`${item.element} Microservice`} />
                            <CardContent sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                                <TextField
                                    label="Endpoint"
                                    value={tempEndpoints[item.key] || ""}
                                    onChange={(e) => handleEndpointChange(item.key, e.target.value)}
                                    fullWidth
                                />
                                <Button variant="outlined" onClick={() => handleSaveEndpoint(item.key)}>Save Endpoint</Button>

                                {item.dropFunction && (
                                    <Button onClick={async () => {
                                        const responseOk = await item.dropFunction();
                                        if (responseOk)
                                            showSnackbar("success", `${item.element} reset successfully`);
                                    }} variant="contained" color="error">
                                        Restore Microservice
                                    </Button>
                                )}
                            </CardContent>
                        </Card>
                    </Grid>
                ))}
            </Grid>
            <Typography component="h2" variant="h6" sx={{ mb: 2 }}>
                Map Settings 
            </Typography>
            <Grid container spacing={1} columns={12} sx={{ mb: (theme) => theme.spacing(2) }} >
                {/* Card for Map Coordinates */}
                <Grid size={{ xs: 12, md: 6, lg: 4 }}>
                    <Card variant="outlined" sx={{ display: 'flex', flexDirection: 'column', gap: '8px', flexGrow: 1 }}>
                        <CardHeader title="Map Center Coordinates" />
                        <CardContent sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                            {/* Warning message */}
                            <Alert severity="warning" variant="outlined">
                                Changing coordinates will reset all microservices data.
                            </Alert>

                            <TextField
                                label="Longitude"
                                type="number"   
                                name="longitude"
                                value={tempCoordinates?.longitude || ""}
                                onChange={handleCoordinateChange}
                                fullWidth
                            />
                            <TextField
                                label="Latitude"
                                type="number"
                                name="latitude"
                                value={tempCoordinates?.latitude || ""}
                                onChange={handleCoordinateChange}
                                fullWidth
                            />
                            <Button variant="outlined" onClick={handleSaveCoordinates}>Save Coordinates</Button>
                        </CardContent>
                    </Card>
                </Grid>
                <Grid size={{ xs: 12, md: 6, lg: 8 }}>
                    <SettingMap
                        coordinates={tempCoordinates}
                        onCoordinatesChange={handleCoordinatesChangeOnMap}
                    />
                </Grid>
            </Grid>

            {/* Snackbar for notifications */}
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
