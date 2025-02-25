import PropTypes from 'prop-types';
import Divider from '@mui/material/Divider';
import Drawer, { drawerClasses } from '@mui/material/Drawer';
import Stack from '@mui/material/Stack';
import MenuContent from './MenuContent';
import Box from '@mui/material/Box';

function SideMenuMobile({ open, toggleDrawer, primaryListItems, secondaryListItems, logo }) {
  return (
    <Drawer
      anchor="right"
      open={open}
      onClose={toggleDrawer(false)}
      sx={{
        zIndex: (theme) => theme.zIndex.drawer + 1,
        [`& .${drawerClasses.paper}`]: {
          backgroundImage: 'none',
          backgroundColor: 'background.paper',
        },
      }}
    >
      <Stack
        sx={{
          maxWidth: '70dvw',
          height: '100%',
        }}
      >
        <Box
          sx={{
            display: 'flex',
            mt: 'calc(var(--template-frame-height, 0px) + 4px)',
            p: 1.5,
            justifyContent: 'center',
          }}
        > <img
            src={logo}
            alt="U-Space Logo"
            style={{
              maxWidth: '100%',
              maxHeight: '50px', // Altezza regolabile
              objectFit: 'contain',
            }}
          />
        </Box>
        <Divider />
        <Stack sx={{ flexGrow: 1 }}>
          <MenuContent primaryListItems={primaryListItems} secondaryListItems={secondaryListItems} />
          <Divider />
        </Stack>
      </Stack>
    </Drawer>
  );
}

SideMenuMobile.propTypes = {
    open: PropTypes.bool,
    toggleDrawer: PropTypes.func.isRequired,
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

export default SideMenuMobile;
