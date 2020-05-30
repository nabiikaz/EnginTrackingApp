package com.pfe.enginapp.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.pfe.enginapp.models.Agent;
import com.pfe.enginapp.models.Team;
import com.pfe.enginapp.services.IUserClient;
import com.pfe.enginapp.services.retrofitClient;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AgentRepository {

    private static AgentRepository instance;
    private static final String TAG = "AgentRepository";

    private  Retrofit retrofit;



    private Agent agent;

    public static AgentRepository getInstance(String authToken){
        if(instance == null){
            instance = new AgentRepository(authToken);
        }
        return instance;

    }

    private AgentRepository(String authToken){
        //this.authToken = authToken;
        //get retrofit instance with an authorization header added to it holding the authToken value
        retrofit = new retrofitClient(authToken).getRetrofit();
    }

    public void getAgent(MutableLiveData<Agent> mAgentMutableLiveData,String idAgent) {


        Log.d(TAG, "getAgent: ");

        IUserClient userClient = retrofit.create(IUserClient.class);



        Call<Agent> call = userClient.getAgent(idAgent);



        call.enqueue(new FetchAgentCallBack(mAgentMutableLiveData));

    }

    public void getTeam(MutableLiveData<Team> mTeam) {


        Log.d(TAG, "getAgent: ");

        IUserClient userClient = retrofit.create(IUserClient.class);



        Call<Team> call = userClient.getTeam();



        call.enqueue(new FetchTeamCallBack(mTeam));

    }




    public void setAgent(){
        agent = new Agent();
        agent.setAgent_nom("Nabi Zakaria");

    }


    private  class FetchAgentCallBack implements Callback<Agent> {

        private WeakReference<MutableLiveData<Agent>> dataRef;
        private MutableLiveData<Agent> data;

        public FetchAgentCallBack(MutableLiveData<Agent> data){
            this.data = new WeakReference<MutableLiveData<Agent>>(data).get();

        }
        @Override
        public void onResponse(Call<Agent> call, Response<Agent> response) {
            Log.d(TAG, "getAgent: after execute ");
            if(response.isSuccessful()){
                data.setValue(response.body());

            }else{

            }
        }

        @Override
        public void onFailure(Call<Agent> call, Throwable t) {
            //Log.d(TAG, "getAgent: failed : \n");
            t.printStackTrace();

        }
    }


    private  class FetchTeamCallBack implements Callback<Team> {

        private WeakReference<MutableLiveData<Team>> dataRef;
        private MutableLiveData<Team> data;

        public FetchTeamCallBack(MutableLiveData<Team> data){
            this.data = new WeakReference<MutableLiveData<Team>>(data).get();

        }
        @Override
        public void onResponse(Call<Team> call, Response<Team> response) {
            Log.d(TAG, "getAgent: after execute ");
            if(response.isSuccessful()){
                data.setValue(response.body());

            }else{

            }
        }

        @Override
        public void onFailure(Call<Team> call, Throwable t) {
            //Log.d(TAG, "getAgent: failed : \n");
            t.printStackTrace();

        }
    }






}
