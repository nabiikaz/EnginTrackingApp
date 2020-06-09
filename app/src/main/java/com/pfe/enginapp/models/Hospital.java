package com.pfe.enginapp.models;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

public class Hospital {

    String name,numTel;
    Gps_coordonnee gps_coordonnee;

    DistanceMatrixResult.SubElement distance;
    DistanceMatrixResult.SubElement duration;

    public DistanceMatrixResult.SubElement getDistance() {
        return distance;
    }

    public void setDistance(DistanceMatrixResult.SubElement distance) {
        this.distance = distance;
    }

    public DistanceMatrixResult.SubElement getDuration() {
        return duration;
    }

    public void setDuration(DistanceMatrixResult.SubElement duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public Gps_coordonnee getGps_coordonnee() {
        return gps_coordonnee;
    }

    public void setGps_coordonnee(Gps_coordonnee gps_coordonnee) {
        this.gps_coordonnee = gps_coordonnee;
    }

    public class Gps_coordonnee{
        float lat,lng;

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public float getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }

        public LatLng getLatLng(){
            return  new LatLng(lat,lng);
        }

        @NonNull
        @Override
        public String toString() {
            return lat+","+lng;
        }
    }

}
