import { useState, useEffect } from 'react';
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
    inputProps: { 'aria-label': ariaLabel } = {},
  } = props;

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
  const [now, setNow] = useState(dayjs()); // aggiorna il testo del bottone
  const [open, setOpen] = useState(false);
  const [calendarValue, setCalendarValue] = useState(dayjs()); // usato solo nel calendario

  useEffect(() => {
    const intervalId = setInterval(() => {
      setNow(dayjs());
    }, 1000);
    return () => clearInterval(intervalId);
  }, []);

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <DatePicker
        value={calendarValue}
        onChange={(newValue) => setCalendarValue(newValue)} // non fa nulla davvero
        label={now.format('MMM DD, YYYY HH:mm:ss')}
        open={open}
        onOpen={() => setOpen(true)}
        onClose={() => setOpen(false)}
        readOnly
        slots={{ field: ButtonField }}
        slotProps={{ field: { setOpen } }}
      />
    </LocalizationProvider>
  );
}
