package com.pfe.enginapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pfe.enginapp.models.Agent;



public class LoginViewModel extends ViewModel {


    private MutableLiveData<Agent> agent;


    public void init(){
        //this means that the agent is already fetched up
        if(agent != null){
            return;
        }





    }

    public LiveData<Agent> getAgent(){
        return agent;


    }

}
