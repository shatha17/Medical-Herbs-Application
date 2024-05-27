package com.example.project_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.project_2.Models.AccountDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private String receiverUserID, senderUserID, Current_State;

    private CircleImageView userProfileImage;
    private TextView userProfileName, userProfileStatus;
    private Button SendMessageRequestButton;

    private DatabaseReference UserRef;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");


        receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
        senderUserID = mAuth.getCurrentUser().getUid();


        userProfileImage = (CircleImageView) findViewById(R.id.visit_profile_image);
        userProfileName = (TextView) findViewById(R.id.visit_user_name);
        userProfileStatus = (TextView) findViewById(R.id.visit_profile_status);
        SendMessageRequestButton = (Button) findViewById(R.id.send_message_request_button);
        Current_State = "new";

        RetrieveUserInfo();

        SendMessageRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        AccountDB userProfile=dataSnapshot.getValue(AccountDB.class);

                            Intent chatIntent = new Intent(ProfileActivity.this, ChatActivity.class);
                            chatIntent.putExtra("visit_user_id", receiverUserID);
                            //chatIntent.putExtra("visit_user_state", state);
                            chatIntent.putExtra("visit_user_name", userProfileName.getText().toString());
                            chatIntent.putExtra("visit_image", userProfile.getProfile_image());
                            startActivity(chatIntent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

    }

    private void RetrieveUserInfo() {
        UserRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                AccountDB userProfile=dataSnapshot.getValue(AccountDB.class);
                if ((dataSnapshot.exists()))
                {
                    String userImage = userProfile.getProfile_image();
                    String userName = userProfile.getUsername();
                    String userstatus = userProfile.getAccountType()+"\n"+userProfile.getEmail();

                    Glide.with(getApplicationContext()).load(userProfile.getProfile_image()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.user)).into(userProfileImage);
                    userProfileName.setText(userName);
                    userProfileStatus.setText(userstatus);
                }
                else
                {
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String userstatus = dataSnapshot.child("status").getValue().toString();

                    userProfileName.setText(userName);
                    userProfileStatus.setText(userstatus);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}