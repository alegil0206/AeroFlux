import React, { createContext, useState, useEffect, useContext } from 'react';

const SettingContext = createContext();

export const useSettings = () => {
  return useContext(SettingContext);
};

export const SettingProvider = ({ children }) => {

  const defaultCoordinates = {
    latitude: 45.476592,
    longitude: 9.219752,
  };

  const defaultService = {
    DRONE_IDENTIFICATION: "http://uspace.aeroflux/drone-identification",
    GEO_AUTHORIZATION: "http://uspace.aeroflux/geo-authorization",
    GEO_AWARENESS: "http://uspace.aeroflux/geo-awareness",
    WEATHER: "http://uspace.aeroflux/weather",
    SIMULATOR: "http://simulator.aeroflux"
  };

  const [coordinates, setCoordinates] = useState(defaultCoordinates);
  const [services, setServices] = useState(defaultService);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);


  useEffect(() => {
    if (coordinates && services) setLoading(false);
  }, [coordinates, services]);

  const updateCoordinates = async (newCoordinates) => {
    setError(null);
    try {
      const response = await fetch(`${services.WEATHER}/setting/coordinates`, {
        method: 'PUT',
        body: JSON.stringify(newCoordinates),
        headers: {
          'Content-Type': 'application/json',
        },
      });
      if (!response.ok) throw new Error('Failed to update coordinates');
      setCoordinates(newCoordinates);
      return true;
    } catch (error) {
      setError(error);
      return false;
    }
  };

  const updateServiceUrl = async (serviceName, newUrl) => {
    try {
      const response = await fetch(`${services.SIMULATOR}/setting/service/${serviceName}?newUrl=${encodeURIComponent(newUrl)}`, {
        method: 'PUT',
      });
      if (!response.ok) throw new Error('Failed to update service URL');
      setServices((prevServices) => ({
        ...prevServices,
        [serviceName]: newUrl,
      }));
      return true;
    } catch (error) {
      setError(error);
      return false;
    }
  };

  return (
    <SettingContext.Provider value={{ coordinates, services, loading, error, updateCoordinates, updateServiceUrl }}>
      {children}
    </SettingContext.Provider>
  );
};
