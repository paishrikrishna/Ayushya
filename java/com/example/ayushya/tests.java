package com.example.ayushya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class tests extends AppCompatActivity {
    public static final String EXTRA_TEXT ="com.example.application.example.EXTRA_TEXT";
    String name,da,pu,tem,tty,in,option,number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);
        Calendar calendar = Calendar.getInstance();
        final String datte = DateFormat.getDateInstance().format(calendar.getTime());
        Intent intent = getIntent();
         option = intent.getStringExtra(option_list.EXTRA_TEXT);
         number = intent.getStringExtra(option_list.EXTRA_NUMBER);
        if(option.isEmpty()){
            option = intent.getStringExtra(MapsActivity.EXTRA_TEXT);

        }
        if(number.isEmpty()) {
            number = intent.getStringExtra(MapsActivity.EXTRA_NUMBER);
        }
        TableLayout tl = (TableLayout) findViewById(R.id.Report);

        final TextView date = (TextView) findViewById(R.id.date);
        final TextView temp = (TextView) findViewById(R.id.temperature);
        final TextView type = (TextView) findViewById(R.id.cough_type);
        final TextView intensity = (TextView) findViewById(R.id.cough_intensity);
        final TextView pulse = (TextView) findViewById(R.id.pulse);
        final TextView nsm = (TextView) findViewById(R.id.name);
        final TextView full_che = (TextView) findViewById(R.id.full_check);
        final TextView symp = (TextView) findViewById(R.id.symptoms);

        TextView tx2 = (TextView) findViewById(R.id.textView21);
        tx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_specific_checkup(number);
            }
        });
        if(option.equals("   Previous Check UP   ")){
         tl.setVisibility(View.VISIBLE);
         full_che.setVisibility(View.GONE);
         tx2.setVisibility(View.GONE);
        }

        final DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("patients").child(number);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name =dataSnapshot.child("name").getValue().toString();
                 da = dataSnapshot.child("tests").child("date").getValue().toString();
                pu = dataSnapshot.child("tests").child("pulse").getValue().toString();
                 tem = dataSnapshot.child("tests").child("temperature").getValue().toString();
                 tty = dataSnapshot.child("tests").child("cought").getValue().toString();
                 in = dataSnapshot.child("tests").child("coughi").getValue().toString();
                date.setText(da);
                temp.setText(tem);
                type.setText(tty);
                intensity.setText(in);
                nsm.setText(name);
                pulse.setText(pu);
                if((tty.equals("DRY")||tty.equals("WET")) && (in.equals("HIGH")||in.equals("MODERATE"))&&(Float.parseFloat(tem)>38.00)){
                    ref.child("disease").setValue("COVID-19");
                    symp.setText("COVID-19");
                    symp.setTextColor(Color.RED);
                }
                else{
                    ref.child("disease").setValue("COMMON COLD");
                    symp.setText("COMMON COLD");
                }
                if(Float.parseFloat(tem)>38.00){
                    temp.setTextColor(Color.RED);
                }
                else
                {
                    temp.setTextColor(Color.GREEN);
                }
                if(in.equals("HIGH")){
                    intensity.setTextColor(Color.RED);
                }
                else if(in.equals("MODERATE")){
                    intensity.setTextColor(Color.MAGENTA);
                }
                else{
                    intensity.setTextColor(Color.GREEN);
                }
                if(Integer.parseInt(pu)>180){
                    pulse.setTextColor(Color.RED);
                }
                else{
                    pulse.setTextColor(Color.GREEN);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        full_che.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("tests").child("date").setValue(datte);
                open_checkup(number);
            }
        });
    }

    public void open_checkup(String number) {
        Intent intent = new Intent(this,Temperature.class);
        intent.putExtra(EXTRA_TEXT,number);
        startActivity(intent);
    }
    public void open_specific_checkup(String number) {
        Intent intent = new Intent(this,specific_test.class);
        intent.putExtra(EXTRA_TEXT,number);
        startActivity(intent);
    }
}
