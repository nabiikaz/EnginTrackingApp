package com.pfe.enginapp.ui;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.pfe.enginapp.R;
import com.pfe.enginapp.models.Auth;
import com.pfe.enginapp.repositories.AuthenticationService;

public class Dashboard extends AppCompatActivity {

    AuthenticationService authenticationService;

    ImageView logout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        authenticationService = new AuthenticationService(Dashboard.this,AuthenticationService.DASHBOARD_ACTIVITY);

        initViews();
    }

    private void initViews(){
        logout_btn = findViewById(R.id.logout_btn);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticationService.Logout();
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        authenticationService.authenticateWithRedirection(AuthenticationService.NO_ACTIVITY);
    }
}
