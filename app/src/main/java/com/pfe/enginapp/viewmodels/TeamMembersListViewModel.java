package com.pfe.enginapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pfe.enginapp.models.Agent;
import com.pfe.enginapp.models.Team;
import com.pfe.enginapp.repositories.AgentRepository;

import java.util.List;

public class TeamMembersListViewModel extends ViewModel {

    private String authToken;
    private MutableLiveData<Team> mTeam;
    private AgentRepository mAgentRepository;

    public TeamMembersListViewModel() {
        super();




    }

    public void init(String authToken){
        this.authToken = authToken;
        if(mTeam != null){
            return;
        }

        mAgentRepository = AgentRepository.getInstance(authToken);
        mTeam = new MutableLiveData<>();

        //mAgentRepository.getTeam(mTeam);




    }

    public LiveData<Team> getTeam(){
        mAgentRepository.getTeam(mTeam);

        return mTeam;
    }


// TODO: Implement the ViewModel
}
