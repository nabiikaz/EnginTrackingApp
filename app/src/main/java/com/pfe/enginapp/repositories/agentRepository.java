package com.pfe.enginapp.repositories;

import androidx.lifecycle.MutableLiveData;

import com.pfe.enginapp.models.Agent;

public class agentRepository {

    private static agentRepository instance;

    private Agent agent;

    public static agentRepository getInstance(){
        if(instance == null){
            instance = new agentRepository();
        }
        return instance;

    }

    public MutableLiveData<Agent> getAgent(){
        //this mimicks the retrieval of data from database
        setAgent();

        MutableLiveData<Agent> data = new MutableLiveData<>();

        data.setValue(this.agent);

        return  data;



    }

    public void setAgent(){
        agent = new Agent();
        agent.setAgent_nom("Nabi Zakaria");

    }


}
