package com.pfe.enginapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;

public class Permission {

    private Activity context;

    //list of permissions
    private ArrayList<String> permissionsToRequest;

    //list of rejected permissions
    private ArrayList<String> permissionsRejected = new ArrayList<>();

    //list of permissions needed
    private ArrayList<String> neededPermissions = new ArrayList<>();

    ArrayList<String> grantedPermissions = new ArrayList<>();
    ArrayList<String> deniedPermissions = new ArrayList<>();



    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;



    public Permission(Activity context){

        this.context = context;

        initNeededPermissions();



        permissionsToRequest();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (deniedPermissions.size() > 0) {
                context.requestPermissions(deniedPermissions.toArray(
                        new String[deniedPermissions.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

    }

    private void initNeededPermissions() {

        //permissions needed for location
        neededPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        neededPermissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }


        return true;
    }


    /**
     *
     *
     * @return a list of non available permissions for this application
     * */
    private void permissionsToRequest() {
        grantedPermissions = new ArrayList<>();
        deniedPermissions = new ArrayList<>();

        for (String permission : neededPermissions) {
            if (hasPermission(permission)) {
                grantedPermissions.add(permission);
            }else{
                deniedPermissions.add(permission);

            }
        }


    }
}
