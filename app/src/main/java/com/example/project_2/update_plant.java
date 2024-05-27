package com.example.project_2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.project_2.Models.Admin_Account;
import com.example.project_2.Models.plantBD;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class update_plant extends AppCompatActivity implements View.OnClickListener {
    MaterialToolbar back;
    TextView plantname, symptoms,location;
    MultiAutoCompleteTextView description, information;
    Button updateplant;
    FloatingActionButton edit_plant;
    ImageView plant_image;
    CheckBox fruits,leaf,flower,trees,seeds,roots;

    static final int REQUEST_TAKE_PHOTO = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    String used = " ";

    private Uri mImageUri;
    private StorageReference mStorageRef;
    boolean[]selectedsymptoms;
    ArrayList<Integer> symptomsList =new ArrayList<>();
    String [] symptomsArray;
    boolean[]selectedlocation;
    ArrayList<Integer> locationList =new ArrayList<>();
    String [] locationArray;

    Admin_Account admin;
    plantBD plant;
    DatabaseReference reference;
    String name1 ,Symptoms ,description1 ,information1 ,image_plant1, used1,location1;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_plant);
        symptomsArray= new String[]{getString(R.string.fatigue), getString(R.string.fever), getString(R.string.stomachache), getString(R.string.headache)
                , getString(R.string.nausea), getString(R.string.skin), getString(R.string.indigestion), getString(R.string.infections), getString(R.string.diarrhea)
                , getString(R.string.constipation), getString(R.string.colds)};
        Arrays.sort(symptomsArray);

        locationArray=new String[]{getString(R.string.levant), getString(R.string.Arab_Maghreb), getString(R.string.Arabian_Peninsula), getString(R.string.Latin_america)
                , getString(R.string.Australia), getString(R.string.Europe), getString(R.string.Africa), getString(R.string.Middle_Atlantic), getString(R.string.Western_Indian)
                , getString(R.string.West_Asia), getString(R.string.Pacific), getString(R.string.North_Amarica), getString(R.string.South_America)};
        Arrays.sort(locationArray);

        plantname =findViewById(R.id.plantname);
        symptoms = findViewById(R.id.symptoms);
        description=findViewById(R.id.description);
        location=findViewById(R.id.location);
        information=findViewById(R.id.information);
        updateplant=findViewById(R.id.updateplant);
        edit_plant=findViewById(R.id.edit_plant);
        plant_image=findViewById(R.id.plant_image);
        fruits=findViewById(R.id.Fruit);
        leaf=findViewById(R.id.Leaf);
        flower=findViewById(R.id.Flower);
        trees=findViewById(R.id.Trees);
        seeds=findViewById(R.id.Seeds);
        roots=findViewById(R.id.Roots);
        back=findViewById(R.id.topAppBar);
        mStorageRef = FirebaseStorage.getInstance().getReference("users");
        reference = FirebaseDatabase.getInstance().getReference("plants");

        selectedsymptoms = new boolean[symptomsArray.length];
        selectedlocation=new boolean[locationArray.length];
        SharedPreferences prefs = getSharedPreferences("viewplant", MODE_PRIVATE);
        name1 = prefs.getString("name", "No name defined");
        Symptoms = prefs.getString("Symptoms", "No Symptoms defined");
        description1 = prefs.getString("description", "No description defined");
        information1 = prefs.getString("information", "No information defined");
        image_plant1=prefs.getString("plant_image", "No name defined");
        used1 =prefs.getString("used", "No name defined");
        location1 =prefs.getString("location", "No name defined");


        plantname.setText(name1);
        symptoms.setText(Symptoms);
        description.setText(description1);
        information.setText(information1);
        location.setText(location1);
        Glide.with(update_plant.this).load(image_plant1).apply(new RequestOptions().centerCrop().centerInside().placeholder(R.drawable.plant)).into(plant_image);

        if(used1.contains(fruits.getText()))
        {
            fruits.setChecked(true);
        }
        if(used1.contains(flower.getText()))
        {
            flower.setChecked(true);
        } if(used1.contains(trees.getText()))
        {
            trees.setChecked(true);
        } if(used1.contains(leaf.getText()))
        {
            leaf.setChecked(true);
        } if(used1.contains(seeds.getText()))
        {
            seeds.setChecked(true);
        } if(used1.contains(roots.getText()))
        {
            roots.setChecked(true);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(update_plant.this, view_plant.class));

            }
        });
        symptoms.setOnClickListener(this);
        location.setOnClickListener(this);
        symptoms.setOnClickListener(this);
        updateplant.setOnClickListener(this);
        edit_plant.setOnClickListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.edit_plant:
                selectImage();
                break;
            case R.id.symptoms:
                AlertDialog.Builder builder = new AlertDialog.Builder(update_plant.this);
                builder.setTitle(getString(R.string.select_symptoms));
                builder.setCancelable(false);
                builder.setMultiChoiceItems(symptomsArray, selectedsymptoms, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            symptomsList.add(which);
                            Collections.sort(symptomsList);
                        } else if (symptomsList.contains(which)) {
                            symptomsList.remove(Integer.valueOf(which));
                        }
                    }
                }).setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < symptomsList.size(); i++) {
                            stringBuilder.append(symptomsArray[symptomsList.get(i)]);
                            if (i != symptomsList.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        symptoms.setText(stringBuilder.toString());
                        symptoms.setError(null);

                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).setNeutralButton(getString(R.string.clear_all), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < selectedsymptoms.length; i++) {
                            selectedsymptoms[i] = false;
                            symptomsList.clear();
                            symptoms.setText("");
                        }
                    }
                }).show();

                break;
            case R.id.location:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(update_plant.this);
                builder1.setTitle(getString(R.string.Select_location));
                builder1.setCancelable(false);
                builder1.setMultiChoiceItems(locationArray, selectedlocation, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            locationList.add(which);
                            Collections.sort(locationList);
                        } else if (locationList.contains(which)) {
                            locationList.remove(Integer.valueOf(which));
                        }
                    }
                }).setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < locationList.size(); i++) {
                            stringBuilder.append(locationArray[locationList.get(i)]);
                            if (i != locationList.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }
                        location.setText(stringBuilder.toString());
                        location.setError(null);

                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).setNeutralButton(getString(R.string.clear_all), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < selectedlocation.length; i++) {
                            selectedlocation[i] = false;
                            locationList.clear();
                            location.setText("");
                        }
                    }
                }).show();

                break;
            case R.id.updateplant:
                checkDataEntered();
                if ( !symptoms.getText().toString().isEmpty() && !description.getText().toString().isEmpty()
                        &&!information.getText().toString().isEmpty() &&!location.getText().toString().isEmpty()&&
                        (fruits.isChecked()|| flower.isChecked() || trees.isChecked() || leaf.isChecked() || seeds.isChecked() ||roots.isChecked()) ){

                    if(fruits.isChecked()){
                        used+=fruits.getText()+", ";
                    }
                    if(flower.isChecked()){
                        used+=flower.getText()+", ";
                    }
                    if(seeds.isChecked()){
                        used+=seeds.getText()+", ";

                    }
                    if(trees.isChecked()){
                        used+=trees.getText()+", ";
                    }
                    if(leaf.isChecked()){
                        used+=leaf.getText()+", ";
                    }
                    if(roots.isChecked()){
                        used+=roots.getText()+", ";

                    }
                    editor = getApplicationContext().getSharedPreferences("viewplant", MODE_PRIVATE).edit();
                    reference.child(name1).child("symptoms").setValue(symptoms.getText().toString());
                    reference.child(name1).child("description").setValue(description.getText().toString());
                    reference.child(name1).child("information").setValue(information.getText().toString());
                    reference.child(name1).child("used").setValue(used);
                    reference.child(name1).child("location").setValue(location.getText().toString());

                    if (mImageUri != null) {
                        mStorageRef = mStorageRef.child(name1
                                + "." + getFileExtension(mImageUri));
                        mStorageRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final Uri downloadUrl = uri;
                                        reference.child(name1).child("plant_image").setValue(downloadUrl.toString());
                                        editor.putString("plant_image", downloadUrl.toString());
                                        image_plant1=downloadUrl.toString();

                                    }
                                });
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(update_plant.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                    }
                                });
                    }
                    editor.putString("name", name1);
                    editor.putString("Symptoms", symptoms.getText().toString());
                    editor.putString("description",description.getText().toString());
                    editor.putString("information", information.getText().toString());
                    editor.putString("used", used);
                    editor.putString("location", location.getText().toString());
                    editor.apply();
                    admin=new Admin_Account();
                    plant=new plantBD(name1,symptoms.getText().toString(),description.getText().toString(),information.getText().toString(), image_plant1,used,location.getText().toString());

                    admin.updateplant(plant);

                    Toast.makeText(getApplicationContext(),getString(R.string.Plant_updated),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(update_plant.this, search.class));
                }
                else {
                    Toast.makeText(getApplicationContext(),getString(R.string.all_fields),Toast.LENGTH_SHORT).show();

                }
                break;
        }

    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Glide.with(update_plant.this).load(mImageUri).apply(new RequestOptions().centerCrop().centerInside().placeholder(R.drawable.plant)).into(plant_image);

        }
        else  if (requestCode == REQUEST_TAKE_PHOTO) {
            Glide.with(update_plant.this).load(mImageUri).apply(new RequestOptions().centerCrop().centerInside().placeholder(R.drawable.plant)).into(plant_image);


        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose_gallery),
                getString(R.string.cancel)};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(update_plant.this);
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals(getString(R.string.take_photo))) {
                TakePicture();
            } else if (items[item].equals(getString(R.string.choose_gallery))) {
                openFileChooser();
            } else if (items[item].equals(getString(R.string.cancel))) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void checkDataEntered() {

        if ( isEmpty(symptoms)) {
            symptoms.setError(getString(R.string.Empty_symptoms));

        }
        if (isEmpty(description)) {
            description.setError(getString(R.string.Empty_description));
        }
        if ( isEmpty(location)) {
            symptoms.setError(getString(R.string.Empty_location));

        }
        if (isEmpty(information)) {
            information.setError(getString(R.string.Empty_information));
        }
    }
    boolean isEmpty( EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }
    boolean isEmpty( TextView text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }boolean isEmpty( MultiAutoCompleteTextView text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void TakePicture(){
        if (ContextCompat.checkSelfPermission(update_plant.this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(update_plant.this, new String[]{
                    Manifest.permission.CAMERA}, REQUEST_TAKE_PHOTO);
        }
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From te Camera");
        mImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraInter = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraInter.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(cameraInter, REQUEST_TAKE_PHOTO);
    }


}