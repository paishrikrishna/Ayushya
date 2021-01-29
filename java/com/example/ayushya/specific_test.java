package com.example.ayushya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class specific_test extends AppCompatActivity {
    public static final String EXTRA_TEXT ="com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_NUMBER ="com.example.application.example.EXTRA_NUMBER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_test);
        Intent intent = getIntent();
        final String number = intent.getStringExtra(tests.EXTRA_TEXT);
        final TextView temperature = (TextView) findViewById(R.id.textView36);
        final TextView cough = (TextView) findViewById(R.id.textView37);
        final DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("patients").child(number);
        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_test_portal("Temperature",number);
                //ref.child("specific_test").child("name").setValue("Temperature");
            }
        });
        cough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_test_portal("Cough",number);
                //ref.child("specific_test").child("name").setValue("Cough");
            }
        });
    }

    public void open_test_portal(String name,String number) {
        Intent intent = new Intent(this,test_portal.class);
        intent.putExtra(EXTRA_NUMBER,number);
        intent.putExtra(EXTRA_TEXT,name);
        startActivity(intent);
    }
}
