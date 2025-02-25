import { styled } from '@mui/material/styles';
import MuiDrawer, { drawerClasses } from '@mui/material/Drawer';
import Box from '@mui/material/Box';
import Divider from '@mui/material/Divider';
import MenuContent from './MenuContent';
import PropTypes from 'prop-types';


const drawerWidth = 180;

const Drawer = styled(MuiDrawer)({
  width: drawerWidth,
  flexShrink: 0,
  boxSizing: 'border-box',
  mt: 10,
  [`& .${drawerClasses.paper}`]: {
    width: drawerWidth,
    boxSizing: 'border-box',
  },
});

export default function SideMenu({primaryListItems, secondaryListItems, logo}) {
  return (
    <Drawer
      variant="permanent"
      sx={{
        display: { xs: 'none', md: 'block' },
        [`& .${drawerClasses.paper}`]: {
          backgroundColor: 'background.paper',
        },
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
      <MenuContent primaryListItems={primaryListItems} secondaryListItems={secondaryListItems}/>
    </Drawer>
  );
}

SideMenu.propTypes = {
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
