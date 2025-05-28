import { styled } from '@mui/material/styles';
import Typography from '@mui/material/Typography';
import Breadcrumbs, { breadcrumbsClasses } from '@mui/material/Breadcrumbs';
import NavigateNextRoundedIcon from '@mui/icons-material/NavigateNextRounded';
import { useLocation } from 'react-router-dom';


const StyledBreadcrumbs = styled(Breadcrumbs)(({ theme }) => ({
  margin: theme.spacing(1, 0),
  [`& .${breadcrumbsClasses.separator}`]: {
    color: (theme.vars || theme).palette.action.disabled,
    margin: 1,
  },
  [`& .${breadcrumbsClasses.ol}`]: {
    alignItems: 'center',
  },
}));

// Funzione per mappare i percorsi alle etichette dei breadcrumb
const getPageNameFromPath = (path) => {
  switch (path) {
    case '/geozone':
      return 'GeoZone';
    case '/drone':
      return 'Drone';
    case '/weather':
      return 'Weather';
    case '/history':
      return 'Simulation History';
    case '/setting':
      return 'Settings';
    default:
      return 'Home';
  }
};


export default function NavbarBreadcrumbs() {

  const location = useLocation(); 
  const currentPage = getPageNameFromPath(location.pathname);

  return (
    <StyledBreadcrumbs
      aria-label="breadcrumb"
      separator={<NavigateNextRoundedIcon fontSize="small" />}
    >
      <Typography variant="body1">Dashboard</Typography>
      <Typography variant="body1" sx={{ color: 'text.primary', fontWeight: 600 }}>
        {currentPage}
      </Typography>
    </StyledBreadcrumbs>
  );
}
