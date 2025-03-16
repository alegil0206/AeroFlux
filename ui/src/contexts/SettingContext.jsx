import React, { createContext, useState, useEffect, useContext } from 'react';

const SettingContext = createContext();

export const useSettings = () => {
  return useContext(SettingContext);
};

export const SettingProvider = ({ children }) => {
  const [coordinates, setCoordinates] = useState(null);
  const [services, setServices] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [mainService, setMainService] = useState(null);

  // Funzione per recuperare le coordinate
  const fetchCoordinates = async () => {
    try {
      const response = await fetch(`http://${mainService}/setting/coordinates`);
      if (!response.ok) throw new Error('Failed to fetch coordinates');
      const data = await response.json();
      setCoordinates(data);
    } catch (error) {
      setError(error);
    }
  };

  // Funzione per recuperare i servizi
  const fetchServices = async () => {
    try {
      const response = await fetch(`http://${mainService}/setting/service`);
      if (!response.ok) throw new Error('Failed to fetch services');
      const data = await response.json();
      const services = data.reduce((acc, service) => {
        acc[service.name] = service.url;
        return acc;
      }, {});
      setServices(services);
    } catch (error) {
      setError(error);
    }
  };

  const fetchSettings = async () => {
    setError(null);
    await Promise.all([fetchCoordinates(), fetchServices()]);
  };

  useEffect(() => {
    if (mainService)
      fetchSettings();
  }, [mainService]);

  useEffect(() => {
    if (coordinates && services) setLoading(false);
  }, [coordinates, services]);

  const updateCoordinates = async (newCoordinates) => {
    setError(null);
    try {
      const response = await fetch(`http://${mainService}/setting/coordinates`, {
        method: 'PUT',
        body: JSON.stringify(newCoordinates),
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) throw new Error('Failed to update coordinates');
      return true;
    } catch (error) {
      setError(error);
      return false;
    }
  };

  const updateServiceUrl = async (serviceName, newUrl) => {
    try {
      const response = await fetch(`http://${mainService}/setting/service/${serviceName}?newUrl=${encodeURIComponent(newUrl)}`, {
        method: 'PUT',
      });
      if (!response.ok) throw new Error('Failed to update service URL');
      return true;
    } catch (error) {
      setError(error);
      return false;
    }
  };

  const connectToMainService = async (mainServiceUrl) => {
    if (mainServiceUrl === mainService)
      fetchSettings();
    else
      setMainService(mainServiceUrl);
  };

  return (
    <SettingContext.Provider value={{ mainService, connectToMainService, coordinates, services, loading, error, updateCoordinates, updateServiceUrl, fetchSettings }}>
      {children}
    </SettingContext.Provider>
  );
};
