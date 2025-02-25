const API_ENDPOINT = window.env.GEO_AWARENESS_ENDPOINT;

export const fetchGeoZones = async () => {
  const response = await fetch(`${API_ENDPOINT}/geozone`);
  if (!response.ok) throw new Error(`Error fetching GeoZones: ${response.statusText}`);
  return response.json();
};

export const addGeoZone = async (geoZone) => {
  const response = await fetch(`${API_ENDPOINT}/geozone`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(geoZone),
  });
  if (!response.ok) throw new Error(`Error adding GeoZone: ${response.statusText}`);
  const locationHeader = response.headers.get('Location');
  const id = locationHeader?.split('/').pop();
  return id;
};

export const updateGeoZone = async (geoZone) => {
  const response = await fetch(`${API_ENDPOINT}/geozone/${geoZone.id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(geoZone),
  });
  if (!response.ok) throw new Error(`Error updating GeoZone ${geoZone.id}: ${response.statusText}`);
  return geoZone.id;
};

export const deleteGeoZone = async (id) => {
  const response = await fetch(`${API_ENDPOINT}/geozone/${id}`, {
    method: 'DELETE',
  });
  if (!response.ok) throw new Error(`Error deleting GeoZone ${id}: ${response.statusText}`);
  return id;
};

export const deleteAllGeoZones = async () => {
  const response = await fetch(`${API_ENDPOINT}/geozone`, {
    method: 'DELETE',
  });
  if (!response.ok) throw new Error(`Error deleting GeoZones: ${response.statusText}`);
  return;
};
