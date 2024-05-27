package com.example.project_2;

import android.annotation.SuppressLint;
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
import com.example.project_2.Models.Admin_Account;
import com.example.project_2.Models.Herbalist_Account;
import com.example.project_2.Models.ConfirmplantBD;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewConfirm extends AppCompatActivity implements View.OnClickListener{
    TextView name,symptom,description,info,date,parts,location;
    Button Accept,Reject;
    FirebaseDatabase root;
    FirebaseAuth mAuth ;
    DatabaseReference reference ,ref;
    String name1 ,Symptoms ,description1 ,information1,dat ,image_plant1,used,location1;
    String id,email;
    ImageView image_plant;

    private DatabaseReference UserRef;
    Admin_Account admin;
    Herbalist_Account herbalist;
    ConfirmplantBD confirmplant;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_confirm);
        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");

        name=findViewById(R.id.name);
        symptom=findViewById(R.id.symptom);
        description=findViewById(R.id.description);
        info=findViewById(R.id.information);
        date=findViewById(R.id.date);
        parts=findViewById(R.id.parts);
        location=findViewById(R.id.location);
        image_plant=findViewById(R.id.image_plant);
        SharedPreferences prefs = getSharedPreferences("viewplant", MODE_PRIVATE);
        name1 = prefs.getString("name", "No name defined");
        Symptoms = prefs.getString("Symptoms", "No Symptoms defined");
        description1 = prefs.getString("description", "No description defined");
        information1 = prefs.getString("information", "No information defined");
        image_plant1=prefs.getString("plant_image", "No name defined");
        dat = prefs.getString("date", "No name defined");
        used=prefs.getString("used", "No name defined");
        location1=prefs.getString("location", "No name defined");
        id=prefs.getString("id", "No name defined");
        UserRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                herbalist=dataSnapshot.getValue(Herbalist_Account.class);
                email=herbalist.getEmail();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        name.setText(name1);
        symptom.setText(Symptoms);
        description.setText(description1);
        info.setText(information1);
        date.setText(dat);
        parts.setText(used);
        location.setText(location1);
        Glide.with(ViewConfirm.this).load(image_plant1).apply(new RequestOptions().centerCrop().centerInside().placeholder(R.drawable.plant)).into(image_plant);

        Accept=findViewById(R.id.Accept);
        Accept.setOnClickListener(this);
        Reject=findViewById(R.id.Reject);
        Reject.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Accept:
                reference = root.getReference("plants");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(name1)) {

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ViewConfirm.this);
                            builder1.setMessage(getString(R.string.plant_exists));
                            builder1.setCancelable(false);

                            builder1.setPositiveButton(
                                    getString(R.string.yes),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            admin=new Admin_Account();
                                            confirmplant=new ConfirmplantBD(name1,Symptoms,description1,information1,herbalist.getId(),herbalist.getUsername(),dat,image_plant1,used,location1);
                                            admin.confirm_plant(confirmplant);
                                            Toast.makeText(getApplicationContext(),getString(R.string.plant_added),Toast.LENGTH_SHORT).show();
                                            sendMail("( "+name1+") "+getString(R.string.approved));


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
                        else{
                            admin=new Admin_Account();
                            confirmplant=new ConfirmplantBD(name1,Symptoms,description1,information1,herbalist.getId(),herbalist.getUsername(),dat,image_plant1,used,location1);
                            admin.confirm_plant(confirmplant);
                            Toast.makeText(getApplicationContext(),getString(R.string.plant_added),Toast.LENGTH_SHORT).show();
                            sendMail("( "+name1+") "+getString(R.string.approved));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                break;
            case R.id.Reject:
                ref = FirebaseDatabase.getInstance().getReference("confirm_plants").child(name1);
                ref.removeValue();
                Toast.makeText(getApplicationContext(),getString(R.string.Plant_deleted),Toast.LENGTH_SHORT).show();
                sendMail("( "+name1+") "+getString(R.string.disapproval));



                break;
        }
    }
    @SuppressLint({"LongLogTag", "IntentReset"})
    private void sendMail(String message) {
       if (email != null){
           System.out.println(message + "====" + email + "----");
           Intent mainIntent = new Intent(ViewConfirm.this, confirm_list.class);
           mainIntent.putExtra("email", email);
           mainIntent.putExtra("text", message);
           ViewConfirm.this.startActivity(mainIntent);
       }
    }
}