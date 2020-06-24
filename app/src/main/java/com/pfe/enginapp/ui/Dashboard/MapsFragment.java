package com.pfe.enginapp.ui.Dashboard;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.pfe.enginapp.LocationManager;
import com.pfe.enginapp.R;
import com.pfe.enginapp.adapters.HospitalsRecyclerViewAdapter;
import com.pfe.enginapp.models.Hospital;
import com.pfe.enginapp.models.Intervention;
import com.pfe.enginapp.models.SnappedPoints;
import com.pfe.enginapp.viewmodels.MapsViewModel;

import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback,HospitalsRecyclerViewAdapter.OnHospitalListener {
    private static final String TAG = "MapsFragment";

    private HospitalsRecyclerViewAdapter adapter;
    private RecyclerView hospitalRecyclerView;

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

    TextView call_time,start_time,arrival_time,transfer_time;

    ImageView ambulance_btn, accident_btn, hospital_btn;
    Button intervention_btn;
    ProgressBar progressBar;

    MaterialCardView hospitalsCard,interventionCard;

    private String id_team;



    private Boolean intervention_btn_active = true;

    public void setIntervention_btn_active(Boolean intervention_btn_active) {
        this.intervention_btn_active = intervention_btn_active;
    }


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


        mMapsViewModel.fetchIntervention(id_team).observe(getViewLifecycleOwner(), new Observer<Intervention>() {
            @Override
            public void onChanged(Intervention intervention) {








                if(intervention == null){
                    interventionCard.setVisibility(View.GONE);
                    hospitalsCard.setVisibility(View.GONE);

                    return;

                }




                hospitalsCard.setVisibility(View.GONE);
                interventionCard.setVisibility(View.VISIBLE);



                address_rue.setText(intervention.getAdresse().getAdresse_rue());
                phone.setText(intervention.getNumTel());


                accidentMarker = initMarker(intervention.getAdresse().getGps_coordonnee().getLatLng(),"Accident",ACCIDENT_RESOURCE_ID);


                //fetch the list of the hospitals in accordance with the distance to this intervention incident
                mMapsViewModel.getHospitals(accidentMarker.getPosition());



                String status = intervention.getStatut();
                switch (status) {
                    case "recu":
                        if(hospitalMarker != null){
                            hospitalMarker.setVisible(true);
                        }
                        if(accidentMarker != null){
                            accidentMarker.setVisible(true);

                        }

                        intervention_btn.setText("commencer l'intervention");
                        intervention_btn.setBackgroundResource(R.color.recu);

                        call_time.setText("Heure de l'appel :"+intervention.getDateTimeAppel());

                        break;

                    case "depart":

                        intervention_btn.setText("Arrivé à destination");
                        intervention_btn.setBackgroundResource(R.color.blue);

                        start_time.setText("Heure de depart :"+intervention.getDateTimeDepart());
                        break;

                    case "en_cours":
                        intervention_btn.setText("transfert vers Hopital");
                        intervention_btn.setBackgroundResource(R.color.colorPrimary);

                        arrival_time.setText("Heure d'arrivée   :"+intervention.getDateTimeArrive());
                        break;

                    case "transfere":

                        intervention_btn.setText("Terminer L'intervention");
                        intervention_btn.setBackgroundResource(R.color.red);

                        transfer_time.setText("Heure de transfer   :"+intervention.getTransfere().getDateTimeDepart());

                        break;

                    case "termine":
                        mMapsViewModel.clearIntervention();
                        hospitalsCard.setVisibility(View.GONE);
                        interventionCard.setVisibility(View.GONE);
                        if(hospitalMarker != null){
                            hospitalMarker.setVisible(false);
                        }
                        if(accidentMarker != null){
                            accidentMarker.setVisible(false);


                        }



                        /*intervention_btn.setText("Cette intervention est terminée");
                        intervention_btn.setBackgroundResource(R.color.recu);
                        intervention_btn.setClickable(false);*/
                        break;
                }

                progressBar.setVisibility(View.INVISIBLE);







            }
        });


        mMapsViewModel.getHospitals(null).observe(getViewLifecycleOwner(), new Observer<List<Hospital>>() {
            @Override
            public void onChanged(List<Hospital> hospitals) {
                Log.d(TAG, "getHospitals > onChanged: "+hospitals.get(1).getName());
                adapter.setmHospitals(hospitals);
                adapter.notifyDataSetChanged();

            }
        });

    }

    private void updateCamera(LatLng location){

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, DEFAULT_CAMERA_ZOOM);
        mMap.animateCamera(cameraUpdate);

    }



    private void initViews(final View view){

        address_rue = view.findViewById(R.id.address_rue);
        phone = view.findViewById(R.id.phone);
        progressBar = view.findViewById(R.id.progressBar);


        call_time = view.findViewById(R.id.call_time);
        start_time = view.findViewById(R.id.start_time);
        arrival_time = view.findViewById(R.id.arrival_time);
        transfer_time = view.findViewById(R.id.transfer_time);

        hospitalsCard = view.findViewById(R.id.hospitalsCard);
        interventionCard = view.findViewById(R.id.interventionCard);




        ambulance_btn = view.findViewById(R.id.ambulance_btn);
        accident_btn = view.findViewById(R.id.incident_btn);
        hospital_btn = view.findViewById(R.id.hospital_btn);
        intervention_btn  = view.findViewById(R.id.intervention_btn);


        hospitalRecyclerView = view.findViewById(R.id.hospitalsRecyclerView);

        initRecyclerView();




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
                if(mMapsViewModel.getIntervention() == null){
                    Toast.makeText(getContext(), "Une intervention est nécessaire pour cette fonctionnalité.", Toast.LENGTH_SHORT).show();
                    return;

                }
                switch (hospitalsCard.getVisibility()) {
                    case View.VISIBLE:
                        hospitalsCard.setVisibility(View.GONE);
                        interventionCard.setVisibility(View.VISIBLE);

                        break;
                    case View.GONE:
                        interventionCard.setVisibility(View.GONE);
                        hospitalsCard.setVisibility(View.VISIBLE);


                        break;
                }

                if (hospitalMarker == null) {
                    Toast.makeText(getContext(), "Coordonnées GPS non disponibles", Toast.LENGTH_SHORT).show();

                }else{
                    updateCamera(hospitalMarker.getPosition());

                    referenceLocation = hospitalMarker.getPosition();

                }





            }
        });
        intervention_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intervention currentIntervention = mMapsViewModel.fetchIntervention(id_team).getValue();

                if (currentIntervention == null) {
                    return;
                }

                String status = currentIntervention.getStatut();

                Log.d(TAG, "intervention_btn > setOnClickListener> currentIntervention : status "+currentIntervention.getStatut());
                String newStatus ="";
                String dialogMessage = "";

                switch (status){
                    case "recu" :
                        newStatus = "depart";
                        dialogMessage = "Vous êtes sur le point de commencer cette intervention.";

                        break;

                    case "depart" :
                        newStatus = "en_cours";
                        dialogMessage = "Arrivé sur le lieu de l'intervention!";


                        break;

                    case "en_cours" :
                        newStatus = "transfere";
                        dialogMessage = "Transfert à l'hôpital le plus proche.";

                        break;

                    case "transfere" :
                        newStatus = "termine";
                        dialogMessage = "L'intervention est terminée!";


                        break;

                    case "termine" :
                        newStatus = "termine";
                        dialogMessage = "L'intervention est terminée!";

                        intervention_btn.setClickable(false);

                        break;
                }
                //mMapsViewModel.updateIntervention(currentIntervention);

                confirmInterventionStatusChange(newStatus,dialogMessage);





            }
        });


    }


    private void initRecyclerView(){
       adapter = new HospitalsRecyclerViewAdapter(this.getContext(),this);



        hospitalRecyclerView.setAdapter(adapter);



        hospitalRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

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

        mMapsViewModel.fetchIntervention(id_team);


    }

    public void confirmInterventionStatusChange(final String status,final String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.bilan_dialog_layout, (ViewGroup) getView(), false);
        final TextInputEditText bilan = viewInflated.findViewById(R.id.bilan);

        builder.setTitle("Changement de statut d'intervention")
                .setMessage(message)
                .setNeutralButton("Confirmer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String bilanStr = "";
                        if(status == "termine"){
                            bilanStr = bilan.getText().toString();
                        }

                        updateInterventionStatus(status,bilanStr);


                    }

                });

        if(status == "termine"){


            builder.setView(viewInflated);

        }








        builder.create().show();


    }

    private void updateInterventionStatus(String status,String bilanStr){
        if(status == "")
            return;
        Intervention intervention = mMapsViewModel.fetchIntervention(id_team).getValue();
        intervention.setStatut(status);
        switch (status){
            case "recu" :

                break;

            case "depart" :
                intervention.setDateTimeDepart("now()");

                break;

            case "en_cours" :
                intervention.setDateTimeArrive("now()");


                break;

            case "transfere" :

                intervention.getTransfere().setDateTimeDepart("now()");



                break;

            case "termine" :
                intervention.setDateTimeFin("now()");
                intervention.setBilan(bilanStr);


                break;
        }

        mMapsViewModel.updateIntervention(intervention);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnHospitalListener(int position) {
        Hospital selectedHospital = adapter.getmHospitals().get(position);
        LatLng location = selectedHospital.getGps_coordonnee().getLatLng();
        String title = selectedHospital.getName();
        if(hospitalMarker == null){
            hospitalMarker = initMarker(location,title,HOSPITAL_RESOURCE_ID);
        }else{
            hospitalMarker.setPosition(location);
            hospitalMarker.setTitle(title);
        }

        updateCamera(location);

        hospitalsCard.setVisibility(View.GONE);
        interventionCard.setVisibility(View.VISIBLE);

        mMapsViewModel.updateHospitalTransfer(selectedHospital.get_id());



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
