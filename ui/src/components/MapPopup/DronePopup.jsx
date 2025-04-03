import PropTypes from 'prop-types';

function DronePopup({ drone }) {
  return (
    <div style={{color: 'black'}}>
      <strong>{drone.id}, {drone.name}</strong>
      <p>Owner: {drone.owner}</p>
      <p>Model: {drone.model}</p>
      <p>Position: {drone.status.position.latitude.toFixed(3)}, {drone.status.position.longitude.toFixed(3)}</p>
      <p>Altitude: {drone.status.position.altitude}m</p>
      <p>Battery: {drone.status.batteryLevel}</p>
    </div>
  );
}

DronePopup.propTypes = {
  drone: PropTypes.shape({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    owner: PropTypes.string.isRequired,
    model: PropTypes.string.isRequired,
    status: PropTypes.shape({
      position: PropTypes.shape({
        latitude: PropTypes.number.isRequired,
        longitude: PropTypes.number.isRequired,
        altitude: PropTypes.number.isRequired,
      }).isRequired,
      batteryLevel: PropTypes.number.isRequired,
    }).isRequired,
  }).isRequired,
};

export default DronePopup;
