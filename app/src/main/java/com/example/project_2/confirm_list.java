package com.example.project_2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_2.Models.ConfirmplantBD;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class confirm_list extends AppCompatActivity {
    DatabaseReference databaseReference;
    ArrayList<ConfirmplantBD> confirmplantBDS;
    RecyclerView recyclerView;
    Context context=confirm_list.this;
    MaterialToolbar back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_list);
        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){
            sendMail(bundle.getString("email"),bundle.getString("text"));
        }
        confirmplantBDS =new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference= FirebaseDatabase.getInstance().getReference().child("confirm_plants");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    confirmplantBDS=new ArrayList<>();
                    for(DataSnapshot ds:snapshot.getChildren()){
                        confirmplantBDS.add(ds.getValue(ConfirmplantBD.class));
                    }

                    Confirmadapter myadapter=new Confirmadapter(confirmplantBDS,context);
                    recyclerView.setAdapter(myadapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back=findViewById(R.id.topAppBar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(confirm_list.this, home.class));

            }
        });
    }
    private void sendMail(String mail,String message) {
        String subject =getString(R.string.confirm_plant);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mail});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
        Toast.makeText(getApplicationContext(), getString(R.string.send_email), Toast.LENGTH_SHORT).show();

    }
}