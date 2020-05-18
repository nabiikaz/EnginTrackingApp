package com.pfe.enginapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.pfe.enginapp.R;
import com.pfe.enginapp.models.agent;
import com.pfe.enginapp.repositories.authenticationRepository;
import com.pfe.enginapp.viewmodels.LoginViewModel;

public class Login extends AppCompatActivity  {

    
    public static final String IS_ADDING_NEW_ACCOUNT = "IS_ADDING_NEW_ACCOUNT" ;
    public static final String ACCOUNT_TYPE = "com.pfe.enginapp.account";
    public final static String AUTH_TOKEN_TYPE = "LoginAuthType" ;


    private TextInputLayout username_layout,password_layout;
    private Button login_btn;
    private LoginViewModel loginViewModel;
    private ProgressDialog progressDoalog;
    private String PARAM_USER_PASS;
    
    private String authToken = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);





        initViews();

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginViewModel.init();

        loginViewModel.getAgent().observe(this, new Observer<agent>() {
            @Override
            public void onChanged(agent agent) {
                username_layout.getEditText().setText(agent.getNom());

            }
        });




    }

    private void initViews(){

        username_layout = findViewById(R.id.username_layout);
        password_layout = findViewById(R.id.password_layout);
        login_btn = findViewById(R.id.login_btn);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = username_layout.getEditText().getText().toString();
                String password = password_layout.getEditText().getText().toString();

                authenticationRepository.getInstance(Login.this).Login(username,password);

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();


    }



    public void authCompleted(Boolean auth_success) {
        if(auth_success){
            Intent intent = new Intent(Login.this,Dashboard.class);
            startActivity(intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Login.this.finish();
            }

        }else{
            username_layout.setError(" ");
            username_layout.setEnabled(true);
            password_layout.setError(" ");


        }

    }
}
