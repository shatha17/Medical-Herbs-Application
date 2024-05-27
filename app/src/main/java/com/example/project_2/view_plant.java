package com.example.project_2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.project_2.Models.AccountDB;
import com.example.project_2.Models.Admin_Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class view_plant extends AppCompatActivity {

    TextView name,symptom,description,information,parts,location;
    Button update,delete;
    DatabaseReference reference ,ref;
    FirebaseUser user;
    String userID;
    String type_account;
    ImageView back ,image_plant;
    String message;
    Admin_Account admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plant);
        Intent intent = getIntent();
         message = intent.getStringExtra("Gallery");
         if(message==null){
             message="view";
         }

        name=findViewById(R.id.name);
        symptom=findViewById(R.id.symptom);
        description=findViewById(R.id.description);
        information=findViewById(R.id.information);
        update=findViewById(R.id.update);
        delete=findViewById(R.id.delete);
        parts=findViewById(R.id.parts);
        location=findViewById(R.id.location);
        image_plant=findViewById(R.id.image_plant);
        update.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);
        SharedPreferences prefs = getSharedPreferences("viewplant", MODE_PRIVATE);
        String name1 = prefs.getString("name", "No name defined");
        String Symptoms = prefs.getString("Symptoms", "No name defined");
        String description1 = prefs.getString("description", "No name defined");
        String information1 = prefs.getString("information", "No name defined");
        String used1=prefs.getString("used", "No name defined");
        String image_plant1=prefs.getString("plant_image", "No name defined");
        String location1=prefs.getString("location", "No name defined");
        System.out.println(image_plant1);


        //Bundle bundle = getIntent().getExtras();
        name.setText(name1);
        symptom.setText(Symptoms);
        description.setText(description1);
        information.setText(information1);
        parts.setText(used1);
        location.setText(location1);
        Glide.with(view_plant.this).load(image_plant1).apply(new RequestOptions().centerCrop().centerInside().placeholder(R.drawable.plant)).into(image_plant);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(message.equals("Gallery"))
                {
                    startActivity(new Intent(view_plant.this, gallery_recycler.class));

                }
                else
                startActivity(new Intent(view_plant.this, search.class));

            }
        });


        user= FirebaseAuth.getInstance().getCurrentUser();
        userID=user.getUid();
        reference= FirebaseDatabase.getInstance().getReference("users");
        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                AccountDB userProfile=snapshot.getValue(AccountDB.class);
                type_account =userProfile.getAccountType();
                if(type_account.equals("Admin Account")) {
                    update.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    admin=new Admin_Account();
                    if(message.equals("Gallery")){
                        update.setVisibility(View.INVISIBLE);
                        delete.setVisibility(View.INVISIBLE);
                    }
                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mainIntent = new Intent(view_plant.this, update_plant.class);
                            view_plant.this.startActivity(mainIntent);
                        }
                    });

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(view_plant.this);
                            builder1.setMessage(getString(R.string.delete_plant));
                            builder1.setCancelable(false);

                            builder1.setPositiveButton(
                                    getString(R.string.yes),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            admin.deleteplant(name1);
                                            Toast.makeText(getApplicationContext(),getString(R.string.Plant_deleted),Toast.LENGTH_SHORT).show();
                                            Intent mainIntent = new Intent(view_plant.this, search.class);
                                            view_plant.this.startActivity(mainIntent);

                                        }
                                    });

                            builder1.setNegativeButton(
                                    getString(R.string.no),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}