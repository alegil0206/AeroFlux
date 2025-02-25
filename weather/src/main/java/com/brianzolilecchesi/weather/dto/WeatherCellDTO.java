package com.brianzolilecchesi.weather.dto;

public class WeatherCellDTO {
    private int row, col;
    private double lat, lon;
    private boolean rain;

    public WeatherCellDTO(int row, int col, double lat, double lon, boolean rain) {
        this.row = row;
        this.col = col;
        this.lat = lat;
        this.lon = lon;
        this.rain = rain;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }
    public boolean isRain() { return rain; }
}
