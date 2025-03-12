import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import { useState } from 'react';
import { useSettings } from '../context/SettingContext';
import Alert from '@mui/material/Alert';
import CircularProgress from '@mui/material/CircularProgress';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import PropTypes from 'prop-types';

import ThunderstormRoundedIcon from '@mui/icons-material/ThunderstormRounded';
import AirplanemodeActiveRoundedIcon from '@mui/icons-material/AirplanemodeActiveRounded';
import LocalPoliceRoundedIcon from '@mui/icons-material/LocalPoliceRounded';
import FactCheckIcon from '@mui/icons-material/FactCheck';


export default function LoadingSection( { logo } ) {

    const { connectToMainService, error } = useSettings();

    const [tempEndpoint, setTempEndpoint] = useState("http://api.uspace.local/simulator");
    const [isConnecting, setIsConnecting] = useState(false);

    const handleButtonClick = async () => {
        setIsConnecting(true);
        await connectToMainService(tempEndpoint);
        setIsConnecting(false);
    }

    const items = [
        {
          icon: <LocalPoliceRoundedIcon sx={{ color: 'text.secondary' }} />,
          title: 'Geo-Awareness Service',
          description:
            'The Geo-Awareness Service provides information about the geo-zones. It is used to manage the geo-zones.',
        },
        {
          icon: <AirplanemodeActiveRoundedIcon sx={{ color: 'text.secondary' }} />,
          title: 'Drone Identification Service',
          description:
            'The Drone Identification Service provides information about the drones. It is used to manage the drones.',
        },
        {
          icon: <FactCheckIcon sx={{ color: 'text.secondary' }} />,
          title: 'Geo-Authorization Service',
          description:
            'The Geo-Authorization Service provides authorization to access geo-zones for drones. It is used to manage the authorizations.',
        },
        {
          icon: <ThunderstormRoundedIcon sx={{ color: 'text.secondary' }} />,
          title: 'Weather Service',
          description:
            'The Weather Service provides information about the weather. It is used to manage the weather.',
        }
      ];

    return (

        <Stack
        direction="column"
        component="main"
        sx={[
          {
            justifyContent: 'center',
            height: 'calc((1 - var(--template-frame-height, 0)) * 100%)',
            marginTop: 'max(40px - var(--template-frame-height, 0px), 0px)',
            minHeight: '100%',
          },
        ]}
      >
        <Stack
          direction={{ xs: 'column-reverse', md: 'row' }}
          sx={{
            justifyContent: 'center',
            gap: { xs: 6, sm: 12 },
            p: 2,
            mx: 'auto',
          }}
        >
          <Stack
            direction={{ xs: 'column-reverse', md: 'row' }}
            sx={{
              justifyContent: 'center',
              gap: { xs: 6, sm: 12 },
              p: { xs: 2, sm: 4 },
              m: 'auto',
              alignItems: 'center',
            }}
          >
            <Stack
            sx={{ flexDirection: 'column', alignSelf: 'center', gap: 4, maxWidth: 450 }}
            >
              <Box sx={{ display: { xs: 'none', md: 'flex' } }}>
                  <img
                      src={logo}
                      alt="U-Space Logo"
                      style={{
                          maxWidth: '25%',
                          maxHeight: '25%',
                          objectFit: 'contain',
                      }}
                  />
              </Box>
              {items.map((item, index) => (
                  <Stack key={index} direction="row" sx={{ gap: 2 }}>
                  {item.icon}
                  <div>
                      <Typography gutterBottom sx={{ fontWeight: 'medium' }}>
                      {item.title}
                      </Typography>
                      <Typography variant="body2" sx={{ color: 'text.secondary' }}>
                      {item.description}
                      </Typography>
                  </div>
                  </Stack>
              ))}
            </Stack>

            <Card variant="outlined" 
              sx={{ height: 'fit-content', display: 'flex', flexDirection: 'column', gap: 2, justifyContent: 'center', alignItems: 'center', maxWidth: 450 }}>
                <Box sx={{ display: { xs: 'flex', md: 'none' } }}>
                <img
                    src={logo}
                    alt="U-Space Logo"
                    style={{
                        maxWidth: '25%',
                        maxHeight: '25%',
                        objectFit: 'contain',
                    }}
                />
                </Box>
                <Typography
                  component="h1"
                  variant="h4"
                  sx={{ width: '100%', fontSize: 'clamp(2rem, 10vw, 2.15rem)' }}
                >
                  U-Space Simulator
                </Typography>
                <Box
                  sx={{ display: 'flex', flexDirection: 'column', width: '100%', gap: 2 }}
                >
                    <Typography>
                      To start using the U-Space Simulator, please connect to the main service. Input the endpoint below and click "Connect to Simulator".
                    </Typography>
                    <TextField
                        label="Endpoint"
                        value={tempEndpoint || ""}
                        onChange={(e) => setTempEndpoint(e.target.value)}
                        fullWidth
                    />
                    <Button variant="contained" onClick={() => handleButtonClick()}>Connect to Simulator</Button>
                    { isConnecting && <CircularProgress /> }    
                    {error && <Alert severity="error">{error.message + '. Try again.' } </Alert>} 
                </Box>
            </Card>
          </Stack>
        </Stack>
      </Stack>
    
    );
}

LoadingSection.propTypes = {
    logo: PropTypes.string.isRequired
};  