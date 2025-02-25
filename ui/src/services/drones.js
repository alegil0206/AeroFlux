const API_ENDPOINT = window.env.DRONE_IDENTIFICATION_ENDPOINT;

export const fetchDrones = async () => {
  const response = await fetch(`${API_ENDPOINT}/drone`);
  if (!response.ok) throw new Error(`Failed to fetch drones: ${response.statusText}`);
  return await response.json();
};

export const addDrone = async (drone) => {
  const response = await fetch(`${API_ENDPOINT}/drone`, {
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
  const response = await fetch(`${API_ENDPOINT}/drone/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(droneData),
  });
  if (!response.ok) throw new Error(`Error updating Drone ${id}: ${response.statusText}`);
  return droneData.id;
};

export const deleteDrone = async (id) => {
  const response = await fetch(`${API_ENDPOINT}/drone/${id}`, {
    method: "DELETE",
  });
  if (!response.ok) throw new Error(`Error deleting Drone ${id}: ${response.statusText}`);
  return id;
};

export const deleteAllDrones = async () => {
  const response = await fetch(`${API_ENDPOINT}/drone`, {
    method: "DELETE",
  });
  if (!response.ok) throw new Error(`Error deleting Drones: ${response.statusText}`);
  return;
};
