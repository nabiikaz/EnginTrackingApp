package com.pfe.enginapp;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LocationManager {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean requestingLocationUpdates;

    //the context of the activity that created this LocationManager Instance
    private Context context;

    public LocationManager(Context context,LocationCallback locationCallback){

        this.context = context;


        createLocationRequest();


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        this.locationCallback = locationCallback;



    }




    private boolean checkPlayServices() {
        /**GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                //finish();
            }

            return false;
        }
        */
        return true;
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    public void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    private void resumeRequestingLocationUpdates(){
        if(requestingLocationUpdates){
            startLocationUpdates();
        }else{
            stopLocationUpdates();
        }
    }


    public Location getLastLocation(){
        final Location[] loc = {null};

        /*fusedLocationClient.getLastLocation()
                .addOnSuccessListener(context, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Toast.makeText(context,"Latitude"+ location.getLatitude()+" , Longitude"+location.getLongitude(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    Location location = task.getResult();
                    if(location != null){

                        Toast.makeText(context,"Latitude"+ location.getLatitude()+" , Longitude"+location.getLongitude(),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    try {
                        throw task.getException();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            }


        });


        return  loc[0];
    }

    
}
