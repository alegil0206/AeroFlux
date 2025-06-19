import {
    TextField,
    FormControl,
    Grid2 as Grid,
    FormHelperText,
    Typography
  } from '@mui/material';
  import PropTypes from 'prop-types';

export default function SupportPointDetailsForm({ supportPoint, errors, handleChange }) {
    return (
        <div>
            <Typography component="h3" variant="h6">Support Point Details</Typography>
            <FormControl fullWidth margin="normal" error={!!errors.name}>
                <TextField
                    label="Name"
                    name="name"
                    value={supportPoint.name}
                    onChange={(e) => handleChange('name', e.target.value)}
                    fullWidth
                />
                <FormHelperText>{errors.name}</FormHelperText>
            </FormControl>  
            <Grid container columns={12} spacing={{ xs : 0, md : 1}} sx = {{ mb : 1 }}>   
                <Grid size={{ xs: 12, md: 6 }}>
                    <FormControl fullWidth margin="normal" error={!!errors.latitude}>
                        <TextField
                            label="Latitude"
                            name="latitude"
                            type="number"
                            value={supportPoint.latitude}
                            onChange={(e) => handleChange('latitude', parseFloat(e.target.value))}
                            fullWidth
                        />
                        <FormHelperText>{errors.latitude}</FormHelperText>
                    </FormControl>
                </Grid>
                <Grid size={{ xs: 12, md: 6 }}>
                    <FormControl fullWidth margin="normal" error={!!errors.longitude}>
                        <TextField
                            label="Longitude"
                            name="longitude"
                            type="number"
                            value={supportPoint.longitude}
                            onChange={(e) => handleChange('longitude', parseFloat(e.target.value))}
                            fullWidth
                        />
                        <FormHelperText>{errors.longitude}</FormHelperText>
                    </FormControl>
                </Grid>
            </Grid>
        </div>
    );
}

SupportPointDetailsForm.propTypes = {
    supportPoint: PropTypes.object.isRequired,
    errors: PropTypes.object.isRequired,
    handleChange: PropTypes.func.isRequired,
};