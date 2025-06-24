import React from 'react';
import { Button } from '@mui/material';
import DownloadIcon from '@mui/icons-material/Download';

const DownloadJsonButton = ({ data, filename = "data.json" }) => {
  const handleDownload = () => {
    const jsonStr = JSON.stringify(data, null, 2);
    const blob = new Blob([jsonStr], { type: "application/json" });
    const url = URL.createObjectURL(blob);

    const a = document.createElement("a");
    a.href = url;
    a.download = filename;
    a.click();

    URL.revokeObjectURL(url);
  };

  return (
    <Button 
      fullWidth
      variant="outlined" 
      startIcon={<DownloadIcon />} 
      onClick={handleDownload}
    >
      Download Data
    </Button>
  );
};

export default DownloadJsonButton;
