import PropTypes from 'prop-types';

function SourcePopup({ source, droneName }) {
  return (
    <div style={{color: 'black'}}>
      <strong>Source of {droneName}</strong>
      <p>Coordinates: {source.latitude.toFixed(3)}, {source.longitude.toFixed(3)}</p>
    </div>
  );
}

SourcePopup.propTypes = {
  source: PropTypes.shape({
    latitude: PropTypes.number.isRequired,
    longitude: PropTypes.number.isRequired,
  }).isRequired,
  droneName: PropTypes.string.isRequired,
};

export default SourcePopup;
