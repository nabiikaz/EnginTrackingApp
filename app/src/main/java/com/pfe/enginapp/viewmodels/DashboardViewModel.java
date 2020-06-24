package com.pfe.enginapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pfe.enginapp.models.Agent;
import com.pfe.enginapp.repositories.AgentRepository;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<Agent> mAgent;

    private AgentRepository mAgentRepository;

    private String authToken;

    public void init(String authToken){

        if(mAgent != null){
            return;
        }

        this.authToken = authToken;

        mAgentRepository = mAgentRepository.getInstance(authToken);
        mAgent = new MutableLiveData<>();

    }

    public LiveData<Agent> getSignedInAgent(String idAgent){
        mAgentRepository.getAgent(mAgent,idAgent);

        return mAgent;

    }

}
