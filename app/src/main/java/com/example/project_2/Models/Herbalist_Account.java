package com.example.project_2.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Herbalist_Account extends AccountDB {


    public Herbalist_Account() {

    }

    public Herbalist_Account(String username, String email, String password, String accountType, String profile_image, String id) {
        super(username, email, password, accountType, profile_image, id);
    }


    public void addplant(ConfirmplantBD plant){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("confirm_plants").child(plant.getName());
        reference.child("name").setValue(plant.getName());
        reference.child("symptoms").setValue(plant.getSymptoms());
        reference.child("description").setValue(plant.getDescription());
        reference.child("location").setValue(plant.getLocation());
        reference.child("information").setValue(plant.getInformation());
        reference.child("id").setValue(plant.getId());
        reference.child("added_by").setValue(plant.getAdded_by());
        reference.child("plant_image").setValue(plant.getPlant_image());
        reference.child("date").setValue(plant.getDate());
        reference.child("used").setValue(plant.getUsed());

    }

}
