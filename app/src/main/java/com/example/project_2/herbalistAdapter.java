package com.example.project_2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.project_2.Models.AccountDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class herbalistAdapter extends RecyclerView.Adapter<herbalistAdapter.herbalistViewHolder> {
    ArrayList<AccountDB> model;
    Context context;
    private DatabaseReference UsersRef;
    private final String accountType="Herbalist Account";



    public herbalistAdapter(ArrayList<AccountDB> model, Context context) {
        this.model = model;
        this.context = context;
        UsersRef = FirebaseDatabase.getInstance().getReference().child("users");

    }


    @NonNull
    @Override
    public herbalistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout, parent, false);
        return new herbalistViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull herbalistViewHolder holder, int position) {

        if(model.get(position).getAccountType().equals(accountType)){

            holder.userName.setText(model.get(position).getUsername());
            holder.userStatus.setText(model.get(position).getEmail());
            Glide.with(context).load(model.get(position).getProfile_image()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.user)).into(holder.profileImage);

            UsersRef.child(model.get(position).getId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("userState").hasChild("state"))
                    {
                        String state = snapshot.child("userState").child("state").getValue().toString();
                        String date = snapshot.child("userState").child("date").getValue().toString();
                        String time = snapshot.child("userState").child("time").getValue().toString();

                        if (state.equals("online"))
                        {
                            holder.onlineIcon.setVisibility(View.VISIBLE);
                        }
                        else if (state.equals("offline"))
                        {
                            holder.onlineIcon.setVisibility(View.INVISIBLE);
                        }
                    }
                    else
                    {
                        holder.onlineIcon.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent profileIntent = new Intent(context, ProfileActivity.class);
                profileIntent.putExtra("visit_user_id", model.get(position).getId());
                context.startActivity(profileIntent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public static class herbalistViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, userStatus;
        CircleImageView profileImage;
        ImageView onlineIcon;

        public herbalistViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            onlineIcon = (ImageView) itemView.findViewById(R.id.user_online_status);
        }
    }
}
