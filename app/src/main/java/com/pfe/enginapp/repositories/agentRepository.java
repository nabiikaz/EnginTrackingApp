package com.pfe.enginapp.repositories;

import androidx.lifecycle.MutableLiveData;

import com.pfe.enginapp.models.agent;

public class agentRepository {

    private static agentRepository instance;

    private agent agent;

    public static agentRepository getInstance(){
        if(instance == null){
            instance = new agentRepository();
        }
        return instance;

    }

    public MutableLiveData<agent> getAgent(){
        //this mimicks the retrieval of data from database
        setAgent();

        MutableLiveData<agent> data = new MutableLiveData<>();

        data.setValue(this.agent);

        return  data;



    }

    public void setAgent(){
        agent = new agent();
        agent.setNom("Nabi Zakaria");

    }


}
