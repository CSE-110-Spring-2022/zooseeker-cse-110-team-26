package com.example.zooapplication;

public class Coord {

    public double lat;
    public double lng;

    public Coord(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public static double getDist(Coord c1, Coord c2){
        return Math.sqrt(Math.pow((c1.lat - c2.lat), 2) + Math.pow(c1.lng - c2.lng, 2));
    }
}
