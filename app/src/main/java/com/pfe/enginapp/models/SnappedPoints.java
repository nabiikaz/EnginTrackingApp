package com.pfe.enginapp.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class SnappedPoints {
    List<SnappedPoint> snappedPoints;


    public List<SnappedPoint> getSnappedPoints() {
        return snappedPoints;
    }

    public void setSnappedPoints(List<SnappedPoint> snappedPoints) {
        this.snappedPoints = snappedPoints;
    }


    public int getSize(){
        return snappedPoints.size();
    }


    public class SnappedPoint {
        Location location;
        int originalIndex;
        String placeId;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public int getOriginalIndex() {
            return originalIndex;
        }

        public void setOriginalIndex(int originalIndex) {
            this.originalIndex = originalIndex;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public class Location{
             float latitude,longitude;


            public float getLatitude() {
                return latitude;
            }

            public void setLatitude(float latitude) {
                this.latitude = latitude;
            }

            public float getLongitude() {
                return longitude;
            }

            public void setLongitude(float longitude) {
                this.longitude = longitude;
            }


            public LatLng getLatLng(){
                return new LatLng(getLatitude(),getLongitude());
            }
        }
    }
}
