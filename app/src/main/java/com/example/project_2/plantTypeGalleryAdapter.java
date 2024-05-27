package com.example.project_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.project_2.Models.plantBD;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class plantTypeGalleryAdapter extends RecyclerView.Adapter<plantTypeGalleryAdapter.plantTypeGalleryViewHolder> {
        Context context;
        List<plantBD> modelList;

public plantTypeGalleryAdapter(Context context, List<plantBD> modelList) {
        this.context = context;
        this.modelList = modelList;
        }

@NonNull
@Override
public plantTypeGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plant_gallery_type, parent, false);

        return new plantTypeGalleryAdapter.plantTypeGalleryViewHolder(view );     }

@Override
public void onBindViewHolder(@NonNull plantTypeGalleryViewHolder holder, int position) {
       // holder.imgURL1.setBackgroundResource(modelList.get(position).getImgURL1());
    Glide.with(context).load(modelList.get(position).getPlant_image()).apply(new RequestOptions().centerCrop().centerInside().placeholder(R.drawable.plant)).into(holder.imgURL1);

    holder.imgURL1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences.Editor editor = context.getSharedPreferences("viewplant", MODE_PRIVATE).edit();
            editor.putString("name", modelList.get(position).getName());
            editor.putString("Symptoms", modelList.get(position).getSymptoms());
            editor.putString("description", modelList.get(position).getDescription());
            editor.putString("information", modelList.get(position).getInformation());
            editor.putString("plant_image",modelList.get(position).getPlant_image());
            editor.putString("used",modelList.get(position).getUsed());
            editor.putString("location",modelList.get(position).getLocation());

            editor.apply();
            Intent intent = new Intent(context, view_plant.class);
            intent.putExtra("Gallery", "Gallery");

            context.startActivity(intent);
        }
    });

}

@Override
public int getItemCount() {
        return modelList.size();
        }


public class plantTypeGalleryViewHolder extends RecyclerView.ViewHolder{
    ImageView imgURL1;
    public plantTypeGalleryViewHolder(@NonNull View itemView) {
        super(itemView);

        imgURL1=itemView.findViewById(R.id.image1);
    }
}
}
