package com.example.project_2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.project_2.Models.AccountDB;
import com.example.project_2.Models.Admin_Account;
import com.example.project_2.Models.Herbalist_Account;
import com.example.project_2.Models.ConfirmplantBD;
import com.example.project_2.Models.plantBD;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class add_plant extends AppCompatActivity implements View.OnClickListener{
    MaterialToolbar back;
    EditText plantame;
    TextView symptoms,location;
    CheckBox fruits,leaf,flower,trees,seeds,roots;
    MultiAutoCompleteTextView description, information;
    Button addplant;
    boolean[]selectedsymptoms;
    ArrayList<Integer> symptomsList =new ArrayList<>();
    String [] symptomsArray;

    boolean[]selectedlocation;
    ArrayList<Integer> locationList =new ArrayList<>();
    String [] locationArray;

    FirebaseDatabase root;
    FirebaseUser user;
    FirebaseAuth mAuth ;
    DatabaseReference reference,reference_user;
    String userID;
    String used=" " ;
    String type_account;
    AccountDB accountDB;
    Admin_Account admin;
    Herbalist_Account herbalist;
    plantBD plant;
    ConfirmplantBD confirmplant;


    FloatingActionButton add_img;
    ImageView plant_img;
    static final int REQUEST_TAKE_PHOTO = 100;
    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri=null;
    private StorageReference mStorageRef;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        symptomsArray= new String[]{getString(R.string.fatigue), getString(R.string.fever), getString(R.string.stomachache), getString(R.string.headache)
                , getString(R.string.nausea), getString(R.string.skin), getString(R.string.indigestion), getString(R.string.infections), getString(R.string.diarrhea)
                , getString(R.string.constipation), getString(R.string.colds)};
        Arrays.sort(symptomsArray);

        locationArray=new String[]{getString(R.string.levant), getString(R.string.Arab_Maghreb), getString(R.string.Arabian_Peninsula), getString(R.string.Latin_america)
                , getString(R.string.Australia), getString(R.string.Europe), getString(R.string.Africa), getString(R.string.Middle_Atlantic), getString(R.string.Western_Indian)
                , getString(R.string.West_Asia), getString(R.string.Pacific), getString(R.string.North_Amarica), getString(R.string.South_America)};
        Arrays.sort(locationArray);

        plantame =findViewById(R.id.plantname);
        symptoms = findViewById(R.id.symptoms);
        location=findViewById(R.id.location);
        description=findViewById(R.id.description);
        information=findViewById(R.id.information);
        addplant=findViewById(R.id.addplant);
        back=findViewById(R.id.topAppBar);
        add_img=findViewById(R.id.add_img);
        plant_img=findViewById(R.id.plant_img);
        fruits=findViewById(R.id.Fruit);
        leaf=findViewById(R.id.Leaf);
        flower=findViewById(R.id.Flower);
        trees=findViewById(R.id.Trees);
        seeds=findViewById(R.id.Seeds);
        roots=findViewById(R.id.Roots);
        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("plants");

        user= FirebaseAuth.getInstance().getCurrentUser();
        selectedsymptoms = new boolean[symptomsArray.length];
        selectedlocation=new boolean[locationArray.length];
        symptoms.setOnClickListener(this);
        location.setOnClickListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(add_plant.this, home.class));

            }
        });
        add_img.setOnClickListener(this);
        addplant.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        userID=user.getUid();
        reference_user= FirebaseDatabase.getInstance().getReference("users");
        reference_user.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accountDB = snapshot.getValue(AccountDB.class);

                type_account =accountDB.getAccountType();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.symptoms:
                AlertDialog.Builder builder = new AlertDialog.Builder(add_plant.this);
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(add_plant.this);
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
            case R.id.add_img:
                selectImage();
                break;
            case R.id.addplant:
                checkDataEntered();
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
                if (!plantame.getText().toString().isEmpty() && !symptoms.getText().toString().isEmpty() &&
                        !description.getText().toString().isEmpty() &&!information.getText().toString().isEmpty() &&(used!=null)
                        && !location.getText().toString().isEmpty()){
                    final String plantnameValue = plantame.getText().toString();
                    final String symptomsValue = symptoms.getText().toString();
                    final String descriptionValue = description.getText().toString();
                    final String informationValue = information.getText().toString();
                    final String locationValue = location.getText().toString();

                    final String[] img = new String[1];

                    if (type_account != null) {
                        if (type_account.equals("Admin Account")) {
                            reference = root.getReference("plants");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.hasChild(plantnameValue)) {

                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(add_plant.this);
                                        builder1.setMessage(getString(R.string.plant_exists));
                                        builder1.setCancelable(false);
                                        builder1.setPositiveButton(
                                                getText(R.string.yes),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        if (mImageUri != null) {
                                                            mStorageRef = mStorageRef.child(plantnameValue
                                                                    + "." + getFileExtension(mImageUri));
                                                            mStorageRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                    mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                        @Override
                                                                        public void onSuccess(Uri uri) {
                                                                            final Uri downloadUrl = uri;
                                                                            if(downloadUrl!=null){
                                                                                admin=new Admin_Account();
                                                                                plant=new plantBD(plantnameValue,symptomsValue,descriptionValue,informationValue, downloadUrl.toString(),used,locationValue);
                                                                                admin.addplant(plant);
                                                                                Toast.makeText(getApplicationContext(), getString(R.string.plant_added), Toast.LENGTH_SHORT).show();
                                                                                Intent mainIntent = new Intent(getApplicationContext(), home.class);
                                                                                add_plant.this.startActivity(mainIntent);
                                                                            }

                                                                        }
                                                                    });

                                                                }
                                                            })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(add_plant.this, "***"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    })
                                                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                                        @Override
                                                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                                                        }
                                                                    });
                                                        }

                                                    }
                                                });

                                        builder1.setNegativeButton(
                                                getText(R.string.no),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();
                                    } else {

                                        if (mImageUri != null) {
                                            mStorageRef = mStorageRef.child(plantnameValue
                                                    + "." + getFileExtension(mImageUri));
                                            mStorageRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            final Uri downloadUrl = uri;
                                                            if(downloadUrl!=null){
                                                                admin=new Admin_Account();
                                                                plant=new plantBD(plantnameValue,symptomsValue,descriptionValue,informationValue, downloadUrl.toString(),used,locationValue);
                                                                admin.addplant(plant);
                                                                Toast.makeText(getApplicationContext(), getString(R.string.plant_added), Toast.LENGTH_SHORT).show();
                                                                Intent mainIntent = new Intent(getApplicationContext(), home.class);
                                                                add_plant.this.startActivity(mainIntent);
                                                            }

                                                        }
                                                    });

                                                }
                                            })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(add_plant.this, "***"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                                        }
                                                    });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        } else {

                            AlertDialog.Builder builder4 = new AlertDialog.Builder(add_plant.this);
                            builder4.setMessage(getString(R.string.admin_confirmation));
                            builder4.setCancelable(false);
                            builder4.setPositiveButton(
                                    getText(R.string.yes),
                                    new DialogInterface.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        public void onClick(DialogInterface dialog, int id) {
                                            if (mImageUri != null) {
                                                mStorageRef = mStorageRef.child(plantnameValue
                                                        + "." + getFileExtension(mImageUri));
                                                mStorageRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                final Uri downloadUrl = uri;

                                                                if(downloadUrl!=null){
                                                                    Date date = new Date();
                                                                    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                                                    String date_plant=localDate.getMonthValue() + "/" + localDate.getDayOfMonth() + "/" + localDate.getYear();
                                                                    herbalist=new Herbalist_Account();
                                                                    confirmplant=new ConfirmplantBD(plantnameValue,symptomsValue,descriptionValue,informationValue,userID,
                                                                            accountDB.getUsername(),date_plant, downloadUrl.toString(),used,locationValue);
                                                                  //  herbalist.addplant(confirmplant);
                                                                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("confirm_plants").child(plantnameValue);
                                                                    reference.child("name").setValue(plantnameValue);
                                                                    reference.child("symptoms").setValue(symptomsValue);
                                                                    reference.child("description").setValue(descriptionValue);
                                                                    reference.child("location").setValue(locationValue);
                                                                    reference.child("information").setValue(informationValue);
                                                                    reference.child("id").setValue(userID);
                                                                    reference.child("added_by").setValue(accountDB.getUsername());
                                                                    reference.child("plant_image").setValue(downloadUrl.toString());
                                                                    reference.child("date").setValue(date_plant);
                                                                    reference.child("used").setValue(used);
                                                                    Toast.makeText(getApplicationContext(), getString(R.string.plant_added), Toast.LENGTH_SHORT).show();


                                                                    Intent mainIntent = new Intent(add_plant.this, home.class);
                                                                    add_plant.this.startActivity(mainIntent);
                                                                }

                                                            }
                                                        });

                                                    }
                                                })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(add_plant.this, "***"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                                            }
                                                        });
                                            }

                                        }
                                    });

                                builder4.setNegativeButton(
                                        getText(R.string.no),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder4.create();
                                alert11.show();
                                }

                            }

                }
                else {
                    Toast.makeText(getApplicationContext(),getString(R.string.all_fields),Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void checkDataEntered() {
        if (isEmpty(plantame)) {
            plantame.setError(getString(R.string.Empty_plant_name));
        }
        if ( isEmpty(symptoms)) {
            symptoms.setError(getString(R.string.Empty_symptoms));

        }
        if ( isEmpty(location)) {
            symptoms.setError(getString(R.string.Empty_location));

        }
        if (isEmpty(description)) {
            description.setError(getString(R.string.Empty_description));
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
    }
    boolean isEmpty( MultiAutoCompleteTextView text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
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
            Glide.with(add_plant.this).load(mImageUri).apply(new RequestOptions().centerCrop().centerInside().placeholder(R.drawable.plant)).into(plant_img);

        }
        else  if (requestCode == REQUEST_TAKE_PHOTO) {
            Glide.with(add_plant.this).load(mImageUri).apply(new RequestOptions().centerCrop().centerInside().placeholder(R.drawable.plant)).into(plant_img);


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
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(add_plant.this);
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void TakePicture(){
        if (ContextCompat.checkSelfPermission(add_plant.this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(add_plant.this, new String[]{
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