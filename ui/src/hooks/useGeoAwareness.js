import { useState, useCallback} from 'react';
import { useSettings } from '../contexts/SettingContext';

export const useGeoAwareness = () => {
    const { services } = useSettings();
    const [geoZones, setGeoZones] = useState([]);
    const [supportPoints, setSupportPoints] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    
    const fetchGeoZones = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            if (!services.geo_awareness) throw new Error('Geo Awareness service is not available');
            const response = await fetch(`${services.geo_awareness}/geozone`);
            if (!response.ok) throw new Error(`Failed to fetch Geo Zones: ${response.statusText}`);
            const data = await response.json();
            setGeoZones(data);
        } catch (error) {
            setError(error);
        }
        finally {
            setLoading(false);
        }
    }, [services.geo_awareness]);

    const addGeoZone = useCallback(async (geoZone) => {
        setLoading(true);
        setError(null);
        try {
            if (!services.geo_awareness) throw new Error('Geo Awareness service is not available');
            const response = await fetch(`${services.geo_awareness}/geozone`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(geoZone),
            });
            if (!response.ok) throw new Error(`Error adding Geo Zone: ${response.statusText}`);
            const locationHeader = response.headers.get('Location');
            const id = locationHeader?.split('/').pop();
            return id;
        } catch (error) {
            setError(error);
            return null;
        }
        finally {
            setLoading(false);
        }
    }, [services.geo_awareness]);

    const updateGeoZone = useCallback(async (geoZone) => {
        setLoading(true);
        setError(null);
        try {
            if (!services.geo_awareness) throw new Error('Geo Awareness service is not available');
            const response = await fetch(`${services.geo_awareness}/geozone/${geoZone.id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(geoZone),
            });
            if (!response.ok) throw new Error(`Error updating Geo Zone ${geoZone.id}: ${response.statusText}`);
            return geoZone.id;
        } catch (error) {
            setError(error);
            return null;
        }
        finally {
            setLoading(false);
        }
    }, [services.geo_awareness]);

    const deleteGeoZone = useCallback(async (id) => {
        setLoading(true);
        setError(null);
        try {
            if (!services.geo_awareness) throw new Error('Geo Awareness service is not available');
            const response = await fetch(`${services.geo_awareness}/geozone/${id}`, {
                method: 'DELETE',
            });
            if (!response.ok) throw new Error(`Error deleting Geo Zone ${id}: ${response.statusText}`);
            return id;
        } catch (error) {
            setError(error);
            return null;
        }
        finally {
            setLoading(false);
        }
    }, [services.geo_awareness]);

    const deleteAllGeoZones = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            if (!services.geo_awareness) throw new Error('Geo Awareness service is not available');
            const response = await fetch(`${services.geo_awareness}/geozone`, {
                method: 'DELETE',
            });
            if (!response.ok) throw new Error(`Error deleting all Geo Zones: ${response.statusText}`);
            return true;
        } catch (error) {
            setError(error);
            return false;
        }
        finally {
            setLoading(false);
        }
    }, [services.geo_awareness]);


    const fetchSupportPoints = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            if (!services.geo_awareness) throw new Error('Geo Awareness service is not available');
            const response = await fetch(`${services.geo_awareness}/support-point`);
            if (!response.ok) throw new Error(`Failed to fetch Support Points: ${response.statusText}`);
            const data = await response.json();
            setSupportPoints(data);
        } catch (error) {
            setError(error);
        }
        finally {
            setLoading(false);
        }
    }, [services.geo_awareness]);

    const updateSupportPoint = useCallback(async (supportPoint) => {
        setLoading(true);
        setError(null);
        try {
            if (!services.geo_awareness) throw new Error('Geo Awareness service is not available');
            const response = await fetch(`${services.geo_awareness}/support-point/${supportPoint.id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(supportPoint),
            });
            if (!response.ok) throw new Error(`Error updating Support Point ${supportPoint.id}: ${response.statusText}`);
            return supportPoint.id;
        } catch (error) {
            setError(error);
            return null;
        }
        finally {
            setLoading(false);
        }
    }, [services.geo_awareness]);

    const addSupportPoint = useCallback(async (supportPoint) => {
        setLoading(true);
        setError(null);
        try {
            if (!services.geo_awareness) throw new Error('Geo Awareness service is not available');
            const response = await fetch(`${services.geo_awareness}/support-point`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(supportPoint),
            });
            if (!response.ok) throw new Error(`Error adding Support Point: ${response.statusText}`);
            const locationHeader = response.headers.get('Location');
            const id = locationHeader?.split('/').pop();
            return id;
        } catch (error) {
            setError(error);
            return null;
        }
        finally {
            setLoading(false);
        }
    }, [services.geo_awareness]);

    const deleteSupportPoint = useCallback(async (id) => {
        setLoading(true);
        setError(null);
        try {
            if (!services.geo_awareness) throw new Error('Geo Awareness service is not available');
            const response = await fetch(`${services.geo_awareness}/support-point/${id}`, {
                method: 'DELETE',
            });
            if (!response.ok) throw new Error(`Error deleting Support Point ${id}: ${response.statusText}`);
            return id;
        } catch (error) {
            setError(error);
            return null;
        }
        finally {
            setLoading(false);
        }
    }, [services.geo_awareness]);

    const deleteAllSupportPoints = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            if (!services.geo_awareness) throw new Error('Geo Awareness service is not available');
            const response = await fetch(`${services.geo_awareness}/support-point`, {
                method: 'DELETE',
            });
            if (!response.ok) throw new Error(`Error deleting all Support Points: ${response.statusText}`);
            return true;
        } catch (error) {
            setError(error);
            return false;
        }
        finally {
            setLoading(false);
        }
    }, [services.geo_awareness]);

    return {
        geoZones,
        loading,
        error,
        fetchGeoZones,
        addGeoZone,
        updateGeoZone,
        deleteGeoZone,
        deleteAllGeoZones,
        supportPoints,
        fetchSupportPoints,
        addSupportPoint,
        updateSupportPoint,
        deleteSupportPoint,
        deleteAllSupportPoints,
    };
}