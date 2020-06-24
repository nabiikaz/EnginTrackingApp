package com.pfe.enginapp.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.pfe.enginapp.models.DistanceMatrixResult;
import com.pfe.enginapp.models.Hospital;
import com.pfe.enginapp.models.Intervention;
import com.pfe.enginapp.models.SnappedPoints;
import com.pfe.enginapp.services.IGoogleMapsApiClient;
import com.pfe.enginapp.services.IUserClient;
import com.pfe.enginapp.services.retrofitClient;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapApiRepository {

    private static MapApiRepository instance;

    private  Retrofit retrofit;

    private static String ROADS_BASE_URL = "https://roads.googleapis.com/v1/";
    private static String DISTANCE_BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/";
    private static final String TAG = "MapApiRepository";

    private   String authToken;

    public static MapApiRepository getInstance(String authToken){

        if(instance == null){
            instance = new MapApiRepository(authToken);
        }



        return  instance;
    }

    private MapApiRepository(String authToken){
        this.authToken = authToken;
    }


    public void getSnapToRoadPositions(MutableLiveData<List<SnappedPoints.SnappedPoint>> mSnappedPoints, String raw_path){

        Log.d(TAG, "FetchSnapToRoadPositions: ");

        Retrofit retrofit = getRetrofit(ROADS_BASE_URL);

        IGoogleMapsApiClient userClient = retrofit.create(IGoogleMapsApiClient.class);

       /* Call<SnappedPoints> call = userClient.snapToRoads("-35.27801,149.12958|-35.28032,149.12907|-35.28099,149.12929|-35.28144,149.12984|-35.28194,149.13003|-35.28282,149.12956|-35.28302,149.12881|-35.28473,149.12836",
                        "AIzaSyAOifFchSotR-YmTmtWsgybi62qqGmVjUU");*/

        Call<SnappedPoints> call = userClient.snapToRoads(raw_path,"AIzaSyAOifFchSotR-YmTmtWsgybi62qqGmVjUU");

        call.enqueue(new SnapToRoadCallBack(mSnappedPoints,authToken));








    }




    public void getIntervention(MutableLiveData<Intervention> mIntervention,String id_team){

        Retrofit retrofit = new retrofitClient(authToken).getRetrofit();

        IUserClient userClient = retrofit.create(IUserClient.class);

        Call<Intervention> call = userClient.getIntervention(id_team);

        call.enqueue(new FetchIntervention(mIntervention));







    }

    public void updateIntervention(MutableLiveData<Intervention> mIntervention,Intervention intervention){
        Retrofit retrofit = new retrofitClient(authToken).getRetrofit();

        IUserClient userClient = retrofit.create(IUserClient.class);


        Call<Intervention> call = userClient.updateInterventionStatus(intervention.get_id(),intervention);

        call.enqueue(new FetchIntervention(mIntervention));


    }



    private Retrofit getRetrofit(String baseUrl){
        OkHttpClient.Builder  httpClient = new OkHttpClient.Builder();






        return  new Retrofit.Builder()
                .baseUrl(baseUrl)

                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())


                .client(httpClient.build()) // add this line
                .build();

    }

    public void getHospitals(MutableLiveData<List<Hospital>> mHospitals,LatLng position) {

        Retrofit retrofit = new retrofitClient(authToken).getRetrofit();

        IUserClient userClient = retrofit.create(IUserClient.class);

        Call<List<Hospital>> call = userClient.getHospitals();

        String positionStr = position.latitude + ","+position.longitude;

        call.enqueue(new FetchHopitals(mHospitals,positionStr));

    }

    private  class FetchHopitals implements  Callback<List<Hospital>>{

        private static final String TAG = "FetchHospitals";

        MutableLiveData<List<Hospital>> data ;
        String origin;


        public FetchHopitals(MutableLiveData<List<Hospital>> data,String origin){
            Log.d(TAG, "FetchHospitals: ");

            this.data = new WeakReference<>(data).get();
            this.origin = origin;

        }

        @Override
        public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {

            if(response.isSuccessful()){

               // Log.d(TAG, "onResponse: "+getDestinationsString(response.body()));

                final List<Hospital> hospitals = response.body();

                if(hospitals == null)
                    return;

                data.postValue(hospitals);






              Retrofit retrofit = getRetrofit(DISTANCE_BASE_URL);

              IGoogleMapsApiClient userClient = retrofit.create(IGoogleMapsApiClient.class);



                Call<DistanceMatrixResult> secondCall = userClient.getDistanceMatrixResult(origin,getDestinationsString(hospitals),"AIzaSyAOifFchSotR-YmTmtWsgybi62qqGmVjUU");


                Log.d(TAG, "onResponse: url"+secondCall.request().url());
                secondCall.enqueue(new FetchDistances(this.data));


                

            }

        }

        @Override
        public void onFailure(Call<List<Hospital>> call, Throwable t) {

        }
        
        public String getDestinationsString(List<Hospital> hospitals){
            StringBuilder result = new StringBuilder();

            for (Hospital hospital : hospitals) {

                result.append(hospital.getGps_coordonnee().toString()).append("|");

                
            }






            return result.toString();
            
        }
    }


    private  class FetchDistances implements  Callback<DistanceMatrixResult>{

        private static final String TAG = "FetchDistances";

        MutableLiveData<List<Hospital>> data ;


        public FetchDistances(MutableLiveData<List<Hospital>> data){
            

            this.data = new WeakReference<>(data).get();

        }

        @Override
        public void onResponse(Call<DistanceMatrixResult> call, Response<DistanceMatrixResult> response) {
            Log.d(TAG, "secondCall: ");
            List<Hospital> hospitals = data.getValue();
            if(response.isSuccessful()){

                List<DistanceMatrixResult.Element> elements = response.body().getRows().get(0).getElements() ;
                Log.d(TAG, "onResponse: distance response "+elements.get(0).getDistance().getText());
                for (int i = 0; i < elements.size(); i++) {
                    hospitals.get(i).setDistance(elements.get(i).getDistance());
                    hospitals.get(i).setDuration(elements.get(i).getDuration());

                }

                Collections.sort(hospitals, new Comparator<Hospital>(){
                    @Override
                    public int compare(Hospital o1, Hospital o2) {
                        return Long.valueOf(o1.getDistance().getValue()).compareTo(o2.getDistance().getValue());


                    }


                });

                data.postValue(hospitals);

            }else{
                Log.d(TAG, "onResponse: distance not successful status"+response.code()+" \n body"+response.body());

            }
        }

        @Override
        public void onFailure(Call<DistanceMatrixResult> call, Throwable t) {
            Log.d(TAG, "secondCall F: ");
            t.printStackTrace();


        }
    }





    private  class FetchIntervention implements  Callback<Intervention>{

        private static final String TAG = "FetchIntervention";

        MutableLiveData<Intervention> data ;


        public FetchIntervention(MutableLiveData<Intervention> data){
            Log.d(TAG, "FetchIntervention: ");

            this.data = new WeakReference<>(data).get();

        }

        @Override
        public void onResponse(Call<Intervention> call, Response<Intervention> response) {

            if(response.isSuccessful()){



                this.data.postValue(response.body());

               Log.d(TAG, "onResponse: Successful"+ (response.body() != null ? response.body().getStatut() : "null"));

            }

        }

        @Override
        public void onFailure(Call<Intervention> call, Throwable t) {

        }
    }




    private  class SnapToRoadCallBack implements Callback<SnappedPoints> {
        private static final String TAG = "SnapToRoadCallBack";

        private WeakReference<MutableLiveData<SnappedPoints.SnappedPoint>> dataRef;
        private MutableLiveData<List<SnappedPoints.SnappedPoint>> data;

        String authToken ;

        public SnapToRoadCallBack(MutableLiveData<List<SnappedPoints.SnappedPoint>> data,String authToken){
            this.data = new WeakReference<>(data).get();

            this.authToken = authToken;

        }
        @Override
        public void onResponse(Call<SnappedPoints> call, Response<SnappedPoints> response) {

            if(response.isSuccessful()){

                List<SnappedPoints.SnappedPoint> result;

                if(response.body() != null){
                    result = response.body().getSnappedPoints() ;
                    if(result.size() == 0)
                        return;

                }else{
                    return;
                }



                List<SnappedPoints.SnappedPoint> currentSnappedPoints = data.getValue();

                SnappedPoints.SnappedPoint.Location lastLocation;
                if (currentSnappedPoints != null) {


                    currentSnappedPoints.addAll(result);
                }else{
                    currentSnappedPoints =result;
                }

                data.postValue(currentSnappedPoints);




                lastLocation  = currentSnappedPoints.get(currentSnappedPoints.size()-1).getLocation();




                //get the last location coordinates

                SendLastLocationToServer(lastLocation);






            }else{

            }
        }


        @Override
        public void onFailure(Call<SnappedPoints> call, Throwable t) {
            //Log.d(TAG, "getAgent: failed : \n");
            t.printStackTrace();

        }


        public void SendLastLocationToServer(SnappedPoints.SnappedPoint.Location lastLocation){


            Retrofit retrofit = new retrofitClient(authToken).getRetrofit();




            IGoogleMapsApiClient userClient = retrofit.create(IGoogleMapsApiClient.class);



            Call<ResponseBody> call = userClient.setAdresseTeam(lastLocation);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }



    }










}
