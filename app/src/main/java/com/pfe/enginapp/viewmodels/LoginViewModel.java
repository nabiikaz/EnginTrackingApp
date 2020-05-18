package com.pfe.enginapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pfe.enginapp.models.agent;
import com.pfe.enginapp.repositories.agentRepository;


public class LoginViewModel extends ViewModel {


    private MutableLiveData<agent> agent;
    private agentRepository mRepo;

    public void init(){
        //this means that the agent is already fetched up
        if(agent != null){
            return;
        }

        mRepo = agentRepository.getInstance();
        agent = mRepo.getAgent();

    }

    public LiveData<agent> getAgent(){
        return agent;


    }

}
