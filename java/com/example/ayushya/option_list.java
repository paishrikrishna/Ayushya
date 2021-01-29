package com.example.ayushya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class option_list extends AppCompatActivity implements LocationListener {
    public static final String EXTRA_TEXT ="com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_NUMBER ="com.example.application.example.EXTRA_NUMBER";
    String Name;
    double Lati,Lon;
    int f=0;
    String la,lt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_list);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }

        assert locationManager != null;
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        assert null != location;
        onLocationChanged(location);

        Intent intent = getIntent();
        final String number = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        final String option = intent.getStringExtra(MainActivity.EXTRA_NUMBER);
        TextView tx = (TextView) findViewById(R.id.textView7);
        final TextView tx1 = (TextView) findViewById(R.id.full_check);
        TextView tx2 = (TextView) findViewById(R.id.textView10);
        final TextView tx3 = (TextView) findViewById(R.id.textView11);
        final TextView quarant = (TextView) findViewById(R.id.quar);
        TextView tx4 = (TextView) findViewById(R.id.textView12);
        TextView tx5 = (TextView) findViewById(R.id.textView13);


        final Switch sw = (Switch) findViewById(R.id.switch1);


        if(option.equals("patients")){
            tx1.setVisibility(View.VISIBLE);
            tx2.setVisibility(View.VISIBLE);
            tx3.setVisibility(View.VISIBLE);
            quarant.setVisibility(View.VISIBLE);
            tx4.setVisibility(View.GONE);
            tx5.setVisibility(View.GONE);
            sw.setVisibility(View.GONE);

        }
        else if(option.equals("doctors")){
            tx4.setVisibility(View.VISIBLE);
            tx5.setVisibility(View.VISIBLE);
            sw.setVisibility(View.VISIBLE);
            quarant.setVisibility(View.GONE);
            tx3.setVisibility(View.GONE);
            tx2.setVisibility(View.GONE);
            tx1.setVisibility(View.GONE);
        }
        final TextView tr = (TextView) findViewById(R.id.textView8);
        tx.setText(number);
        final DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child(option).child(number);
        if(option.equals("patients")) {
            ref.child("location").child("lat").setValue(Lati);
            ref.child("location").child("long").setValue(Lon);
        }
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                la = dataSnapshot.child("location").child("lat").getValue().toString();
                lt = dataSnapshot.child("location").child("long").getValue().toString();
                if(option.equals("doctors")&&f==0) {
                    if (la.equals("o/f") && lt.equals("o/f")) {
                        sw.setChecked(false);
                        sw.setText("OFFLINE");
                    } else {
                        sw.setChecked(true);
                        sw.setText("ONLINE");
                    }
                    f=1;
                }
                if(option.equals("patients")) {
                    String quarantine_status = dataSnapshot.child("quarantine").child("status").getValue().toString();
                    String device = dataSnapshot.child("device_no").getValue().toString();
                    if(device.equals("n/a")){
                        tx3.setVisibility(View.GONE);
                    }
                    if (quarantine_status.equals("yes")) {
                        quarant.setVisibility(View.VISIBLE);
                    } else if (quarantine_status.equals("no")) {
                        ref.child("quarantine").child("center").setValue("n/a");
                        quarant.setVisibility(View.GONE);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ref.child("location").child("lat").setValue(Lati);
                    ref.child("location").child("long").setValue(Lon);
                    sw.setText("ONLINE");
                } else {
                    ref.child("location").child("lat").setValue("o/f");
                    ref.child("location").child("long").setValue("o/f");
                    sw.setText("OFFLINE");
                }
            }
        });
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Name = dataSnapshot.child("name").getValue().toString();

                tr.setText("Welcome "+ Name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tx1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan_device(tx1.getText().toString(),number);

            }
        });
        tx3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_test(tx3.getText().toString(),number);
            }
        });
        tx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_maps("doctors",number); // patients loging part
            }
        });
        tx4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_maps("patients",number); // Doctors login Part
            }
        });
        quarant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_quarantine(number);
            }
        });
    }

    public void open_quarantine(String patient_id) {
        Intent intent = new Intent(this,quarantine_window.class);
        intent.putExtra(EXTRA_TEXT,patient_id);
        startActivity(intent);
    }

    public void  scan_device(final String s, final String number) {
        final String[] re = new String[1];
        ZXingScannerView scannerView = new ZXingScannerView(this);
        scannerView = new ZXingScannerView(this);
        final ZXingScannerView finalScannerView = scannerView;
        scannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                re[0] = result.getText();
                //setContentView(R.layout.activity_main);
                final DatabaseReference rre;
                rre = FirebaseDatabase.getInstance().getReference();
                rre.child("devices").child(re[0]).child("patient_id").setValue(number);
                rre.child("patients").child(number).child("device_no").setValue(re[0]);
                Toast.makeText(option_list.this,"DEVICE SCANNED SUCESSFULLY ",Toast.LENGTH_SHORT).show();
                open_test(s,number);
                finalScannerView.stopCamera();
            }
        });
        setContentView(scannerView);
        scannerView.startCamera();


    }

    public void open_maps(String access,String num) {
        Intent intent = new Intent(this,MapsActivity.class);
        intent.putExtra(EXTRA_TEXT,access);
        intent.putExtra(EXTRA_NUMBER,num);
        startActivity(intent);
    }

    public void open_test(String condition,String number) {
        Intent intent = new Intent(this,tests.class);
        intent.putExtra(EXTRA_TEXT,condition);
        intent.putExtra(EXTRA_NUMBER,number);
        startActivity(intent);
    }
    @Override
    public void onLocationChanged(Location location) {
        double loca =   location.getLongitude();
        double lon  =   location.getLatitude();
        Lati = lon;
        Lon = loca;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
