package com.example.project_2.Models;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project_2.R;
import com.example.project_2.settings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AccountDB {
    String username;
    String id;
    String email;
    String password;
    String accountType;
    String profile_image;

    ArrayList<AccountDB> accountDBS;
    ArrayList<plantBD>plant;


    public AccountDB() {
    }

    public AccountDB(String username, String email, String password, String accountType, String profile_image, String id ) {
        this.username = username;
        this.id=id;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.profile_image =profile_image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<plantBD> getPlant() {
        return plant;
    }

    public void setPlant(ArrayList<plantBD> plant) {
        this.plant = plant;
    }
    public ArrayList<AccountDB> getAccountDBS() {
        return accountDBS;
    }

    public void setAccountDBS(ArrayList<AccountDB> accountDBS) {
        this.accountDBS = accountDBS;
    }

    public ArrayList<plantBD> searchName(String s)
    {
        ArrayList<plantBD>list=new ArrayList<>();
        for(plantBD object: plant){
            if(object.getName().toLowerCase().startsWith(s.toLowerCase())){
                list.add(object);
            }
        }
        return list;

    }

    public ArrayList<plantBD> searchSymptoms(String s)
    {
        String[] arrOfStr = s.split(",");

        ArrayList<plantBD>list=new ArrayList<>();
        for(plantBD object: plant){
            boolean exists=true;
            for (String a : arrOfStr){
                if(!object.getSymptoms().contains(a))
                    exists=false;
            }
            if(exists)
                list.add(object);

        }
      return list;

    }
    public ArrayList<AccountDB> searchHerbalist(String name ) {
        ArrayList<AccountDB>list=new ArrayList<>();
        for(AccountDB user: accountDBS){
            if( user.getUsername().startsWith(name)){
                list.add(user);
            }
        }
        return list;

    }
    public void changaPassword(String cur_password, String new_password, String con_password, Context context){
        FirebaseUser user;
        DatabaseReference reference;
        FirebaseAuth mAuth;
        reference= FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), cur_password);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if(new_password.equals(con_password)){

                                user.updatePassword(new_password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            reference.child(user.getUid()).child("password").setValue(new_password);
                                            Toast.makeText(context,context.getString(R.string.Password_updated),Toast.LENGTH_SHORT).show();
                                            context.startActivity(new Intent(context, settings.class));

                                        } else {
                                            Toast.makeText(context,context.getString(R.string.Error_password),Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });}
                            else {
                                Toast.makeText(context,context.getString(R.string.Password_mismatching),Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(context,context.getString(R.string.Password_incorrect),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}
