import { useState, useCallback} from 'react';
import { useSettings } from '../context/SettingContext';

export const useDroneIdentification = () => {
    const { services } = useSettings();
    const [drones, setDrones] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchDrones = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            if (!services.drone_identification) throw new Error('Drone Identification service is not available');
            const response = await fetch(`${services.drone_identification}/drone`);
            if (!response.ok) throw new Error(`Failed to fetch Drone Identification: ${response.statusText}`);
            const data = await response.json();
            setDrones(data);
        } catch (error) {
            setError(error);
        }
        finally {
            setLoading(false);
        }
    }, [services.drone_identification]);

    const addDrone = useCallback(async (drone) => {
        setLoading(true);
        setError(null);
        try {
            if (!services.drone_identification) throw new Error('Drone Identification service is not available');
            const response = await fetch(`${services.drone_identification}/drone`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(drone),
            });
            if (!response.ok) throw new Error(`Error adding Drone: ${response.statusText}`);
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
    }, [services.drone_identification]);

    const updateDrone = useCallback(async (drone) => {
        setLoading(true);
        setError(null);
        try {
            if (!services.drone_identification) throw new Error('Drone Identification service is not available');
            const response = await fetch(`${services.drone_identification}/drone/${drone.id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(drone),
            });
            if (!response.ok) throw new Error(`Error updating Drone: ${response.statusText}`);
            return drone.id;
        } catch (error) {
            setError(error);
            return null;
        }
        finally {
            setLoading(false);
        }
    }, [services.drone_identification]);

    const deleteDrone = useCallback(async (id) => {
        setLoading(true);
        setError(null);
        try {
            if (!services.drone_identification) throw new Error('Drone Identification service is not available');
            const response = await fetch(`${services.drone_identification}/drone/${id}`, {
                method: 'DELETE',
            });
            if (!response.ok) throw new Error(`Error deleting Drone: ${response.statusText}`);
            return id;
        }
        catch (error) {
            setError(error);
            return null;
        }
        finally {
            setLoading(false);
        }
    }, [services.drone_identification]);

    const deleteAllDrones = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            if (!services.drone_identification) throw new Error('Drone Identification service is not available');
            const response = await fetch(`${services.drone_identification}/drone`, {
                method: 'DELETE',
            });
            if (!response.ok) throw new Error(`Error deleting Drones: ${response.statusText}`);
            return true;
        } catch (error) {
            setError(error);
            return false;
        }
        finally {
            setLoading(false);
        }
    }, [services.drone_identification]);

    return {
        drones,
        loading,
        error,
        fetchDrones,
        addDrone,
        updateDrone,
        deleteDrone,
        deleteAllDrones,
    };
}