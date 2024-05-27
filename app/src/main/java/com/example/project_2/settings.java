package com.example.project_2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.project_2.Models.AccountDB;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;

public class settings extends AppCompatActivity implements View.OnClickListener {

    CardView change_image,edit_image,Privacy_image,terms_image,change_lang;
    MaterialToolbar back;
    String type_account;
    private DatabaseReference reference;
    private  String userID;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        change_image=findViewById(R.id.change_image);
        change_lang=findViewById(R.id.change_lang);
        edit_image=findViewById(R.id.edit_image);
        Privacy_image=findViewById(R.id.Privacy_image);
        terms_image=findViewById(R.id.terms_image);
        back=findViewById(R.id.topAppBar);

        change_image.setOnClickListener(this);
        edit_image.setOnClickListener(this);
        Privacy_image.setOnClickListener(this);
        terms_image.setOnClickListener(this);
        back.setOnClickListener(this);
        change_lang.setOnClickListener(this);
        userID= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topAppBar:
                if(type_account.equals("Admin Account"))
                {
                    Intent intent2 = new Intent(settings.this, AdminProfile.class);
                    startActivity(intent2);
                    break;
                }
                else if(type_account.equals("Herbalist Account"))
                {
                    Intent intent2 = new Intent(settings.this, HerbalistProfile.class);
                    startActivity(intent2);
                    break;
                }
                else
                {
                    Intent intent2 = new Intent(settings.this, user_account.class);
                    startActivity(intent2);
                    break;
                }
            case R.id.change_image:
                startActivity(new Intent(settings.this, change_password.class));
                break;
            case R.id.edit_image:
                startActivity(new Intent(settings.this, Edit_userprofile.class));
                break;
            case R.id.Privacy_image:
                startActivity(new Intent(settings.this, privacy_policy.class));
                break;
            case R.id.terms_image:
                startActivity(new Intent(settings.this, Terms_Condition.class));
                break;
            case R.id.change_lang:
                change_lang();
                break;
        }
    }
    private void change_lang() {
        String []list_item={"English","العربية"};
        AlertDialog.Builder builder=new AlertDialog.Builder(settings.this);
        builder.setTitle(getString(R.string.Select_language));
        builder.setSingleChoiceItems(list_item, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        loadLocale();
                        setLocale("en");
                        recreate();


                        break;
                    case 1:
                        loadLocale();
                        setLocale("ar");
                        recreate();

                        break;
                }
            }
        });
        alertDialog=builder.create();
        alertDialog.show();
    }
    private void setLocale( String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My lang",language);
        editor.apply();
        alertDialog.dismiss();


    }
    public void loadLocale(){
        SharedPreferences preferences=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=preferences.getString("My lang","");
        setLocale(language);
    }

}