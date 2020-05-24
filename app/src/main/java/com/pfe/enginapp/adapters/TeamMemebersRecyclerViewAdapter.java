package com.pfe.enginapp.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pfe.enginapp.R;
import com.pfe.enginapp.models.Team;

import java.util.ArrayList;

public class TeamMemebersRecyclerViewAdapter extends RecyclerView.Adapter<TeamMemebersRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "TeamMemebersRecyclerVie";



    private ArrayList<Team.Agent> teamAgents;
    private Context mContext;

    public TeamMemebersRecyclerViewAdapter(Context mContext) {
        this.teamAgents  = new ArrayList<>() ;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from((parent.getContext())).inflate(R.layout.team_memeber_item_layout,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.agent_name_textview.setText(teamAgents.get(position).getNom());

        //if(teamAgents.get(position).getType() == )
        switch (teamAgents.get(position).getType()){
            case Team.Agent.SECOURS_TYPE:
               /* Drawable icon = mContext.getDrawable(R.id.secours)
                holder.agent_type_ImageView.setImageDrawable(icon);*/
                holder.agent_type_TextView.setText("Secours");


                break;

            case Team.Agent.CHEF_TYPE:

                holder.agent_type_TextView.setText("Chef d'agres");

                break;

            case Team.Agent.CHAUFFEUR_TYPE:

                holder.agent_type_TextView.setText("Chauffeur");

                break;
        }

    }

    @Override
    public int getItemCount() {
        return teamAgents.size();
    }

    public void setTeamAgents(ArrayList<Team.Agent> teamAgents) {
        this.teamAgents = teamAgents;
    }





    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView agent_name_textview,agent_type_TextView;
        ImageView agent_type_ImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            agent_name_textview = itemView.findViewById(R.id.agent_name);
            agent_type_ImageView = itemView.findViewById(R.id.agent_type_icon);
            agent_type_TextView = itemView.findViewById(R.id.agent_type_text);

        }
    }



}
