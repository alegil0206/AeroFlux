import { useEffect, useState } from "react";
import { useSettings } from "../context/SettingContext";

const EARTH_RADIUS = 6378137.0; // Raggio della Terra in metri
const HALF_SQUARE_SIZE = 30000; // 30 km

const calculateNewCoordinates = (lat, lon, deltaX, deltaY) => {
    const latRad = lat * Math.PI / 180;
    const newLat = lat + (deltaX / EARTH_RADIUS) * (180 / Math.PI);
    const newLon = lon + (deltaY / (EARTH_RADIUS * Math.cos(latRad))) * (180 / Math.PI);
    return [newLon, newLat];
};

const calculateMapBounds = (coordinates) => {
    const southwest = calculateNewCoordinates(coordinates.latitude, coordinates.longitude, -HALF_SQUARE_SIZE, -HALF_SQUARE_SIZE);
    const northeast = calculateNewCoordinates(coordinates.latitude, coordinates.longitude, HALF_SQUARE_SIZE, HALF_SQUARE_SIZE);
    return [southwest, northeast];
}

const calculateInitialView = (coordinates) => {
    return {
        longitude: coordinates.longitude,
        latitude: coordinates.latitude,
        zoom: 10,
    }
}

export const useMapSettings = () => {
    const { coordinates } = useSettings();
    const [initialViewState, setInitialViewState] = useState(calculateInitialView(coordinates));
    const [mapBounds, setMapBounds] = useState(calculateMapBounds(coordinates));

    useEffect(() => {
        setMapBounds(calculateMapBounds(coordinates));
        setInitialViewState(calculateInitialView(coordinates));
    }, [coordinates]);

    return { initialViewState, mapBounds };
};
