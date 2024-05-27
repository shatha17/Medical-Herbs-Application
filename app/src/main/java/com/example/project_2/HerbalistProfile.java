package com.example.project_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.project_2.Models.AccountDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class HerbalistProfile extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    CardView logout,setting;
    Button addplant;

    CircleImageView myimage;
    TextView user_name,profile_status;


    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herbalist_profile);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent1 = new Intent(HerbalistProfile.this, home.class);
                        startActivity(intent1);
                        break;
                    case R.id.message:
                        Intent intent2 = new Intent(HerbalistProfile.this, herbalist.class);
                        startActivity(intent2);
                        break;
                    case R.id.search:
                        startActivity(new Intent(HerbalistProfile.this, search.class));
                        break;
                }
                return true;
            }
        });

        logout=findViewById(R.id.LogOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HerbalistProfile.this, sign_in.class));
            }
        });

        setting=findViewById(R.id.settings);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HerbalistProfile.this, settings.class));
            }
        });

        addplant=findViewById(R.id.add_plant);
        addplant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(HerbalistProfile.this, add_plant.class);
                startActivity(intent1);
            }
        });

        myimage=findViewById(R.id.visit_profile_image);
        user_name=findViewById(R.id.visit_user_name);
        profile_status=findViewById(R.id.visit_profile_status);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        reference= FirebaseDatabase.getInstance().getReference("users");

        reference.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AccountDB userProfile=snapshot.getValue(AccountDB.class);
                if (userProfile !=null){
                    user_name.setText(userProfile.getUsername());
                    profile_status.setText(userProfile.getEmail());
                    Picasso.get().load(userProfile.getProfile_image()).placeholder(R.drawable.user).into(myimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HerbalistProfile.this, getString(R.string.Try_Again),Toast.LENGTH_LONG).show();
            }
        });

    }
}