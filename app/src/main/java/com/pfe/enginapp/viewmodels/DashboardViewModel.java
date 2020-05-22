package com.pfe.enginapp.viewmodels;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pfe.enginapp.models.Agent;
import com.pfe.enginapp.repositories.AgentRepository;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<Agent> mAgent;

    private AgentRepository agentRepository;

    private String authToken;

    public void init(String authToken){

        if(mAgent != null){
            return;
        }

        this.authToken = authToken;

        agentRepository = agentRepository.getInstance(authToken);
        mAgent = new MutableLiveData<>();

    }

    public LiveData<Agent> getSignedInAgent(){
        agentRepository.getAgent(mAgent,"5e7bab5e98fa4d214c94e2ed");

        return mAgent;

    }

}
