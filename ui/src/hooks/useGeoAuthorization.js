import { useState, useCallback} from 'react';
import { useSettings } from '../contexts/SettingContext';

export const useGeoAuthorization = () => {
    const { services } = useSettings();
    const [authorization, setAuthorization] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchAuthorizations = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            if (!services.geo_authorization) throw new Error('Geo Authorization service is not available');
            const response = await fetch(`${services.geo_authorization}/authorization`);
            if (!response.ok) throw new Error(`Failed to fetch Geo Authorization: ${response.statusText}`);
            const data = await response.json();
            setAuthorization(data);
        } catch (error) {
            setError(error);
        }
        finally {
            setLoading(false);
        }
    }, [services.geo_authorization]);

    const addAuthorization = useCallback(async (authorizationData) => {
        setLoading(true);
        setError(null);
        try {
            if (!services.geo_authorization) throw new Error('Geo Authorization service is not available');
            const response = await fetch(`${services.geo_authorization}/authorization`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(authorizationData),
            });
            const responseData = await response.json();
            if (!response.ok) throw new Error(`Error adding Geo Authorization: ${response.statusText}`);
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
    }, [services.geo_authorization]);

    const revokeAuthorization = useCallback(async (id) => {
        setLoading(true);
        setError(null);
        try {
            if (!services.geo_authorization) throw new Error('Geo Authorization service is not available');
            const response = await fetch(`${services.geo_authorization}/authorization/revoked/${id}`, {
                method: 'POST',
            });
            if (!response.ok) throw new Error(`Error deleting Geo Authorization ${id}: ${response.statusText}`);
            return id;
        } catch (error) {
            setError(error);
            return null;
        }
        finally {
            setLoading(false);
        }
    }, [services.geo_authorization]);

    const deleteAllAuthorizations = useCallback(async () => {
        setLoading(true);
        setError(null);
        try {
            if (!services.geo_authorization) throw new Error('Geo Authorization service is not available');
            const response = await fetch(`${services.geo_authorization}/authorization`, {
                method: 'DELETE',
            });
            if (!response.ok) throw new Error(`Error deleting Geo Authorizations: ${response.statusText}`);
            return true;
        } catch (error) {
            setError(error);
            return false;
        }
        finally {
            setLoading(false);
        }
    }, [services.geo_authorization]);

    return {
        authorization,
        loading,
        error,
        fetchAuthorizations,
        addAuthorization,
        revokeAuthorization,
        deleteAllAuthorizations,
    };
}

