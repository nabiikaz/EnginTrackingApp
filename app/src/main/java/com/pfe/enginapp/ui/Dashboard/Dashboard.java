package com.pfe.enginapp.ui.Dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.app.Notification;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.tabs.TabLayout;
import com.pfe.enginapp.App;
import com.pfe.enginapp.R;
import com.pfe.enginapp.adapters.TabAdapter;
import com.pfe.enginapp.adapters.TeamMemebersRecyclerViewAdapter;
import com.pfe.enginapp.models.Agent;
import com.pfe.enginapp.models.Team;
import com.pfe.enginapp.repositories.SocketRepository;
import com.pfe.enginapp.services.AuthenticationService;
import com.pfe.enginapp.viewmodels.DashboardViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {
    private static final String TAG = "Dashboard";

    AuthenticationService authenticationService;

    ImageView logout_btn;

    Button fetch_btn;

    TextView agentName_textview ;

    Agent signedInAgent;

    TeamMembersList teamMembersList;
    MapsFragment mapsFragment;


    private TabAdapter adapter;
        private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
              R.drawable.team,
            R.drawable.siren,

            /*  R.drawable.users,
              R.drawable.history*/
    };


    Socket socket ;

    private NotificationManagerCompat notificationManager;








    private DashboardViewModel mDashboardViewModel;
    private  String authToken ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        authenticationService = new AuthenticationService(Dashboard.this,AuthenticationService.DASHBOARD_ACTIVITY);

        authenticationService.authenticateWithRedirection(AuthenticationService.NO_ACTIVITY);

        authToken = authenticationService.getAuthToken();

        notificationManager = NotificationManagerCompat.from(this);





        initViews();

        initDashboardViewModel();













    }

    private void initDashboardViewModel(){

        mDashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        mDashboardViewModel.init(authToken);
        mDashboardViewModel.getSignedInAgent().observe(this, new Observer<Agent>() {
            @Override
            public void onChanged(Agent agent) {
                agentName_textview.setText(agent.getAgent_nom());

                if(signedInAgent != null)
                    return;

                signedInAgent = agent;

                //init the socketRepository

                socket = SocketRepository.getInstance(authToken,signedInAgent).getSocket();



                socket.on("interventionStart", new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {



                        if(teamMembersList == null)
                            return;

                        String team_id = (String)args[0];
                        String id_intervention = (String)args[0];

                        if(teamMembersList.belongsToTeam(team_id)){



                            sendInterventionNotification(id_intervention);

                        }




                    }
                });







            }

        });





    }


    private void initViews(){
        agentName_textview = findViewById(R.id.agentName_textview);
        logout_btn = findViewById(R.id.logout_btn);
        fetch_btn = findViewById(R.id.fetch_btn);

        //TabLayout
        viewPager =  findViewById(R.id.viewPager);
        tabLayout =  findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());

        teamMembersList =new TeamMembersList(authToken,notificationManager) ;
        mapsFragment =new MapsFragment(authToken) ;
        adapter.addFragment(teamMembersList, getString(R.string.team_tab_title));
        adapter.addFragment(mapsFragment, getString(R.string.intervention_tab_title));





        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);

        tabLayout.getTabAt(1).setIcon(tabIcons[1]);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() !=1)
                    return;

                Team mTeam = teamMembersList.getmTeam();
                if(mTeam == null)
                    return;

                String id_team = mTeam.get_id();

                mapsFragment.updateTeamId(id_team);



            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });









        //the views onClickListeners


        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticationService.Logout();
            }

        });

        fetch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }



    public void sendInterventionNotification(String id){

        String title = " Intervention:"+id;
        String message = "Vous êtes ici requis dans une intervention , veuillez vous rendre à votre Engin désigné";
        /*Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);
        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/

        Uri soundUri = Uri.parse(
                "android.resource://" +
                        getApplicationContext().getPackageName() +
                        "/" +
                        R.raw.siren);


        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.logo)



                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setColor(Color.BLUE)
                //.setContentIntent(contentIntent)
                //.setAutoCancel(true)
                //.setOnlyAlertOnce(true)
                //.addAction(R.mipmap.ic_launcher, "Toast", actionIntent)

                .build();
        notification.flags |= Notification.FLAG_INSISTENT;
        //notification.flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(1, notification);

    }




    @Override
    protected void onStart() {
        super.onStart();


    }
}
