import { useState, useEffect } from "react";
import { Box, FormControl, InputLabel, Select, MenuItem, Button } from "@mui/material";
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';

export default function HistorySelector({ historyList, onSelectHistory }) {
    const [selectedIndex, setSelectedIndex] = useState(0);

    useEffect(() => {
        if (historyList.length > 0) {
            onSelectHistory(historyList[selectedIndex]);
        }
    }, [selectedIndex, historyList, onSelectHistory]);

    const handleSelectChange = (e) => {
        const newIndex = historyList.findIndex(h => h.id === e.target.value);
        setSelectedIndex(newIndex);
    };

    const handlePrevious = () => {
        if (selectedIndex > 0) {
            setSelectedIndex(selectedIndex - 1);
        }
    };

    const handleNext = () => {
        if (selectedIndex < historyList.length - 1) {
            setSelectedIndex(selectedIndex + 1);
        }
    };

    return (
        <Box sx={{ mb: 2, display: "flex", justifyContent: "space-between" }}>
            <Button
                variant="outlined"
                onClick={handlePrevious}
                disabled={selectedIndex === 0 || historyList.length < 1}
                startIcon={<ArrowBackIosIcon />}
            >
                Previous
            </Button>
            <FormControl fullWidth sx={{ mb: 1 }}>
                <InputLabel id="select-drone-label">Select Simulation</InputLabel>
                <Select
                    labelId="select-drone-label"
                    value={historyList[selectedIndex]?.id || ""}
                    onChange={handleSelectChange}
                    label="Select Simulation"
                >
                    {historyList.map((history) => (
                        <MenuItem key={history.id} value={history.id}>
                            {`${history.id} - ${history.date}`}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
            <Button
                variant="outlined"
                onClick={handleNext}
                disabled={selectedIndex === historyList.length - 1 || historyList.length < 1}
                endIcon={<ArrowForwardIosIcon />}
            >
                Next
            </Button>
        </Box>
    );
}
