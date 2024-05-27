package com.example.project_2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

public class user_account extends AppCompatActivity implements View.OnClickListener {
    BottomNavigationView bottomNavigationView;
    CardView logout,setting;

    CircleImageView myimage;
    TextView user_name,profile_status;


    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String currentUserID;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        myimage=findViewById(R.id.visit_profile_image);
        user_name=findViewById(R.id.visit_user_name);
        profile_status=findViewById(R.id.visit_profile_status);

        setting=findViewById(R.id.settings);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setting.setOnClickListener(this);
        logout=findViewById(R.id.LogOut);
        logout.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        reference= FirebaseDatabase.getInstance().getReference("users");

        reference.child(currentUserID).addValueEventListener(new ValueEventListener() {
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

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent1 = new Intent(user_account.this, home.class);
                        startActivity(intent1);
                        break;
                    case R.id.message:
                        Intent intent2 = new Intent(user_account.this, herbalist.class);
                        startActivity(intent2);
                        break;
                    case R.id.search:
                        startActivity(new Intent(user_account.this, search.class));
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.LogOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(user_account.this, sign_in.class));

                break;
            case R.id.settings:
                startActivity(new Intent(user_account.this, settings.class));

                break;


        }
    }
}