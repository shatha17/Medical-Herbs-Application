package com.example.project_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

public class Terms_Condition extends AppCompatActivity {
    MaterialToolbar back;
    boolean signup=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms__condition);
        back=findViewById(R.id.topAppBar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup = getIntent().getBooleanExtra("sign_up",false);
                if(signup) {
                    startActivity(new Intent(Terms_Condition.this, sign_up.class));
                }
                else {
                    startActivity(new Intent(Terms_Condition.this, settings.class));

                }
            }
        });
    }
}