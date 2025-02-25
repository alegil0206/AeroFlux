import PropTypes from 'prop-types';

function GeoZonePopup({ geozone }) {
  return (
    <div style={{color: 'black'}}>
      <strong>GeoZone: {geozone.id}</strong>
      <p>Status: {geozone.status}</p>
      <p>Category: {geozone.category}</p>
    </div>
  );
}

GeoZonePopup.propTypes = {
  geozone: PropTypes.shape({
    id: PropTypes.string.isRequired,
    status: PropTypes.string.isRequired,
    category: PropTypes.string.isRequired,
  }).isRequired,
};

export default GeoZonePopup;
