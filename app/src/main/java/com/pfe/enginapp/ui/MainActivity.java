package com.pfe.enginapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.pfe.enginapp.R;
import com.pfe.enginapp.services.AuthenticationService;

public class MainActivity extends AppCompatActivity {

    private TextView textView ;
    private AuthenticationService authenticationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textview);

        /*Intent intent = new Intent(this,Login.class);
        startActivity(intent);
        finish();*/

        authenticationService  = new AuthenticationService(MainActivity.this,AuthenticationService.MAIN_ACTIVITY);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //do the transitiona the animations

        authenticationService.authenticateWithRedirection(authenticationService.DASHBOARD_ACTIVITY);





    }

    @Override
    protected void onStop() {
        super.onStop();

        MainActivity.this.finish();
    }
}
