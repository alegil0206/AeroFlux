import { useState, useEffect } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
} from '@mui/material';
import PropTypes from 'prop-types';
import SupportPointDetailsForm from './SupportPointDetailsForm';
import SupportPointDrawMap from './SupportPointDrawMap';

import { useMapSettings } from '../../../hooks/useMapSettings';

export default function SupportPointFormDialog({ onClose, onSave, initialData = null, open }) {

    const { initialViewState } = useMapSettings();

    const getDefaultSupportPoint = () => ({
        id: '',
        name: '',
        latitude: initialViewState.latitude,
        longitude: initialViewState.longitude,
    });

    const [supportPoint, setSupportPoint] = useState(getDefaultSupportPoint());
    const [errors, setErrors] = useState({});

    useEffect(() => {
        if (open) {
            setSupportPoint(initialData || getDefaultSupportPoint());
            setErrors({});
        } else {
            setSupportPoint(getDefaultSupportPoint());
            setErrors({});
        }
    }, [open, initialData]);

    const handleChange = (field, value) => {
        setSupportPoint((prev) => ({
            ...prev,
            [field]: typeof value === 'object' ? { ...prev[field], ...value } : value,
        }));
    }

    const validateFields = () => {
        const newErrors = {};

        if (!supportPoint.name) newErrors.name = 'Name is required.';
        if (!supportPoint.latitude) newErrors.latitude = 'Latitude is required.';
        if (!supportPoint.longitude) newErrors.longitude = 'Longitude is required.';

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    }

    const handleSave = () => {
        if (validateFields()) {
            onSave(supportPoint);
            onClose();
        }
    }

    return (
        <Dialog open={open} onClose={onClose} maxWidth="lg" fullWidth>
            <DialogTitle>{initialData ? 'Edit Support Point' : 'Add Support Point'}</DialogTitle>
            <DialogContent>
                <SupportPointDetailsForm
                    supportPoint={supportPoint}
                    errors={errors}
                    handleChange={handleChange}
                />
                <SupportPointDrawMap
                    supportPoint={supportPoint}
                    handleChange={handleChange}  
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose} color="secondary">Cancel</Button>
                <Button onClick={handleSave} color="primary">Save</Button>
            </DialogActions>
        </Dialog>
    );
}

SupportPointFormDialog.propTypes = {
    open: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    onSave: PropTypes.func.isRequired,
    initialData: PropTypes.object,
};