package com.example.project_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_2.Models.AccountDB;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class change_password extends AppCompatActivity implements View.OnClickListener {
    MaterialToolbar back;
    TextView Cancel;
    TextInputEditText cur_password,new_password,con_password;
    Button change_button;
    FirebaseAuth mAuth;
    AccountDB accountDB;

    private FirebaseUser user;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        reference= FirebaseDatabase.getInstance().getReference("users");
        back=findViewById(R.id.topAppBar1);
        back.setOnClickListener(this);
        Cancel=findViewById(R.id.cancel);
        Cancel.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
        accountDB=new AccountDB();

        cur_password=findViewById(R.id.cur_password);
        new_password=findViewById(R.id.new_password);
        con_password=findViewById(R.id.con_password);
        change_button=findViewById(R.id.change_button);
        change_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.topAppBar1 :
                startActivity(new Intent(change_password.this, settings.class));
                break;
            case R.id.cancel:
                startActivity(new Intent(change_password.this, settings.class));
                break;
            case R.id.change_button:
                if(!cur_password.getText().toString().isEmpty()&&!new_password.getText().toString().isEmpty()
                        &&!con_password.getText().toString().isEmpty()){
                    accountDB.changaPassword(cur_password.getText().toString(),new_password.getText().toString(),con_password.getText().toString()
                    ,change_password.this);

                }
                else{
                    Toast.makeText(getApplicationContext(),getString(R.string.all_fields),Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}