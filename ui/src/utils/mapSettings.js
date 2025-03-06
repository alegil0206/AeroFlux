const DEFAULT_COORDINATES = { longitude: 9.219752, latitude: 45.476592 };
const EARTH_RADIUS = 6378137.0; ; // Raggio della Terra in metri (approssimativamente)
const HALF_SQUARE_SIZE = 30000; // Metà del lato del quadrato in metri (30 km)

let mapCoordinates = { ...DEFAULT_COORDINATES };
let mapBounds = [];

export const getCoordinates = () => mapCoordinates;

export const setCoordinates = (longitude, latitude) => {
    mapCoordinates = { longitude, latitude };
};

function calculateNewCoordinates(lat, lon, deltaX, deltaY) {
    const latRad = lat * Math.PI / 180;
    const newLat = lat + (deltaX / EARTH_RADIUS) * (180 / Math.PI);
    const newLon = lon + (deltaY / (EARTH_RADIUS * Math.cos(latRad))) * (180 / Math.PI);
    return [newLon, newLat];
}

// Funzione per calcolare i bounds dinamicamente
export const getMapBounds = () => mapBounds;

export const updateMapBounds = () => {
    const { longitude, latitude } = mapCoordinates;
    const southwest = calculateNewCoordinates(latitude, longitude, -HALF_SQUARE_SIZE, -HALF_SQUARE_SIZE)
    const northeast = calculateNewCoordinates(latitude, longitude, HALF_SQUARE_SIZE, HALF_SQUARE_SIZE)
    mapBounds = [ southwest, northeast];
}

// Funzione per ottenere il valore iniziale della mappa
export const getInitialViewState = () => {
    return {
        longitude: mapCoordinates.longitude,
        latitude: mapCoordinates.latitude,
        zoom: 1,
    };
};

updateMapBounds();
