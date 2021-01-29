package com.example.ayushya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class test_portal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_portal);
        Intent intent = getIntent();
        final String number = intent.getStringExtra(specific_test.EXTRA_NUMBER);
        final String test_nam = intent.getStringExtra(specific_test.EXTRA_TEXT);
        TextView test_name = (TextView) findViewById(R.id.test_name);
        final TextView swi_bu = (TextView) findViewById(R.id.swit_but);
        final TextView result = (TextView) findViewById(R.id.valu);
        test_name.setText(test_nam+" Test :");
        final DatabaseReference re;
        re = FirebaseDatabase.getInstance().getReference().child("patients").child(number).child("specific_test");
        re.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nam = dataSnapshot.child("name").getValue().toString();
                String ret = dataSnapshot.child("value").getValue().toString();
                result.setText(ret);
                if(nam.equals(test_nam)){
                    swi_bu.setVisibility(View.GONE);
                }
                else if(nam.equals("done")){
                    swi_bu.setText(" SHOW RESULT ");
                    swi_bu.setVisibility(View.VISIBLE);
                }
                else if(nam.equals("n/a")){
                    re.child("value").setValue("");
                    swi_bu.setText("START");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        swi_bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swi_bu.getText().toString().equals(" SHOW RESULT ")){
                    swi_bu.setText("DONE");
                    result.setVisibility(View.VISIBLE);
                }
                else if(swi_bu.getText().toString().equals("START")){
                    re.child("name").setValue(test_nam);
                    result.setVisibility(View.GONE);
                }
                else if(swi_bu.getText().toString().equals("DONE")){
                    re.child("name").setValue("n/a");
                }
            }
        });
    }
}
