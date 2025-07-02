import { useState, useCallback } from 'react';
import { useSettings } from '../contexts/SettingContext';

export const useHistory = () => {
    const { services } = useSettings();
    const [historyList, setHistoryList] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [historyDetails, setHistoryDetails] = useState(null);


    const fetchHistoryList = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            if (!services.SIMULATOR) throw new Error('Main service is not available');
            const response = await fetch(`${services.SIMULATOR}/simulation-history`);
            if (!response.ok) throw new Error(`Failed to fetch History List: ${response.statusText}`);
            const data = await response.json();
            setHistoryList(data);
        } catch (error) {
            setError(error);
        }
        finally {
            setLoading(false);
        }
    }, [services.SIMULATOR]);

    const fetchHistoryDetails = async (id) => {
        setLoading(true);
        setError(null);
        setHistoryDetails(null);
        try {
            if (!services.SIMULATOR) throw new Error('Main service is not available');
            const response = await fetch(`${services.SIMULATOR}/simulation-history/${id}`);
            if (!response.ok) throw new Error(`Failed to fetch History Details: ${response.statusText}`);
            const data = await response.json();
            setHistoryDetails(data);
        } catch (error) {
            setError(error);
        }
        finally {
            setLoading(false);
        }
    };

    return {
        historyList,
        fetchHistoryList,
        fetchHistoryDetails,
        historyDetails,
        loading,
        error,
    };
}