package com.pfe.enginapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    }

    @Override
    public int getItemCount() {
        return teamAgents.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView agent_name_textview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            agent_name_textview = itemView.findViewById(R.id.agent_name);

        }
    }

    public void setTeamAgents(ArrayList<Team.Agent> teamAgents) {
        this.teamAgents = teamAgents;
    }

}
