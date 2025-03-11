import { useState, useCallback} from 'react';
import { useSettings } from '../context/SettingContext';

export const useGeoAwareness = () => {
    const { services } = useSettings();
    const [geoZones, setGeoZones] = useState([]);
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

    return {
        geoZones,
        loading,
        error,
        fetchGeoZones,
        addGeoZone,
        updateGeoZone,
        deleteGeoZone,
        deleteAllGeoZones,
    };
}