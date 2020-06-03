package com.pfe.enginapp.ui.Dashboard;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.pfe.enginapp.models.Intervention;
import com.pfe.enginapp.models.SnappedPoints;
import com.pfe.enginapp.viewmodels.MapsViewModel;

import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MapsFragment";

    private MapsViewModel mViewModel;
    private LocationManager locationManager;

    private Context context;

    private Boolean receiveLocationUpdates = true;

    private GoogleMap mMap;

    private final static int AMBULANCE_RESOURCE_ID = R.drawable.ambulance;
    private final static int ACCIDENT_RESOURCE_ID = R.drawable.accident1;
    private final static int HOSPITAL_RESOURCE_ID = R.drawable.hospital;

    private final static float DEFAULT_CAMERA_ZOOM = 18;

    //markers

    private Marker enginMaker;
    private Marker accidentMarker;
    private Marker hospitalMarker;

    private LatLng referenceLocation; // the position that the camera will zoom in and fellow

    private Polyline routePolyline;


    private String authToken;
    MapsViewModel mMapsViewModel;

    TextView address_rue,phone;

    ImageView ambulance_btn, accident_btn, hospital_btn;
    Button intervention_btn;
    private String id_team;

    Intervention currentIntervention;

    public MapsFragment(String authToken){
        this.authToken = authToken;

    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.maps_fragment, container, false);

        initViews(view);

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


        Toast.makeText(getContext(),"Map Ready",Toast.LENGTH_LONG).show();




        mMap = googleMap;


        //mMap.setMyLocationEnabled(true);


        locationManager = new LocationManager(context,new LocationCallBackHandler(MapsFragment.this));

        locationManager.startLocationUpdates();

        routePolyline = mMap.addPolyline(new PolylineOptions()
                .color(R.color.colorAccent)
                .clickable(true));


        initDashboardViewModel();






    }

    private void initDashboardViewModel(){

        mMapsViewModel = new ViewModelProvider(this).get(MapsViewModel.class);

        mMapsViewModel.init(this.authToken);

        mMapsViewModel.getSnappedPoints().observe(getViewLifecycleOwner(), new Observer<List<SnappedPoints.SnappedPoint>>() {
            @Override
            public void onChanged(List<SnappedPoints.SnappedPoint> snappedPoints) {

                if (snappedPoints == null )
                    return;
                if(snappedPoints.size() == 0)
                    return;

                LatLng lastLocation = snappedPoints.get(snappedPoints.size()-1).getLocation().getLatLng();

                referenceLocation = lastLocation;
                updateCamera(lastLocation);



                //this get executed only one time
                if(enginMaker == null){
                    enginMaker = initMarker(lastLocation,"Ambulance",AMBULANCE_RESOURCE_ID);


                }else{
                    enginMaker.setPosition(referenceLocation);
                }


                //convert the list of Location into a list of LatLng
                /*<LatLng> points = new ArrayList<>();
                for (SnappedPoints.SnappedPoint snappedPoint : snappedPoints) {
                    points.add(snappedPoint.getLocation().getLatLng());
                }

                routePolyline.setPoints(points);*/




            }
        });


        mMapsViewModel.getIntervention(id_team).observe(getViewLifecycleOwner(), new Observer<Intervention>() {
            @Override
            public void onChanged(Intervention intervention) {





                if(intervention == null)
                    return;

                currentIntervention = intervention;

                address_rue.setText(intervention.getAdresse().getAdresse_rue());
                phone.setText(intervention.getNumTel());


                accidentMarker = initMarker(intervention.getAdresse().getGps_coordonnee().getLatLng(),"Accident",ACCIDENT_RESOURCE_ID);

                if (intervention.getTransfere().getGps_coordonnee() == null) {
                    hospitalMarker = initMarker(intervention.getTransfere().getGps_coordonnee().getLatLng(),intervention.getTransfere().getLieu(),HOSPITAL_RESOURCE_ID);

                }

                String status = intervention.getStatut();
                switch (status) {
                    case "recu":
                        intervention_btn.setText("commencer l'intervention");
                        intervention_btn.setBackgroundResource(R.color.recu);

                        break;

                    case "depart":

                        intervention_btn.setText("Arrivé à destination");
                        intervention_btn.setBackgroundResource(R.color.blue);
                        break;

                    case "en_cours":
                        intervention_btn.setText("transfert vers Hopital");
                        intervention_btn.setBackgroundResource(R.color.colorPrimary);
                        break;

                    case "transfere":

                        intervention_btn.setText("transfert vers Hopital");
                        intervention_btn.setBackgroundResource(R.color.orange);

                        break;

                    case "termine":
                        intervention_btn.setText("Terminer L'intervention");
                        intervention_btn.setBackgroundResource(R.color.red);
                        break;
                }





            }
        });

    }

    private void updateCamera(LatLng location){

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, DEFAULT_CAMERA_ZOOM);
        mMap.animateCamera(cameraUpdate);

    }



    private void initViews(View view){

        address_rue = view.findViewById(R.id.address_rue);
        phone = view.findViewById(R.id.phone);


        ambulance_btn = view.findViewById(R.id.ambulance_btn);
        accident_btn = view.findViewById(R.id.incident_btn);
        hospital_btn = view.findViewById(R.id.hospital_btn);
        intervention_btn  = view.findViewById(R.id.intervention_btn);


        ambulance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enginMaker == null){

                    Toast.makeText(getContext(), "Coordonnées GPS non disponibles", Toast.LENGTH_SHORT).show();
                    return;

                }
                updateCamera(enginMaker.getPosition());

                referenceLocation = enginMaker.getPosition();

            }
        });

        accident_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accidentMarker == null){
                    Toast.makeText(getContext(), "Coordonnées GPS non disponibles", Toast.LENGTH_SHORT).show();
                    return;
                }


                updateCamera(accidentMarker.getPosition());

                referenceLocation = accidentMarker.getPosition();

            }
        });

        hospital_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hospitalMarker == null) {
                    Toast.makeText(getContext(), "Coordonnées GPS non disponibles", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateCamera(hospitalMarker.getPosition());

                referenceLocation = hospitalMarker.getPosition();


            }
        });
        intervention_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIntervention == null) {
                    return;
                }

                String status = currentIntervention.getStatut();

                switch (status){
                    case "recu" :

                        currentIntervention.setStatut("depart");

                        break;

                    case "depart" :

                        currentIntervention.setStatut("en_cours");
                        break;

                    case "en_cours" :
                        currentIntervention.setStatut("transfere");
                        break;

                    case "transfere" :
                        currentIntervention.setStatut("termine");
                        break;

                    case "termine" :
                        break;
                }


                mMapsViewModel.updateIntervention(currentIntervention);
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


    public void updateTeamId(String id_team){
        this.id_team = id_team;

        mMapsViewModel.getIntervention(id_team);


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

//            Toast.makeText(mapsFragmentContext.getContext(),"counter :"+counter,Toast.LENGTH_LONG).show();

            for (Location location : locationResult.getLocations()) {








                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

                mapsFragmentContext.mMapsViewModel.addRawPosition(latLng);



















            }
        }
    };

}
