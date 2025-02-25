import PropTypes from 'prop-types';
import FlightIcon from '@mui/icons-material/Flight';

function DronePin({ height, size = 30 }) {
  const color = height > 0 ? 'green' : 'gray';

  return (
    <FlightIcon
      style={{
        fontSize: size,
        color: color,
        backgroundColor: 'white',
        borderRadius: '50%',
        border: '2px solid white',
        cursor: 'pointer',
      }}
    />
  );
}

DronePin.propTypes = {
  height: PropTypes.number.isRequired,
  size: PropTypes.number,
};

export default DronePin;
