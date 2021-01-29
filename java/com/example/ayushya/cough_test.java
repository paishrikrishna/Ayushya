package com.example.ayushya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileReader;

public class cough_test extends AppCompatActivity {
    public static final String EXTRA_TEXT ="com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_NUMBER ="com.example.application.example.EXTRA_NUMBER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cough_test);
        Intent intent = getIntent();
        final String number = intent.getStringExtra(option_list.EXTRA_TEXT);
        TextView dry = (TextView) findViewById(R.id.textView24);
        TextView wet = (TextView) findViewById(R.id.textView25);
        final TextView instru = (TextView) findViewById(R.id.instru);
        final TextView ch = (TextView) findViewById(R.id.high);
        final TextView cm = (TextView) findViewById(R.id.med);
        final TextView cl = (TextView) findViewById(R.id.low);
        final TextView butt = (TextView) findViewById(R.id.swit);
        final DatabaseReference re;
        re = FirebaseDatabase.getInstance().getReference().child("patients").child(number);
        ch.setVisibility(View.GONE);
        cm.setVisibility(View.GONE);
        cl.setVisibility(View.GONE);
        instru.setVisibility(View.GONE);
        butt.setVisibility(View.GONE);
        dry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re.child("tests").child("cought").setValue("DRY");
                butt.setText("  DONE  ");
                ch.setVisibility(View.VISIBLE);
                cm.setVisibility(View.VISIBLE);
                cl.setVisibility(View.VISIBLE);
                instru.setVisibility(View.GONE);

            }
        });
        wet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re.child("tests").child("cought").setValue("WET");
                ch.setVisibility(View.GONE);
                cm.setVisibility(View.GONE);
                cl.setVisibility(View.GONE);
                instru.setVisibility(View.VISIBLE);
                butt.setVisibility(View.VISIBLE);
                butt.setText("  START  ");
            }
        });
        ch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re.child("tests").child("coughi").setValue("HIGH");
                butt.setVisibility(View.VISIBLE);
            }
        });
        cm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re.child("tests").child("coughi").setValue("MODERATE");
                butt.setVisibility(View.VISIBLE);
            }
        });
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re.child("tests").child("coughi").setValue("LOW");
                butt.setVisibility(View.VISIBLE);
            }
        });
        re.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("current_test").getValue().toString();
                if(status.equals("dOne")){
                    butt.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), " Cough Test Done Successful", Toast.LENGTH_SHORT).show();
                    butt.setText("  NEXT  ");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String condi = butt.getText().toString();
                if(condi.equals("  DONE  ")){
                    Toast.makeText(getApplicationContext(), " Cough Test Done Successful", Toast.LENGTH_SHORT).show();
                    re.child("current_test").setValue("dOne");
                }
                else if(condi.equals("  START  ")){
                    re.child("current_test").setValue("Cough");
                    Toast.makeText(getApplicationContext(), " Cough Test Started", Toast.LENGTH_SHORT).show();
                    butt.setVisibility(View.GONE);
                }
                else if(condi.equals("  NEXT  ")){
                    re.child("current_test").setValue("n/a");
                    //open next slide
                }
            }
        });
    }
}
