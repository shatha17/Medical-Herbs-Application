package com.example.project_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.project_2.Models.plantBD;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class searchadapter extends RecyclerView.Adapter<searchadapter.MyViweHolder> {
    ArrayList<plantBD> list;
    Context context;

    public searchadapter(ArrayList<plantBD> list, Context context) {
        this.list = list;
        this.context=context;

    }


    @NonNull
    @Override
    public MyViweHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,parent,false);
        return new MyViweHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViweHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        Glide.with(context).load(list.get(position).getPlant_image()).apply(new RequestOptions().centerCrop().centerInside().placeholder(R.drawable.plant)).into(holder.img);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences("viewplant", MODE_PRIVATE).edit();
                editor.putString("name", list.get(position).getName());
                editor.putString("Symptoms", list.get(position).getSymptoms());
                editor.putString("description", list.get(position).getDescription());
                editor.putString("information", list.get(position).getInformation());
                editor.putString("plant_image",list.get(position).getPlant_image());
                editor.putString("used",list.get(position).getUsed());
                editor.putString("location",list.get(position).getLocation());
                editor.apply();
                Intent intent = new Intent(context, view_plant.class);

                context.startActivity(intent);
            }
        });    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViweHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name;
        RelativeLayout relativeLayout;

        public MyViweHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img1);
            name=itemView.findViewById(R.id.nametext);
            relativeLayout=itemView.findViewById(R.id.relat);

        }
    }
}
