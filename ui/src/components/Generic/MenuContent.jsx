import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Stack from '@mui/material/Stack';
import { NavLink, useLocation } from 'react-router-dom';
import PropTypes from 'prop-types';

export default function MenuContent({ primaryListItems, secondaryListItems }) {
  
  const location = useLocation(); // Per ottenere il percorso corrente

  return (
    <Stack sx={{ flexGrow: 1, p: 1, justifyContent: 'space-between' }}>
      <List dense>
        {primaryListItems.map((item, index) => (
          <ListItem key={index} disablePadding sx={{ display: 'block' }}>
            <ListItemButton
              component={NavLink}
              to={item.path}
              selected={location.pathname === item.path}>
              <ListItemIcon>{item.icon}</ListItemIcon>
              <ListItemText primary={item.text} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>

      <List dense>
        {secondaryListItems.map((item, index) => (
          <ListItem key={index} disablePadding sx={{ display: 'block' }}>
            <ListItemButton
              component="a"
              href={item.path}
              target="_blank">
              <ListItemIcon>{item.icon}</ListItemIcon>
              <ListItemText primary={item.text} />  
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Stack>
  );
}

MenuContent.propTypes = {
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
};
