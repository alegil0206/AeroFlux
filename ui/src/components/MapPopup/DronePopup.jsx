import PropTypes from 'prop-types';

function DronePopup({ drone }) {
  return (
    <div style={{color: 'black'}}>
      <strong>{drone.id}, {drone.name}</strong>
      <p>Owner: {drone.owner}</p>
      <p>Model: {drone.model}</p>
      <p>Position: {drone.position.latitude.toFixed(3)}, {drone.position.longitude.toFixed(3)}</p>
      <p>Height: {drone.position.height}m</p>
    </div>
  );
}

DronePopup.propTypes = {
  drone: PropTypes.shape({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    owner: PropTypes.string.isRequired,
    model: PropTypes.string.isRequired,
    position: PropTypes.shape({
      latitude: PropTypes.number.isRequired,
      longitude: PropTypes.number.isRequired,
      height: PropTypes.number.isRequired,
    }).isRequired,
  }).isRequired,
};

export default DronePopup;
