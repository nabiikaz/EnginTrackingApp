package com.pfe.enginapp.ui.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.pfe.enginapp.R;
import com.pfe.enginapp.adapters.TabAdapter;
import com.pfe.enginapp.adapters.TeamMemebersRecyclerViewAdapter;
import com.pfe.enginapp.models.Agent;
import com.pfe.enginapp.services.AuthenticationService;
import com.pfe.enginapp.viewmodels.DashboardViewModel;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    AuthenticationService authenticationService;

    ImageView logout_btn;

    Button fetch_btn;

    TextView agentName_textview ;


    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
              R.drawable.team,
             /*  R.drawable.users,
               R.drawable.history*/
    };






    private DashboardViewModel mDashboardViewModel;
    private  String authToken ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        authenticationService = new AuthenticationService(Dashboard.this,AuthenticationService.DASHBOARD_ACTIVITY);

        authenticationService.authenticateWithRedirection(AuthenticationService.NO_ACTIVITY);

        authToken = authenticationService.getAuthToken();



        initViews();

        initDashboardViewModel();









    }

    private void initDashboardViewModel(){

        mDashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        mDashboardViewModel.init(authToken);
        mDashboardViewModel.getSignedInAgent().observe(this, new Observer<Agent>() {
            @Override
            public void onChanged(Agent agent) {
                agentName_textview.setText(agent.getAgent_nom());



            }

        });





    }

    private void initViews(){
        agentName_textview = findViewById(R.id.agentName_textview);
        logout_btn = findViewById(R.id.logout_btn);
        fetch_btn = findViewById(R.id.fetch_btn);

        //TabLayout
        viewPager =  findViewById(R.id.viewPager);
        tabLayout =  findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new TeamMembersList(authToken), getString(R.string.team_tab_title));
        adapter.addFragment(new MapsFragment(authToken), getString(R.string.team_tab_title));





        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);









        //the views onClickListeners


        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticationService.Logout();
            }

        });

        fetch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();


    }
}
