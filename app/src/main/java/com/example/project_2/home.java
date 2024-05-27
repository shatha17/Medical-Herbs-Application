package com.example.project_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_2.Models.plantGalleryModel;
import com.example.project_2.Models.plantInfoModel;
import com.example.project_2.Models.AccountDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerViewTest;
    plantInfoRecyclerAdapter recyclerAdapter;
    List<plantInfoModel> modelList;
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static int itemPosition=0;
    RecyclerView galleryRecyclerView;
    plantGalleryAdapter galleryAdapter;
    List<plantGalleryModel> galleryModels;

    String type_account;
    private DatabaseReference reference;
    private  String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference= FirebaseDatabase.getInstance().getReference("users");
        reference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AccountDB userProfile=snapshot.getValue(AccountDB.class);

                type_account =userProfile.getAccountType();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.message:
                        Intent intent1 = new Intent(home.this, herbalist.class);
                        startActivity(intent1);
                        break;
                    case R.id.ptofile:
                        if(type_account!=null){
                        if(type_account.equals("Admin Account"))
                        {
                            Intent intent2 = new Intent(home.this, AdminProfile.class);
                            startActivity(intent2);
                            break;
                        }
                        else if(type_account.equals("Herbalist Account"))
                        {
                            Intent intent2 = new Intent(home.this, HerbalistProfile.class);
                            startActivity(intent2);
                            break;
                        }
                        else
                        {
                            Intent intent2 = new Intent(home.this, user_account.class);
                            startActivity(intent2);
                            break;
                        }
                        }
                        break;
                    case R.id.search:
                        startActivity(new Intent(home.this, search.class));
                        break;

                }
                return true;
            }
        });


        // ******* recycler view
        recyclerViewTest=findViewById(R.id.recyclerView);

        //add data to model
        modelList=new ArrayList<>();
        modelList.add(new plantInfoModel(getString(R.string.Turmeric),R.drawable.turmeric));
        modelList.add(new plantInfoModel(getString(R.string.Catnip),R.drawable.catnip));
        modelList.add(new plantInfoModel(getString(R.string.Marigold),R.drawable.mariegold));
        modelList.add(new plantInfoModel(getString(R.string.Moonflowers),R.drawable.moonflower));
        modelList.add(new plantInfoModel(getString(R.string.Gingko),R.drawable.gingko));
        setRecyclerViewTest(modelList);
        
        //*********************************
        galleryRecyclerView=findViewById(R.id.recTest);

        //add data to model
        galleryModels=new ArrayList<>();
        galleryModels.add(new plantGalleryModel(getString(R.string.fruit),R.drawable.fruit,R.drawable.fruits_icon1));
        galleryModels.add(new plantGalleryModel(getString(R.string.leaf),R.drawable.leaf,R.drawable.leaf_icon));
        galleryModels.add(new plantGalleryModel(getString(R.string.flower),R.drawable.flower,R.drawable.flower_icon1));
        galleryModels.add(new plantGalleryModel(getString(R.string.trees),R.drawable.tree,R.drawable.tree_icon1));
        galleryModels.add(new plantGalleryModel(getString(R.string.seeds),R.drawable.seeds,R.drawable.seeds_icon));
        galleryModels.add(new plantGalleryModel(getString(R.string.roots),R.drawable.root,R.drawable.root_icon));

        setRecyclerViewGallery(galleryModels);
    }

    private void setRecyclerViewGallery(List<plantGalleryModel> galleryModels1) {

        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        galleryRecyclerView.setLayoutManager(layoutManager);
        galleryAdapter=new plantGalleryAdapter(this,galleryModels1 , this::onGalleryClick);
        galleryRecyclerView.setAdapter(galleryAdapter);
    }

    /*******************************************************
     *
to make the recycler view clickable
*************************************************************     */
    public void onGalleryClick(int position) {
        itemPosition=position;
        //    Log.d("TAG","onGalleryClick : clicked.");
        //   if (galleryModels.get(position).name)
        // Intent intent=new Intent(this,galleryRecycler.class);
        // startActivity(intent);
        Intent intent = new Intent(this, gallery_recycler.class);
        String message = galleryModels.get(itemPosition).getName();
        // String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("gallery", MODE_PRIVATE).edit();
        editor.putString("message",message);

        editor.apply();
        startActivity(intent);
    }

    private void setRecyclerViewTest(List<plantInfoModel> modelList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTest.setLayoutManager(layoutManager);
        recyclerAdapter=new plantInfoRecyclerAdapter(this,modelList);
        recyclerViewTest.setAdapter(recyclerAdapter);
        final int speedScroll = 2000;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            boolean flag = true;
            @Override
            public void run() {
                if(count < recyclerAdapter.getItemCount()){
                    if(count==recyclerAdapter.getItemCount()-1){
                        flag = false;
                    }else if(count == 0){
                        flag = true;
                    }
                    if(flag)
                        count++;
                    else
                        count =0;

                    recyclerViewTest.smoothScrollToPosition(count);
                    handler.postDelayed(this,speedScroll);
                }
            }
        };

        handler.postDelayed(runnable,speedScroll);

    }

}