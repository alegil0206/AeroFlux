import PropTypes from 'prop-types';
import FlightIcon from '@mui/icons-material/Flight';
import { getFlightStatusColor } from '../../utils/utils';

function DronePin({ flightMode, size = 30 }) {

  return (
    <FlightIcon
      style={{
        fontSize: size,
        color: getFlightStatusColor(flightMode),
        backgroundColor: 'white',
        borderRadius: '50%',
        border: '2px solid white',
        cursor: 'pointer',
      }}
    />
  );
}

DronePin.propTypes = {
  size: PropTypes.number,
  flightMode: PropTypes.string.isRequired
};

export default DronePin;
