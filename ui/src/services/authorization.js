const API_ENDPOINT = window.env.GEO_AUTHORIZATION_ENDPOINT;

export const fetchAuthorizations = async () => {
  const response = await fetch(`${API_ENDPOINT}/authorization`);
  if (!response.ok) throw new Error(`Error fetching authorizations: ${response.statusText}`);
  return response.json();
};

export const addAuthorization = async (authorization) => {
  const response = await fetch(`${API_ENDPOINT}/authorization`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(authorization),
  });
  const responseData = await response.json();
  if (response.status === 403) {
    throw new Error(responseData.message || 'Forbidden');
  }
  if (!response.ok) throw new Error(`Error adding authorization: ${response.statusText}`);
  const locationHeader = response.headers.get('Location');
  const id = locationHeader?.split('/').pop();
  return id;
};

export const revokeAuthorization = async (id) => {
  const response = await fetch(`${API_ENDPOINT}/authorization/revoked/${id}`, {
    method: 'POST',
  });
  if (!response.ok) throw new Error(`Error revoking authorization ${id}: ${response.statusText}`);
  return id;
};

export const deleteAllAuthorizations = async () => {
  const response = await fetch(`${API_ENDPOINT}/authorization`, {
    method: 'DELETE',
  });
  if (!response.ok) throw new Error(`Error deleting authorizations: ${response.statusText}`);
  return;
};
