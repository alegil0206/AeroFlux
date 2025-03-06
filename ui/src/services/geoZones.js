import { getEndpoint } from "./apiEndpoints";

export const fetchGeoZones = async () => {
  const endpoint = getEndpoint("geo_awareness");
  if (!endpoint) throw new Error("Geo Awareness API endpoint not set");

  const response = await fetch(`${endpoint}/geozone`);
  if (!response.ok) throw new Error(`Error fetching GeoZones: ${response.statusText}`);
  return response.json();
};

export const addGeoZone = async (geoZone) => {
  const endpoint = getEndpoint("geo_awareness");
  if (!endpoint) throw new Error("Geo Awareness API endpoint not set");

  const response = await fetch(`${endpoint}/geozone`, {
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
  const endpoint = getEndpoint("geo_awareness");
  if (!endpoint) throw new Error("Geo Awareness API endpoint not set");

  const response = await fetch(`${endpoint}/geozone/${geoZone.id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(geoZone),
  });
  if (!response.ok) throw new Error(`Error updating GeoZone ${geoZone.id}: ${response.statusText}`);
  return geoZone.id;
};

export const deleteGeoZone = async (id) => {
  const endpoint = getEndpoint("geo_awareness");
  if (!endpoint) throw new Error("Geo Awareness API endpoint not set");

  const response = await fetch(`${endpoint}/geozone/${id}`, {
    method: 'DELETE',
  });
  if (!response.ok) throw new Error(`Error deleting GeoZone ${id}: ${response.statusText}`);
  return id;
};

export const deleteAllGeoZones = async () => {
  const endpoint = getEndpoint("geo_awareness");
  if (!endpoint) throw new Error("Geo Awareness API endpoint not set");

  const response = await fetch(`${endpoint}/geozone`, {
    method: 'DELETE',
  });
  if (!response.ok) throw new Error(`Error deleting GeoZones: ${response.statusText}`);
  return;
};
