import Card from '@mui/material/Card';
import PropTypes from 'prop-types';
import Typography from '@mui/material/Typography';
import WaterDropIcon from '@mui/icons-material/WaterDrop';
import WbSunnyIcon from '@mui/icons-material/WbSunny';
import CloudIcon from '@mui/icons-material/Cloud';
import ThunderstormIcon from '@mui/icons-material/FlashOn';
import GrainIcon from '@mui/icons-material/Grain';
import AcUnitIcon from '@mui/icons-material/AcUnit';
import WaterIcon from '@mui/icons-material/Water';
import HelpOutLineIcon from '@mui/icons-material/HelpOutline';
import Stack from '@mui/material/Stack';

const weatherIconMap = {
    Clear: WbSunnyIcon,
    Clouds: CloudIcon,
    Rain: WaterDropIcon,
    Thunderstorm: ThunderstormIcon,
    Drizzle: GrainIcon,
    Snow: AcUnitIcon,
    Atmosphere: WaterIcon
};

export default function WeatherCard({ data }) {

    const WeatherIcon = weatherIconMap[data.value.main] || HelpOutLineIcon;

    return (
      <Card
        variant="outlined"
              sx={{ justifyContent: 'space-between', alignItems: 'center' }}
      >
        <Stack
          direction="row"
          sx={{ justifyContent: 'space-between', alignItems: 'center' }}
        >
          <div>        
            <Typography component="h2" variant="subtitle2" gutterBottom>{data.title}</Typography>
            <Typography variant="h4" component="p" sx={{ marginLeft: '8px' }}>
              {data.value.description}
            </Typography>
          </div>
          <WeatherIcon sx={{ fontSize: '3rem' }} />
        </Stack>
      </Card>
    );
  }
  
  WeatherCard.propTypes = {
    data: PropTypes.object.isRequired,
};