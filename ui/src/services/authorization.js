import { getEndpoint } from "./apiEndpoints";


export const fetchAuthorizations = async () => {
  const endpoint = getEndpoint("geo_authorization");
  if (!endpoint) throw new Error("Authorization API endpoint not set");

  const response = await fetch(`${endpoint}/authorization`);
  if (!response.ok) throw new Error(`Error fetching authorizations: ${response.statusText}`);
  return response.json();
};

export const addAuthorization = async (authorization) => {
  const endpoint = getEndpoint("geo_authorization");
  if (!endpoint) throw new Error("Authorization API endpoint not set");

  const response = await fetch(`${endpoint}/authorization`, {
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
  const endpoint = getEndpoint("geo_authorization");
  if (!endpoint) throw new Error("Authorization API endpoint not set");

  const response = await fetch(`${endpoint}/authorization/revoked/${id}`, {
    method: 'POST',
  });
  if (!response.ok) throw new Error(`Error revoking authorization ${id}: ${response.statusText}`);
  return id;
};

export const deleteAllAuthorizations = async () => {
  const endpoint = getEndpoint("geo_authorization");
  if (!endpoint) throw new Error("Authorization API endpoint not set");

  const response = await fetch(`${endpoint}/authorization`, {
    method: 'DELETE',
  });
  if (!response.ok) throw new Error(`Error deleting authorizations: ${response.statusText}`);
  return;
};
