package com.pfe.enginapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.pfe.enginapp.models.Hospital;
import com.pfe.enginapp.models.Intervention;
import com.pfe.enginapp.models.Intervention.Transfere;
import com.pfe.enginapp.models.SnappedPoints;
import com.pfe.enginapp.repositories.MapApiRepository;

import java.util.List;

public class MapsViewModel extends ViewModel {

    MutableLiveData<List<SnappedPoints.SnappedPoint>> mSnappedPoints;
    MutableLiveData<List<Hospital>> mHospitals;

    MutableLiveData<Intervention> mIntervention;

    MapApiRepository mMapApiRepository;

    String authToken;


    private final static int MAX_SIZE = 10;
    String raw_path = "";
    int raw_path_positions_size = 0;


    public void init(String authToken){

        this.authToken = authToken;

        if(mSnappedPoints != null){
            return;
        }



        mMapApiRepository = MapApiRepository.getInstance(authToken);
        mSnappedPoints = new MutableLiveData<>();
        mIntervention = new MutableLiveData<>();
        mHospitals = new MutableLiveData<>();

    }


    public LiveData<List<SnappedPoints.SnappedPoint>> getSnappedPoints(){

        return mSnappedPoints;
    }

    public LiveData<Intervention> fetchIntervention(String id_team){
        mMapApiRepository.getIntervention(mIntervention,id_team);

        return mIntervention;
    }

    public Intervention getIntervention(){


        return mIntervention.getValue();
    }

    public void clearIntervention(){
        mIntervention = new MutableLiveData<>();

    }



    public void updateIntervention(Intervention intervention){

        mMapApiRepository.updateIntervention(mIntervention,intervention);

    }

    public  void updateHospitalTransfer(String hospitalId){
        mIntervention.getValue().getTransfere().setHospital(hospitalId);




        mMapApiRepository.updateIntervention(mIntervention,mIntervention.getValue());
    }

    public LiveData<List<Hospital>> getHospitals(LatLng position){
        if(position != null){
            mMapApiRepository.getHospitals(mHospitals,position);
        }

        return mHospitals;
    }




    public void addRawPosition(LatLng latLng){

        if(raw_path_positions_size == MAX_SIZE){
            mMapApiRepository.getSnapToRoadPositions(mSnappedPoints,raw_path);

            raw_path = "";
            raw_path_positions_size = 0;
        }else{
            String tmp =  latLng.latitude+","+latLng.longitude;

            if(raw_path_positions_size == 0){
                raw_path = tmp;
            }else
                raw_path = raw_path+ "|" + tmp;

            raw_path_positions_size++;
        }





    }
    // TODO: Implement the ViewModel
}
