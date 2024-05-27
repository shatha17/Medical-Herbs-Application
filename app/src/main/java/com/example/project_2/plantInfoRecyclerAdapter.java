package com.example.project_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_2.Models.plantInfoModel;

import java.util.List;

public class plantInfoRecyclerAdapter extends RecyclerView.Adapter<plantInfoRecyclerAdapter.plantInfoViewHolder> {
    Context context;
    List<plantInfoModel> modelList;

    public plantInfoRecyclerAdapter(Context context, List<plantInfoModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public plantInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plan_info_list, parent, false);

        return new plantInfoViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull plantInfoViewHolder holder, int position) {
        holder.desc.setText(modelList.get(position).getDescription());
        holder.plant.setBackgroundResource(modelList.get(position).getImgURL());


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class plantInfoViewHolder extends RecyclerView.ViewHolder {
        TextView desc;
        ImageView plant;
        public plantInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            desc=itemView.findViewById(R.id.plantInfo);
            plant=itemView.findViewById(R.id.plant_info_img);
        }
    }
}
