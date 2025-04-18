import {
    TextField,
    Select,
    MenuItem,
    FormControl,
    InputLabel,
    Grid2 as Grid,
    FormHelperText,
    Typography,
    FormControlLabel,
    Checkbox
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
                    margin="normal"
                />
                <FormHelperText>{errors.name}</FormHelperText>
            </FormControl>

            <Typography component="h3" variant="h6">Position</Typography>
            <Grid container columns={12} spacing={2} sx={{ mb: (theme) => theme.spacing(2) }}>
                <Grid size={{ xs: 12, md: 6 }}>
                    <FormControl fullWidth margin="normal" error={!!errors.latitude}>
                        <TextField
                            label="Latitude"
                            name="latitude"
                            type="number"
                            value={supportPoint.latitude}
                            onChange={(e) => handleChange('latitude', parseFloat(e.target.value))}
                            fullWidth
                            margin="normal"
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
                            margin="normal"
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