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
import LocalPoliceRoundedIcon from '@mui/icons-material/LocalPoliceRounded';
import HouseRoundedIcon from '@mui/icons-material/HouseRounded';
import InfoRoundedIcon from '@mui/icons-material/InfoRounded';
import SettingsIcon from '@mui/icons-material/Settings';
import CircularProgress from '@mui/material/CircularProgress';

import { BrowserRouter, Routes, Route } from 'react-router-dom';

import { useSettings } from './context/SettingContext'; 

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
import SettingSection from './pages/SettingSection';

import logoImage from './assets/logo.png';

const primaryListItems = [
  { text: 'Home', path: '/', icon: <HouseRoundedIcon /> },
  { text: 'GeoZone', path: '/geozone', icon: <LocalPoliceRoundedIcon /> },
  { text: 'Drone', path: '/drone', icon: <AirplanemodeActiveRoundedIcon /> },
  { text: 'Weather', path: '/weather', icon: <ThunderstormRoundedIcon /> },
  { text: 'Setting', path: '/setting', icon: <SettingsIcon /> },
];

const secondaryListItems = [
  { text: 'About', path: 'https://gitlab.com/u-space_lecco_brianza',  icon: <InfoRoundedIcon /> }
];

export default function Dashboard(props) {

  const { loading } = useSettings();

  return (
    <AppTheme {...props} themeComponents={xThemeComponents}>
      <CssBaseline enableColorScheme />

      { loading && 
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
          <CircularProgress />
        </Box> 
      }

      {!loading &&

        <BrowserRouter>
          <Box sx={{ display: 'flex' }}>
            <SideMenu primaryListItems={primaryListItems} secondaryListItems={secondaryListItems} logo={logoImage}/>
            <AppNavbar primaryListItems={primaryListItems} secondaryListItems={secondaryListItems} logo={logoImage} />
            {/* Main content */}
            <Box
              component="main"
              sx={(theme) => ({
                flexGrow: 1,
                backgroundColor: theme.vars
                  ? `rgba(${theme.vars.palette.background.defaultChannel} / 1)`
                  : alpha(theme.palette.background.default, 1),
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
