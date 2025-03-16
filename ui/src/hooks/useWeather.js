import { useState, useCallback} from 'react';
import { useSettings } from '../contexts/SettingContext';

export const useWeather = () => {
    const { services } = useSettings();
    const [weather, setWeather] = useState([]);
    const [weatherConfig, setWeatherConfig] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchWeather = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            if (!services.weather) throw new Error('Weather service is not available');
            const response = await fetch(`${services.weather}/weather/rain-cell`);
            if (!response.ok) throw new Error(`Failed to fetch Weather: ${response.statusText}`);
            const data = await response.json();
            setWeather(data);
        } catch (error) {
            setError(error);
        }
        finally {
            setLoading(false);
        }
    }, [services.weather]);

    const fetchWeatherConfig = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            if (!services.weather) throw new Error('Weather service is not available');
            const response = await fetch(`${services.weather}/weather/config`);
            if (!response.ok) throw new Error(`Failed to fetch Weather Config: ${response.statusText}`);
            const data = await response.json();
            setWeatherConfig(data);
        } catch (error) {
            setError(error);
        }
        finally {
            setLoading(false);
        }
    }, [services.weather]);

    const updateWeatherConfig = useCallback(async (config) => {
        setLoading(true);
        setError(null);
        try {
            if (!services.weather) throw new Error('Weather service is not available');
            const response = await fetch(`${services.weather}/weather/config`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(config),
            });
            if (!response.ok) throw new Error(`Error updating Weather Config: ${response.statusText}`);
            return true;
        } catch (error) {
            setError(error);
            return false;
        }
        finally {
            setLoading(false);
        }
    }, [services.weather]);

    return { 
        weather,
        weatherConfig,
        loading,
        error, 
        fetchWeather, 
        fetchWeatherConfig, 
        updateWeatherConfig };
}