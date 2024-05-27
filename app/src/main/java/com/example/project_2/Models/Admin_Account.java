package com.example.project_2.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_Account extends AccountDB {



    public Admin_Account(){

    }

    public Admin_Account(String username, String email, String password, String accountType, String profile_image, String id) {
        super(username, email, password, accountType, profile_image, id);

    }


    public void addplant(plantBD plant){
        DatabaseReference reference;
        reference= FirebaseDatabase.getInstance().getReference("plants").child(plant.getName());
        reference.child("name").setValue(plant.getName());
        reference.child("symptoms").setValue(plant.getSymptoms());
        reference.child("location").setValue(plant.getLocation());
        reference.child("description").setValue(plant.getDescription());
        reference.child("information").setValue(plant.getInformation());
        reference.child("plant_image").setValue(plant.getPlant_image());
        reference.child("used").setValue(plant.getUsed());

    }
    public void confirm_plant(ConfirmplantBD confirmplant){
        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference("confirm_plants").child(confirmplant.getName());
        reference.removeValue();
        reference = FirebaseDatabase.getInstance().getReference("plants").child(confirmplant.getName());
        reference.child("name").setValue(confirmplant.getName());
        reference.child("symptoms").setValue(confirmplant.getSymptoms());
        reference.child("description").setValue(confirmplant.getDescription());
        reference.child("information").setValue(confirmplant.getInformation());
        reference.child("plant_image").setValue(confirmplant.getPlant_image());
        reference.child("location").setValue(confirmplant.getLocation());
        reference.child("used").setValue(confirmplant.getUsed());
    }
    public void updateplant(plantBD plant){
        DatabaseReference reference;
        reference= FirebaseDatabase.getInstance().getReference("plants").child(plant.getName());
        reference.child("name").setValue(plant.getName());
        reference.child("symptoms").setValue(plant.getSymptoms());
        reference.child("location").setValue(plant.getLocation());
        reference.child("description").setValue(plant.getDescription());
        reference.child("information").setValue(plant.getInformation());
        reference.child("plant_image").setValue(plant.getPlant_image());
        reference.child("used").setValue(plant.getUsed());

    }
    public void deleteplant(String name){
        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference("plants").child(name);
        reference.removeValue();
    }

}
