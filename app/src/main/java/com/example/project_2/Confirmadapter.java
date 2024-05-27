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
import com.example.project_2.Models.ConfirmplantBD;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Confirmadapter extends RecyclerView.Adapter<Confirmadapter.MyViweHolder>  {
    ArrayList<ConfirmplantBD> list;
    Context context;

    public Confirmadapter(ArrayList<ConfirmplantBD> list, Context context) {
        this.list = list;
        this.context=context;

    }

    @NonNull
    @Override
    public MyViweHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_item,parent,false);
        return new Confirmadapter.MyViweHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull MyViweHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.nameherbalist.setText(list.get(position).getAdded_by());
        holder.date.setText(list.get(position).getDate());
        Glide.with(context).load(list.get(position).getPlant_image()).apply(new RequestOptions().centerCrop().centerInside().placeholder(R.drawable.plant)).into(holder.img);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = context.getSharedPreferences("viewplant", MODE_PRIVATE).edit();
                editor.putString("name", list.get(position).getName());
                editor.putString("Symptoms", list.get(position).getSymptoms());
                editor.putString("description", list.get(position).getDescription());
                editor.putString("information", list.get(position).getInformation());
                editor.putString("date", list.get(position).getDate());
                editor.putString("plant_image",list.get(position).getPlant_image());
                editor.putString("used", list.get(position).getUsed());
                editor.putString("location",list.get(position).getLocation());
                editor.putString("id",list.get(position).getId());

                editor.apply();
                Intent intent = new Intent(context, ViewConfirm.class);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViweHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name,nameherbalist,date;
        RelativeLayout relativeLayout;

        public MyViweHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img_confirm);
            name=itemView.findViewById(R.id.plant_name);
            nameherbalist=itemView.findViewById(R.id.nameherbalist);
            date=itemView.findViewById(R.id.date);
            relativeLayout=itemView.findViewById(R.id.crelat);

        }
    }
}
