package com.pfe.enginapp.ui.Dashboard;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pfe.enginapp.R;
import com.pfe.enginapp.adapters.TeamMemebersRecyclerViewAdapter;
import com.pfe.enginapp.models.Agent;
import com.pfe.enginapp.models.Team;
import com.pfe.enginapp.viewmodels.DashboardViewModel;
import com.pfe.enginapp.viewmodels.TeamMembersListViewModel;

import java.util.ArrayList;

public class TeamMembersList extends Fragment {


    private static String authToken;
    private RecyclerView teamMembersRecyclerView;
    private  TeamMemebersRecyclerViewAdapter adapter;




    private TeamMembersListViewModel mTeamMembersListViewModel;



    public TeamMembersList(String authToken){
        this.authToken = authToken;


    }


    private TeamMembersListViewModel mViewModel;

    public static TeamMembersList newInstance() {
        return new TeamMembersList(authToken);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_members_list_fragment, container, false);

        //initializing the Views on this Fragment
        initViews(view);



       initDashboardViewModel();


        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TeamMembersListViewModel.class);
        // TODO: Use the ViewModel
    }



    public void initViews(View view){
        //recyclerView
        teamMembersRecyclerView = view.findViewById(R.id.teamMembersRecyclerView);
        initRecyclerView();
        /////////////////


    }



    private void initRecyclerView(){
        adapter = new TeamMemebersRecyclerViewAdapter(this.getContext());



        teamMembersRecyclerView.setAdapter(adapter);



        teamMembersRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }

    private void initDashboardViewModel(){

        mTeamMembersListViewModel = new ViewModelProvider(this).get(TeamMembersListViewModel.class);

        mTeamMembersListViewModel.init(authToken);
        mTeamMembersListViewModel.getTeam().observe(getViewLifecycleOwner(), new Observer<Team>() {
            @Override
            public void onChanged(Team team) {

                adapter.setTeamAgents(team.getAgents());

                adapter.notifyDataSetChanged();




            }
        });


    }

}
