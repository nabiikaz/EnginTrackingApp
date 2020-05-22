package com.pfe.enginapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pfe.enginapp.R;
import com.pfe.enginapp.models.Agent;
import com.pfe.enginapp.services.AuthenticationService;
import com.pfe.enginapp.viewmodels.DashboardViewModel;

public class Dashboard extends AppCompatActivity {

    AuthenticationService authenticationService;

    ImageView logout_btn;

    Button fetch_btn;

    TextView agentName_textview ;

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

        mDashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
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
