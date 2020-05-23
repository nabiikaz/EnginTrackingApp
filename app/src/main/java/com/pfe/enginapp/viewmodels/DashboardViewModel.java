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

    public LiveData<Agent> getSignedInAgent(){
        mAgentRepository.getAgent(mAgent,"5e7bab5e98fa4d214c94e2ed");

        return mAgent;

    }

}
