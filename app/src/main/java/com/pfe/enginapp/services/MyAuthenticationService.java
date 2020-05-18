package com.pfe.enginapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.pfe.enginapp.Authenticator;

public class MyAuthenticationService extends Service {

    private static final Object lock = new Object();
    private Authenticator mAuthenticator;

    public MyAuthenticationService() {
        Log.d("addAccount","Rani Hna1");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("addAccount","Rani Hna");

        synchronized (lock) {
            if (mAuthenticator == null) {
                mAuthenticator = Authenticator.getInstance(this);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mAuthenticator.getIBinder();
    }

}