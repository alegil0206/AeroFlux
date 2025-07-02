import { useHistory } from "../hooks/useHistory";
import { useEffect, useState } from "react";
import { Box, Typography, Snackbar, Alert } from "@mui/material";
import Grid from "@mui/material/Grid2";
import LogViewer from "../components/Home/LogViewer";
import HistorySelector from "../components/History/HistorySelector";
import MinDistanceChart from "../components/History/MinDistanceChart";
import FlightOutcomeCharts from "../components/History/FlightOutcomeCharts";
import DownloadJsonButton from "../components/History/DownloadJsonButton";
import FlightOutcomeDataGrid from "../components/History/FlightOutcomeDataGrid";
import BatteryLevelChart from "../components/History/BatteryLevelChart";
import HistoryMap from "../components/History/HistoryMap";
import StatCard from "../components/History/StatCard";
import CircularProgress from "@mui/material/CircularProgress";
import Copyright from '../internals/components/Copyright';

function HistorySection() {
    const {
        historyList,
        fetchHistoryList,
        fetchHistoryDetails,
        historyDetails,
        loading,
        error,
    } = useHistory();

    const [selectedHistory, setSelectedHistory] = useState(null);
    const [snackbarOpen, setSnackbarOpen] = useState(false);
    const [alertMessage, setAlertMessage] = useState(null);

    useEffect(() => {
        fetchHistoryList();
    }, []);

    useEffect(() => {
        if (error) showSnackbar("error", error.message);
    }, [error]);

    const showSnackbar = (type, message) => {
        setAlertMessage({ type: type, text: message });
        setSnackbarOpen(true);
    }

    const closeSnackbar = () => {
        setSnackbarOpen(false);
        setAlertMessage(null);
    }

    useEffect(() => {
        if (selectedHistory) {
            fetchHistoryDetails(selectedHistory.id);
        }
    }, [selectedHistory]);

    return (
        <Box sx={{ width: '100%' }}>

            <HistorySelector historyList={historyList} onSelectHistory={setSelectedHistory} />

            {!selectedHistory && (
                <Box
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    height: "100vh",
                }}
                >
                <Typography variant="body1" color="textSecondary">
                    Use the dropdown above to select a simulation history or run a new simulation.
                </Typography>
                </Box>
            )}

            { loading && (
                <Box
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    height: "100vh",
                    flexDirection: "column",
                }}
                >
                <CircularProgress />
                <Typography variant="body1" color="textSecondary">
                    Loading simulation details...
                </Typography>
                </Box>
            )}


            {historyDetails && (
            <>
                <Grid container spacing={1} columns={12} sx={{ mb: (theme) => theme.spacing(2) }}>
                    <Grid size={{ xs: 12, lg: 7 }}>
                        <HistoryMap simulation={historyDetails} />
                    </Grid>
                    <Grid size={{ xs: 12, lg: 5 }}>
                        <Grid container spacing={1} columns={12} sx={{ mb: (theme) => theme.spacing(2) }}>
                            <Grid size={{ xs: 12 }}>
                                <DownloadJsonButton data={historyDetails} />
                            </Grid>  
                            <Grid size={{ xs: 12, md: 6, lg: 12, xl: 6 }}>
                                <StatCard data={{ title: "Simulation ID", value: historyDetails.id }} />
                            </Grid>                          
                            <Grid size={{ xs: 12, md: 6, lg: 12, xl: 6 }}>
                                <StatCard data={{ title: "Execution Date", value: `${new Date(historyDetails.date).toLocaleString()}` }} />
                            </Grid>
                            <Grid size={{ xs: 12, sm: 4, lg: 6 }}>
                                <StatCard data={{ title: "Number of Flights", value: historyDetails.drones.length }} />
                            </Grid>
                            <Grid size={{ xs: 12, sm: 4, lg: 6 }}>
                                <StatCard data={{ title: "Duration (min)", value: (historyDetails.duration / 60).toFixed(0) }} />
                            </Grid>
                            <Grid size={{ xs: 12, sm: 4, lg: 6 }}>
                                <StatCard data={{ title: "Execution Speed", value: `${historyDetails.executionSpeed}x` }} />
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
                <Grid container spacing={1} columns={12} sx={{ mb: (theme) => theme.spacing(2) }}>
                    <Grid size={{ xs: 12 }}>
                        <Typography component="h2" variant="h6" sx={{ mb: 1 }}>
                            Flight Outcomes
                        </Typography>
                    </Grid>
                    <Grid size={{ xs: 12, lg: 7 }}>
                        <FlightOutcomeDataGrid simulation={historyDetails} />
                    </Grid>
                    <Grid size={{ xs: 12, lg: 5 }}>
                        <FlightOutcomeCharts simulation={historyDetails} />
                    </Grid>                    
                    <Grid size={{ xs: 12 }}>
                        <MinDistanceChart simulation={historyDetails} />
                    </Grid>

                    <Grid size={{ xs: 12 }}>
                        <BatteryLevelChart simulation={historyDetails} />
                    </Grid>

                    <Grid size={{ xs: 12 }}>
                        <Typography component="h2" variant="h6" sx={{ mb: 1 }}>
                            Execution Logs
                        </Typography>
                        <LogViewer logs={historyDetails.logs} />
                    </Grid>
                </Grid>
            </>
            )}

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
            <Copyright sx={{ my: 4 }} />
        </Box>
    );
}

export default HistorySection;
