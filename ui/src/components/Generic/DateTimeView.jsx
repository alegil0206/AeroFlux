import { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import Button from '@mui/material/Button';
import CalendarTodayRoundedIcon from '@mui/icons-material/CalendarTodayRounded';
import dayjs from 'dayjs';

function ButtonField(props) {
  const {
    setOpen,
    label,
    id,
    disabled,
    InputProps: { ref } = {}, 
    inputProps: { 'aria-label': ariaLabel } = {} 
  } = props;

  ButtonField.propTypes = {
    /**
     * If `true`, the component is disabled.
     * @default false
     */
    disabled: PropTypes.bool,
    id: PropTypes.string,
    inputProps: PropTypes.shape({
      'aria-label': PropTypes.string,
    }),
    InputProps: PropTypes.shape({
      endAdornment: PropTypes.node,
      startAdornment: PropTypes.node,
    }),
    label: PropTypes.node,
    setOpen: PropTypes.func,
  };  

  return (
    <Button
      variant="outlined"
      id={id}
      disabled={disabled}
      ref={ref}
      aria-label={ariaLabel}
      size="small"
      onClick={() => setOpen?.((prev) => !prev)}
      startIcon={<CalendarTodayRoundedIcon fontSize="small" />}
      sx={{ minWidth: 'fit-content' }}
    >
      {label ? `${label}` : 'Pick a date'}
    </Button>
  );
}

export default function DateTimeView() {
  const [value, setValue] = useState(dayjs());  // Inizializza con la data e ora correnti
  const [open, setOpen] = useState(false);

  useEffect(() => {
    const intervalId = setInterval(() => {
      setValue(dayjs()); 
    }, 1000);
    return () => clearInterval(intervalId);
  }, []);

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <DatePicker
        value={value}
        label={value.format('MMM DD, YYYY HH:mm:ss')}
        onChange={() => {}}
        slots={{ field: ButtonField }}
        slotProps={{
          field: { setOpen },
        }}
        open={open}
        onClose={() => setOpen(false)}
        onOpen={() => setOpen(true)}
        readOnly 
      />
    </LocalizationProvider>
  );
}
