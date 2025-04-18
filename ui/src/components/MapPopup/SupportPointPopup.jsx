import PropTypes from 'prop-types';

function SupportPointPopup({ name, latitude, longitude }) {
  return (
    <div style={{color: 'black'}}>
      <strong>Support Point {name}</strong>
      <p>Coordinates: {latitude.toFixed(3)}, {longitude.toFixed(3)}</p>
    </div>
  );
}

SupportPointPopup.propTypes = {
    name: PropTypes.string.isRequired,
    latitude: PropTypes.number.isRequired,
    longitude: PropTypes.number.isRequired,
};

export default SupportPointPopup;