package com.example.ayushya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_TEXT ="com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_NUMBER ="com.example.application.example.EXTRA_NUMBER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView patient = (ImageView) findViewById(R.id.patient_select);
        ImageView doctor = (ImageView) findViewById(R.id.doctor_select);
        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_portal("patients");
            }
        });
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_portal("doctors");
            }
        });
    }

    public void login_portal(String option) {
        Intent intent = new Intent(this,login.class);
        intent.putExtra(EXTRA_TEXT,option);
        startActivity(intent);
    }
}
