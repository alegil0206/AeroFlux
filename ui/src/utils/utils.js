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

