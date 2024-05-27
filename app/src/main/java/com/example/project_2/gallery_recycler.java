package com.example.project_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_2.Models.plantBD;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class gallery_recycler extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView galleryRecyclerView;
    plantTypeGalleryAdapter galleryAdapter;
    List<plantBD> galleryModels;
    ImageView imageView ,back;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_recycler);
        // Get the Intent that started this activity and extract the string
        SharedPreferences prefs = getSharedPreferences("gallery", MODE_PRIVATE);
        String message = prefs.getString("message", "No name defined");
        // Capture the layout's TextView and set the string as its text
        textView = findViewById(R.id.intentMsg);
        textView.setText(message);
        galleryRecyclerView=findViewById(R.id.rec22);
        galleryModels=new ArrayList<>();
        imageView=findViewById(R.id.coverImg);
        back=findViewById(R.id.back11);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery_recycler.this.startActivity(new Intent(gallery_recycler.this, home.class));
            }
        });

        databaseReference= FirebaseDatabase.getInstance().getReference().child("plants");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    galleryModels=new ArrayList<>();
                    for(DataSnapshot ds:snapshot.getChildren()) {
                        plantBD plant = ds.getValue(plantBD.class);
                        if (plant.getUsed() != null) {
                            if (plant.getUsed().contains(message)) {
                                galleryModels.add(plant);
                            }
                        }
                        setRecyclerViewTest(galleryModels);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if ("Fruit".equals(textView.getText()) || "فاكهة".equals(textView.getText())) {
            imageView.setImageResource(R.drawable.fruit);
        }
        else if ("Leaf".equals(textView.getText())|| "ورقة".equals(textView.getText())) {
            imageView.setImageResource(R.drawable.leaf);
        }
        else if ("Flower".equals(textView.getText())|| "زهرة".equals(textView.getText())) {
            imageView.setImageResource(R.drawable.flower);
            setRecyclerViewTest(galleryModels);
        }
        else if ("Trees".equals(textView.getText())|| "الأشجار".equals(textView.getText()))
        {
            imageView.setImageResource(R.drawable.tree);
        }
        else if ("Seeds".equals(textView.getText())|| "بذور".equals(textView.getText()))
        {
            imageView.setImageResource(R.drawable.seeds);
        }else if ("Roots".equals(textView.getText())|| "الجذور".equals(textView.getText()))
        {
            imageView.setImageResource(R.drawable.root);
        }
    }

    private void setRecyclerViewTest(List<plantBD> galleryModels) {
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 3);
        galleryRecyclerView.setLayoutManager(layoutManager);
        galleryAdapter=new plantTypeGalleryAdapter(this,galleryModels );
        galleryRecyclerView.setAdapter(galleryAdapter);
    }}