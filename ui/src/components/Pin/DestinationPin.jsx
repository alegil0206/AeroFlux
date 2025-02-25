import PropTypes from 'prop-types';
import FlightLandIcon from '@mui/icons-material/FlightLand';

function DestinationPin({ size = 30 }) {

  return (
    <FlightLandIcon
      style={{
        fontSize: size,
        color: 'black',
        backgroundColor: 'white',
        borderRadius: '20%',
        cursor: 'pointer',
        border: '2px solid blue',
      }}
    />
  );
}

DestinationPin.propTypes = {
  size: PropTypes.number,
};

export default DestinationPin;
