package com.pfe.enginapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.material.textfield.TextInputLayout;
import com.pfe.enginapp.R;
import com.pfe.enginapp.models.Agent;

import com.pfe.enginapp.services.AuthenticationService;
import com.pfe.enginapp.viewmodels.LoginViewModel;


public class Login extends AppCompatActivity  {

    
    public final static int AUTH_INVALID_CREDENTIALS = 1;

    public final static int AUTH_INVALID_TOKEN = 2;




    private TextInputLayout username_layout,password_layout;
    private TextView login_label;
    private Button login_btn;
    private LoginViewModel loginViewModel;
    private ProgressDialog progressDoalog;
    private String PARAM_USER_PASS;
    
    private String authToken = "";
    private String TAG = "Login";

    private AuthenticationService authenticationService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        authenticationService = new AuthenticationService(this,AuthenticationService.LOGIN_ACTIVITY);



        initViews();






    }


    @Override
    protected void onStop() {
        super.onStop();

        finish();
    }

    private void initViews(){

        username_layout = findViewById(R.id.username_layout);
        password_layout = findViewById(R.id.password_layout);
        login_label = findViewById(R.id.login_label);
        login_btn = findViewById(R.id.login_btn);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = username_layout.getEditText().getText().toString();
                String password = password_layout.getEditText().getText().toString();


                authenticationService.Login(username,password);

            }
        });



    }



    @Override
    protected void onStart() {
        super.onStart();

        //authenticationService.authenticateWithRedirection(AuthenticationService.DASHBOARD_ACTIVITY);


    }



    public void showAuthenticationErrors(int AUTH_FAILED_RESPONSE) {


        switch (AUTH_FAILED_RESPONSE){

            case AUTH_INVALID_CREDENTIALS :

                username_layout.setError(" ");
                username_layout.setEnabled(true);
                password_layout.setError(" ");
                login_label.setText(R.string.CREDENTIALS_INVALID);
                break;
            case AUTH_INVALID_TOKEN:
                username_layout.setEnabled(true);
                login_label.setText(R.string.SESSION_EXPIRED);
                break;
            default:
                login_label.setText(R.string.AUTH_FAILED_UNKNOWN);

        }

    }
}
