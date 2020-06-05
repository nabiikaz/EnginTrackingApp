package com.pfe.enginapp.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.pfe.enginapp.models.Intervention;
import com.pfe.enginapp.models.SnappedPoints;
import com.pfe.enginapp.services.IGoogleMapsApiClient;
import com.pfe.enginapp.services.IUserClient;
import com.pfe.enginapp.services.retrofitClient;

import java.lang.ref.WeakReference;
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
