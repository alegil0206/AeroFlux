import PropTypes from 'prop-types';
import HubIcon from '@mui/icons-material/Hub';

function SupportPointPin({ size = 30 }) {
    return (
        <HubIcon
        style={{
            fontSize: size,
            color: 'black',
            backgroundColor: 'white',
            borderRadius: '20%',
            cursor: 'pointer',
            border: '2px solid red',
        }}
        />
    );
}

SupportPointPin.propTypes = {
    size: PropTypes.number,
};

export default SupportPointPin;
