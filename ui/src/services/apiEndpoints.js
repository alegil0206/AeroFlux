const apiEndpoints = {
    drone_identification: "http://api.uspace.local/drone-identification",
    geo_awareness: "http://api.uspace.local/geo-awareness",
    geo_authorization: "http://api.uspace.local/geo-authorization",
    weather: "http://api.uspace.local/weather"
};

export const setEndpoint = (key, value) => {
    if (apiEndpoints.hasOwnProperty(key)) {
        apiEndpoints[key] = value;
    } else {
        throw new Error(`Invalid endpoint key: ${key}`);
    }
};

export const getEndpoint = (key) => {
    return apiEndpoints[key] || null;
};

export const getAllEndpoints = () => {
    return { ...apiEndpoints };
};
