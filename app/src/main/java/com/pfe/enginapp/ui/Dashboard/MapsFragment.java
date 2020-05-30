package com.pfe.enginapp.ui.Dashboard;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pfe.enginapp.LocationManager;
import com.pfe.enginapp.R;
import com.pfe.enginapp.models.Path;
import com.pfe.enginapp.models.SnappedPoints;
import com.pfe.enginapp.viewmodels.MapsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MapsFragment";

    private MapsViewModel mViewModel;
    private LocationManager locationManager;

    private Context context;

    private Boolean receiveLocationUpdates = true;

    private GoogleMap mMap;

    private final static int AMBULANCE_RESOURCE_ID = R.drawable.ambulance;

    private final static float DEFAULT_CAMERA_ZOOM = 18;

    //markers

    private Marker enginMaker;
    private Polyline routePolyline;


    private String authToken;
    MapsViewModel mMapsViewModel;

    public MapsFragment(String authToken){
        this.authToken = authToken;

    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.maps_fragment, container, false);


        //get the context in which this fragment is associated with.

        this.context = getContext();



        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MapsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {





        mMap = googleMap;


        //mMap.setMyLocationEnabled(true);


        locationManager = new LocationManager(context,new LocationCallBackHandler(MapsFragment.this));

        locationManager.startLocationUpdates();

        routePolyline = mMap.addPolyline(new PolylineOptions()
                .color(R.color.colorAccent)
                .clickable(true));


        initDashboardViewModel();



       /* LatLngBounds ADELAIDE = new LatLngBounds(new LatLng(33.050738, 8.118182),
                new LatLng(35.056790, -2.319584));

        mMap.setLatLngBoundsForCameraTarget(ADELAIDE);*/


    }

    private void initDashboardViewModel(){

        mMapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);

        mMapsViewModel.init(this.authToken);

        mMapsViewModel.getSnappedPoints().observe(getViewLifecycleOwner(), new Observer<List<SnappedPoints.SnappedPoint>>() {
            @Override
            public void onChanged(List<SnappedPoints.SnappedPoint> snappedPoints) {
                LatLng lastLocation = snappedPoints.get(snappedPoints.size()-1).getLocation().getLatLng();
                enginMaker.setPosition(lastLocation);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(lastLocation, DEFAULT_CAMERA_ZOOM);
                mMap.animateCamera(cameraUpdate);


                //convert the list of Location into a list of LatLng
                /*<LatLng> points = new ArrayList<>();
                for (SnappedPoints.SnappedPoint snappedPoint : snappedPoints) {
                    points.add(snappedPoint.getLocation().getLatLng());
                }

                routePolyline.setPoints(points);*/




            }
        });

    }






    private Marker initMarker(LatLng position,String title,int resourceId){


        MarkerOptions markerOptions = new MarkerOptions();

        if(position != null)
            markerOptions.position(position);
        if(title != null)
            markerOptions.title(title);
        if(resourceId > 0) {

            //create the icon of the marker
            int height = 30;
            int width = 30;

            BitmapDrawable bitmapDrawableRaw = (BitmapDrawable)getResources().getDrawable(resourceId);
            Bitmap b = bitmapDrawableRaw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


            BitmapDescriptor bitmapDescriptor =  BitmapDescriptorFactory.fromBitmap(smallMarker);

            markerOptions.icon(bitmapDescriptor);


            ////////////////////////////////////

        }





        return mMap.addMarker(markerOptions);

    }


    private static class LocationCallBackHandler extends LocationCallback {

        private int counter = 0;
        private MapsFragment mapsFragmentContext;

        LocationCallBackHandler(MapsFragment mapsFragmentContext){

            this.mapsFragmentContext = mapsFragmentContext;




        }


        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }


            counter +=locationResult.getLocations().size();

            Toast.makeText(mapsFragmentContext.getContext(),"counter :"+counter,Toast.LENGTH_LONG).show();

            for (Location location : locationResult.getLocations()) {








                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

                mapsFragmentContext.mMapsViewModel.addRawPosition(latLng);





                //this get executed only one time
                if(mapsFragmentContext.enginMaker == null){
                    mapsFragmentContext.enginMaker = mapsFragmentContext.initMarker(latLng,"Engin",AMBULANCE_RESOURCE_ID);

                }













            }
        }
    };

}
