package com.example.project_2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class sign_in extends AppCompatActivity implements View.OnClickListener {
    TextView sign_up;
    TextView forgetPassword;
    TextInputEditText username;
    TextInputEditText password;
    Button signIn;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        sign_up=findViewById(R.id.signUp);
        username=findViewById(R.id.username_signIn);
        password=findViewById(R.id.password_signIn);
        signIn=findViewById(R.id.sign_in);
        forgetPassword=(TextView) findViewById(R.id.forgetPASS);
        forgetPassword.setOnClickListener( this);
        mAuth=FirebaseAuth.getInstance();

         sign_up.setOnClickListener(this);
         signIn.setOnClickListener(this);

       }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgetPASS:
                startActivity(new Intent(sign_in.this, forget_password.class));
                break;
            case R.id.signUp:
                startActivity(new Intent(sign_in.this, sign_up.class));

                break;
            case R.id.sign_in:
                String usernameValue = username.getText().toString();
                String passwordValue = password.getText().toString();

                if (usernameValue.isEmpty()) {
                    username.setError(getString(R.string.username_required));
                    username.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(usernameValue).matches()) {
                    username.setError(getString(R.string.valid_email));
                    username.requestFocus();
                    return;
                }
                if (passwordValue.isEmpty()) {
                    password.setError(getString(R.string.Empty_password));
                    password.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(usernameValue, passwordValue)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    startActivity(new Intent(sign_in.this, home.class));
                                } else {
                                    Toast.makeText(com.example.project_2.sign_in.this, getString(R.string.signin_failed), Toast.LENGTH_LONG).show();

                                }
                            }
                        });

                break;

        }
    }
    }