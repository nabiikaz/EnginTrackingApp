package com.pfe.enginapp.ui.Dashboard;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.pfe.enginapp.App;
import com.pfe.enginapp.R;
import com.pfe.enginapp.adapters.TeamMemebersRecyclerViewAdapter;
import com.pfe.enginapp.models.Agent;
import com.pfe.enginapp.models.Team;
import com.pfe.enginapp.receivers.NotificationReceiver;
import com.pfe.enginapp.repositories.SocketRepository;
import com.pfe.enginapp.ui.MainActivity;
import com.pfe.enginapp.viewmodels.DashboardViewModel;
import com.pfe.enginapp.viewmodels.TeamMembersListViewModel;

import java.util.ArrayList;

public class TeamMembersList extends Fragment {


    private static String authToken;
    private RecyclerView teamMembersRecyclerView;
    private  TeamMemebersRecyclerViewAdapter adapter;

    private NotificationManagerCompat notificationManager;



    private Team mTeam;


    Socket socket;





    private TeamMembersListViewModel mTeamMembersListViewModel;



    public TeamMembersList(String authToken,NotificationManagerCompat notificationManager){
        this.authToken = authToken;

        this.notificationManager = notificationManager;


    }


    private TeamMembersListViewModel mViewModel;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.team_members_list_fragment, container, false);

        //initializing the Views on this Fragment
        initViews(view);



       initDashboardViewModel();


        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TeamMembersListViewModel.class);
        // TODO: Use the ViewModel
    }



    public void initViews(View view){
        //recyclerView
        teamMembersRecyclerView = view.findViewById(R.id.teamMembersRecyclerView);
        initRecyclerView();
        /////////////////


    }



    private void initRecyclerView(){
        adapter = new TeamMemebersRecyclerViewAdapter(this.getContext());



        teamMembersRecyclerView.setAdapter(adapter);



        teamMembersRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }

    private void initDashboardViewModel(){

        mTeamMembersListViewModel = new ViewModelProvider(this).get(TeamMembersListViewModel.class);

        mTeamMembersListViewModel.init(authToken);
        mTeamMembersListViewModel.getTeam().observe(getViewLifecycleOwner(), new Observer<Team>() {
            @Override
            public void onChanged(final Team team) {

                if(team == null)
                    return;

                mTeam = team;



                /*socket = SocketRepository.getInstance().getSocket();

                socket.on("interventionStart", new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        //Toast.makeText(getContext(),"id_team:"+args[0],Toast.LENGTH_LONG).show();
                        Log.d("TeamMembersList", "call: "+team.get_id());
                        Log.d("TeamMembersList", "call1: "+(String) args[0]);

                        if(team.get_id().equals(((String) args[0])) ){
                            Log.d("TeamMembersList", "calle: "+team.get_id());

                            sendInterventionNotification();
                        }

                    }
                });*/



                adapter.setTeamAgents(team.getAgents());

                adapter.notifyDataSetChanged();




            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(socket != null)
            socket.disconnect();
    }

    public void sendOnChannel1(String message) {


        /*Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getPackageName() + "/raw/siren");

        String title = "title ";
        String message = "message body";
        /*Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);
        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);*/
        Notification notification = new NotificationCompat.Builder(getContext(), App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.logo)

                .setContentTitle("title")
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


    public Boolean belongsToTeam(String id_team){
        if(mTeam == null)
            return  false;


        return mTeam.get_id().equals(id_team);

    }

    public Boolean isChef(String agentId){
        return mTeam.getChefId().equals(agentId);
    }

    public Team getmTeam() {
        return mTeam;
    }

}
