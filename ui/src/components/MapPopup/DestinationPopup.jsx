import PropTypes from 'prop-types';

function DestinationPopup({ destination, droneName }) {
  return (
    <div style={{color: 'black'}}>
      <strong>Destination of {droneName}</strong>
      <p>Coordinates: {destination.latitude.toFixed(3)}, {destination.longitude.toFixed(3)}</p>
    </div>
  );
}

DestinationPopup.propTypes = {
  destination: PropTypes.shape({
    latitude: PropTypes.number.isRequired,
    longitude: PropTypes.number.isRequired,
  }).isRequired,
  droneName: PropTypes.string.isRequired,
};

export default DestinationPopup;
