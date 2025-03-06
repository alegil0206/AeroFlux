import { getEndpoint } from "./apiEndpoints";

export const fetchDrones = async () => {
  const endpoint = getEndpoint("drone_identification");
  if (!endpoint) throw new Error("Drone Identification API endpoint not set");

  const response = await fetch(`${endpoint}/drone`);
  if (!response.ok) throw new Error(`Failed to fetch drones: ${response.statusText}`);
  return await response.json();
};

export const addDrone = async (drone) => {
  const endpoint = getEndpoint("drone_identification");
  if (!endpoint) throw new Error("Drone Identification API endpoint not set");

  const response = await fetch(`${endpoint}/drone`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(drone),
  });
  if (!response.ok) throw new Error(`Error adding Drone: ${response.statusText}`);
  const locationHeader = response.headers.get('Location');
  const id = locationHeader?.split('/').pop();
  return id;
};

export const updateDrone = async (id, droneData) => {
  const endpoint = getEndpoint("drone_identification");
  if (!endpoint) throw new Error("Drone Identification API endpoint not set");

  const response = await fetch(`${endpoint}/drone/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(droneData),
  });
  if (!response.ok) throw new Error(`Error updating Drone ${id}: ${response.statusText}`);
  return droneData.id;
};

export const deleteDrone = async (id) => {
  const endpoint = getEndpoint("drone_identification");
  if (!endpoint) throw new Error("Drone Identification API endpoint not set");

  const response = await fetch(`${endpoint}/drone/${id}`, {
    method: "DELETE",
  });
  if (!response.ok) throw new Error(`Error deleting Drone ${id}: ${response.statusText}`);
  return id;
};

export const deleteAllDrones = async () => {
  const endpoint = getEndpoint("drone_identification");
  if (!endpoint) throw new Error("Drone Identification API endpoint not set");
  
  const response = await fetch(`${endpoint}/drone`, {
    method: "DELETE",
  });
  if (!response.ok) throw new Error(`Error deleting Drones: ${response.statusText}`);
  return;
};
