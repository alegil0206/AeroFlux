import {useState} from 'react';
import PropTypes from 'prop-types';
import { styled } from '@mui/material/styles';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import MuiToolbar from '@mui/material/Toolbar';
import { tabsClasses } from '@mui/material/Tabs';
import Typography from '@mui/material/Typography';
import MenuRoundedIcon from '@mui/icons-material/MenuRounded';
import AirplanemodeActiveRoundedIcon from '@mui/icons-material/AirplanemodeActiveRounded';
import SideMenuMobile from './SideMenuMobile';
import MenuButton from './MenuButton';
import ColorModeIconDropdown from '../../theme/ColorModeIconDropdown';

const Toolbar = styled(MuiToolbar)({
  width: '100%',
  padding: '12px',
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'start',
  justifyContent: 'center',
  gap: '12px',
  flexShrink: 0,
  [`& ${tabsClasses.flexContainer}`]: {
    gap: '8px',
    p: '8px',
    pb: 0,
  },
});

export default function AppNavbar({primaryListItems, secondaryListItems, logo}) {
  const [open, setOpen] = useState(false);

  const toggleDrawer = (newOpen) => () => {
    setOpen(newOpen);
  };

  return (
    <AppBar
      position="fixed"
      sx={{
        display: { xs: 'auto', md: 'none' },
        boxShadow: 0,
        bgcolor: 'background.paper',
        backgroundImage: 'none',
        borderBottom: '1px solid',
        borderColor: 'divider',
        top: 'var(--template-frame-height, 0px)',
      }}
    >
      <Toolbar variant="regular">
        <Stack
          direction="row"
          sx={{
            alignItems: 'center',
            flexGrow: 1,
            width: '100%',
            gap: 1,
          }}
        >
        <Box
            sx={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              height: '2.5rem', // Altezza regolabile
            }}
          >
            <img
              src={logo}
              alt="U-Space Logo"
              style={{
                maxWidth: '100%',
                maxHeight: '100%',
                objectFit: 'contain',
              }}
            />
          </Box>
          
          <ColorModeIconDropdown /> 
          
          <MenuButton aria-label="menu" onClick={toggleDrawer(true)}>
            <MenuRoundedIcon />
          </MenuButton>
          <SideMenuMobile open={open} toggleDrawer={toggleDrawer} primaryListItems={primaryListItems} secondaryListItems={secondaryListItems} logo={logo} />
        </Stack>
      </Toolbar>
    </AppBar>
  );
}

AppNavbar.propTypes = {
    primaryListItems: PropTypes.arrayOf(
    PropTypes.shape({
          path: PropTypes.string.isRequired,
          icon: PropTypes.element.isRequired,
          text: PropTypes.string.isRequired,
        })
      ).isRequired,
      secondaryListItems: PropTypes.arrayOf(
        PropTypes.shape({
          icon: PropTypes.element.isRequired,
          text: PropTypes.string.isRequired,
        })
      ).isRequired,
      logo: PropTypes.string.isRequired
};
