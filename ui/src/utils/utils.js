export const getGeoZoneColor = (zone) => {
    if (zone.status === 'INACTIVE') {
      return 'rgba(169, 169, 169, 0.5)';
    } else if (zone.status === 'ACTIVE' && zone.category === 'RESTRICTED') {
      return 'rgba(255, 166, 0, 0.74)';
    } else if (zone.status === 'ACTIVE' && zone.category === 'EXCLUDED') {
      return 'rgba(255, 0, 0, 0.5)';
    }
    return 'rgba(169, 169, 169, 0.5)';
  };

export const getDefaultMapBounds = () => {
  return [
    [8.7064543, 45.1642737], // Southwest coordinates
    [9.7330497, 45.8835283], // Northeast coordinates
  ];
}

export const getDefaultInitialViewState = () => {
  return {
    longitude: 9.219752,
    latitude: 45.476592,
    zoom: 1,
  };
}

