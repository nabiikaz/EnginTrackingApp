package com.pfe.enginapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pfe.enginapp.R;
import com.pfe.enginapp.models.Hospital;
import com.pfe.enginapp.models.Team;

import java.util.ArrayList;
import java.util.List;

public class HospitalsRecyclerViewAdapter extends RecyclerView.Adapter<HospitalsRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "HospitalsRecyclerViewAdapter";


    OnHospitalListener onHospitalListener;


    private List<Hospital> mHospitals;
    private Context mContext;

    public HospitalsRecyclerViewAdapter(Context mContext,OnHospitalListener onHospitalListener) {
        this.mHospitals = new ArrayList<>() ;
        this.mContext = mContext;
        this.onHospitalListener = onHospitalListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from((parent.getContext())).inflate(R.layout.hospital_item_layout,parent,false);

        ViewHolder viewHolder = new ViewHolder(view,onHospitalListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Hospital hospital = mHospitals.get(position);

        holder.hospital_name.setText(hospital.getName());

        if(hospital.getDistance() != null){
            holder.distance.setText(hospital.getDistance().getText());
        }

        if(hospital.getDuration() != null){
            holder.duration.setText(hospital.getDuration().getText());
        }



    }

    @Override
    public int getItemCount() {
        return mHospitals.size();
    }

    public List<Hospital> getmHospitals() {
        return mHospitals;
    }
    public void setmHospitals(List<Hospital> mHospitals) {
        this.mHospitals = mHospitals;
    }





    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView hospital_name,distance,duration;
        OnHospitalListener onHospitalListener;
        int lastPosition;

        public ViewHolder(@NonNull View itemView , OnHospitalListener onHospitalListener) {
            super(itemView);

            hospital_name = itemView.findViewById(R.id.hospital_name);
            distance = itemView.findViewById(R.id.distance);
            duration = itemView.findViewById(R.id.duration);
            this.onHospitalListener = onHospitalListener;

            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            onHospitalListener.OnHospitalListener(position);


        }
    }

    public interface OnHospitalListener{
        void OnHospitalListener(int position);
    }



}
