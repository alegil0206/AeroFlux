import { alpha } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import AppNavbar from './components/Generic/AppNavbar';
import Header from './components/Generic/Header';
import SideMenu from './components/Generic/SideMenu';
import AppTheme from './theme/AppTheme';
import {
  chartsCustomizations,
  dataGridCustomizations,
  datePickersCustomizations,
  treeViewCustomizations,
} from './theme/customizations';

import ThunderstormRoundedIcon from '@mui/icons-material/ThunderstormRounded';
import AirplanemodeActiveRoundedIcon from '@mui/icons-material/AirplanemodeActiveRounded';
import HouseRoundedIcon from '@mui/icons-material/HouseRounded';
import InfoRoundedIcon from '@mui/icons-material/InfoRounded';
import SettingsIcon from '@mui/icons-material/Settings';
import FactCheckIcon from '@mui/icons-material/FactCheck';
import PublicIcon from '@mui/icons-material/Public';


import { BrowserRouter, Routes, Route } from 'react-router-dom';

import { useSettings } from './contexts/SettingContext'; 

const xThemeComponents = {
  ...chartsCustomizations,
  ...dataGridCustomizations,
  ...datePickersCustomizations,
  ...treeViewCustomizations,
};

import DroneSection from './pages/DroneSection';
import GeoZoneSection from './pages/GeoZoneSection';
import WeatherSection from './pages/WeatherSection';
import HomeSection from './pages/HomeSection';
import AuthorizationSection from './pages/AuthorizationSection';
import SettingSection from './pages/SettingSection';
import LoadingSection from './pages/LoadingSection';


import logoImage from './assets/logo.png';

const primaryListItems = [
  { text: 'Home', path: '/', icon: <HouseRoundedIcon /> },
  { text: 'GeoZone', path: '/geozone', icon: <PublicIcon /> },
  { text: 'Drone', path: '/drone', icon: <AirplanemodeActiveRoundedIcon /> },
  { text: 'Authorization', path: '/authorization', icon: <FactCheckIcon /> },
  { text: 'Weather', path: '/weather', icon: <ThunderstormRoundedIcon /> },
  { text: 'Setting', path: '/setting', icon: <SettingsIcon /> },
];

const secondaryListItems = [
  { text: 'About', path: 'https://github.com/alegil0206/U-space',  icon: <InfoRoundedIcon /> }
];

export default function Dashboard(props) {

  const { loading } = useSettings();

  return (
    <AppTheme {...props} themeComponents={xThemeComponents}>
      <CssBaseline enableColorScheme />

      { loading && 
        <LoadingSection logo = {logoImage} />
      }

      { !loading &&

        <BrowserRouter>
          <Box sx={{ display: 'flex' }}>
            <SideMenu primaryListItems={primaryListItems} secondaryListItems={secondaryListItems} logo={logoImage}/>
            <AppNavbar primaryListItems={primaryListItems} secondaryListItems={secondaryListItems} logo={logoImage} />
            {/* Main content */}
            <Box
              component="main"
              sx={(theme) => ({
                flexGrow: 1,
                backgroundImage:
                  'radial-gradient(ellipse at 50% 50%, hsl(210, 100%, 97%), hsl(0, 0%, 100%))',
                backgroundRepeat: 'no-repeat',
                ...theme.applyStyles('dark', {
                  backgroundImage:
                    'radial-gradient(at 50% 50%, hsla(210, 100%, 16%, 0.5), hsl(220, 30%, 5%))',
                }),
                overflow: 'auto',
              })}
            >
              <Stack
                spacing={2}
                sx={{
                  alignItems: 'center',
                  mx: 3,
                  pb: 5,
                  mt: { xs: 8, md: 0 },
                }}
              >
                <Header />
                <Routes>
                  <Route path="/" element={<HomeSection />} /> {/* Home */}
                  <Route path="/authorization" element={<AuthorizationSection />} />
                  <Route path="/geozone" element={<GeoZoneSection />} />
                  <Route path="/drone" element={<DroneSection />} />
                  <Route path="/weather" element={<WeatherSection />} />
                  <Route path="/setting" element={<SettingSection />} />
                </Routes>
              </Stack>
            </Box>
          </Box>
        </BrowserRouter>
      }
      
    </AppTheme>
  );
}
