package com.pfe.enginapp.models;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.pfe.enginapp.repositories.MapApiRepository;

import java.util.ArrayList;


public class Path {
    private static final String TAG = "Path";
    private final static int MAX_SIZE = 3;


    ArrayList<LatLng> path = new ArrayList<>();

    String raw_path = "";
    int raw_path_positions_size = 0;

    public ArrayList<LatLng> getPath() {
        return path;
    }

    public void setPath(ArrayList<LatLng> path) {
        this.path = path;
    }

    public void addPosition(LatLng latLng){

        if(raw_path_positions_size == MAX_SIZE){
            snapToRoad();
        }else{
            String tmp =  latLng.latitude+","+latLng.longitude;

            if(raw_path_positions_size == 0){
                raw_path = tmp;
            }else
                raw_path = raw_path+ "|" + tmp;

            raw_path_positions_size++;
        }





    }

    public void snapToRoad(){

        //MapApiRepository.getInstance().FetchSnapToRoadPositions(raw_path);

        Log.d(TAG, "snapToRoad: ");
        Log.d(TAG, "snapToRoad: raw_path:"+raw_path);
        raw_path = "";
        raw_path_positions_size=0;

    }

    @Override
    public String toString() {

        String result = "";

        for (int i = 0; i < path.size(); i++) {

            String tmp =  path.get(i).latitude+","+path.get(i).longitude;

            if(i == 0){
                result = tmp;
            }else
                result = result+ "|" + tmp;

        }

        return result;



    }

    public void clear(){
        path.clear();
    }
}
