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
import com.google.firebase.auth.FirebaseAuth;

public class forget_password extends AppCompatActivity implements View.OnClickListener {
TextInputEditText email;
Button resetPass;
TextView register;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        email=findViewById(R.id.email_forgetPass);
        resetPass=findViewById(R.id.newPass_forgetPass);
        resetPass.setOnClickListener(this);
        register=findViewById(R.id.register_forgetPass);
        register.setOnClickListener(this);
        auth=FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newPass_forgetPass:
resetPassword();
                break;
            case R.id.register_forgetPass:
                startActivity(new Intent(forget_password.this, sign_up.class));

                break;
        }
    }

    private void resetPassword() {
        String emailValue=email.getText().toString().trim();

        if (emailValue.isEmpty()) {
            email.setError(getString(R.string.email_required));
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            email.setError(getString(R.string.valid_email));
            email.requestFocus();
            return;
        }

        auth.sendPasswordResetEmail(emailValue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(com.example.project_2.forget_password.this, getString(R.string.Check_email),
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(com.example.project_2.forget_password.this, getString(R.string.Try_Again),
                            Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}