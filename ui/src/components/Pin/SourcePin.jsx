import PropTypes from 'prop-types';
import FlightTakeoffIcon from '@mui/icons-material/FlightTakeoff';

function SourcePin({ size = 30 }) {
  return (
    <FlightTakeoffIcon
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

SourcePin.propTypes = {
  size: PropTypes.number,
};

export default SourcePin;
