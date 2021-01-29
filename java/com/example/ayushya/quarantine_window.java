package com.example.ayushya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import org.w3c.dom.Text;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class quarantine_window extends AppCompatActivity {
    public static final String EXTRA_TEXT = "com.example.application.example.EXTRA_TEXT";
    String patient_id;
    String center;
    String name;
    String count;
    String nummmm;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quarantine_window);
        Intent intent = getIntent();
        String patient = intent.getStringExtra(option_list.EXTRA_TEXT);
        String patin = intent.getStringExtra(quarantine_window.EXTRA_TEXT);
        if (patin.isEmpty()) {
            patient_id = patient;
        } else if (!patin.isEmpty()) {
            patient_id = patin;
        }
        final TextView c1 = (TextView) findViewById(R.id.c1);
        final TextView c2 = (TextView) findViewById(R.id.c2);
        final TextView n1 = (TextView) findViewById(R.id.n1);
        final TextView n2 = (TextView) findViewById(R.id.n2);
        final TextView ty = (TextView) findViewById(R.id.textView46);
        final TextView name = (TextView) findViewById(R.id.center_name);
        final TextView qr = (TextView) findViewById(R.id.qr);
        final TextView tx1 = (TextView) findViewById(R.id.c1);
        final TextView tx2 = (TextView) findViewById(R.id.n1);
        final TextView tx3 = (TextView) findViewById(R.id.textView38);
        final TextView tx4 = (TextView) findViewById(R.id.textView39);
        final TextView tx5 = (TextView) findViewById(R.id.textView40);
        final EditText et = (EditText) findViewById(R.id.editText);
        final TableLayout ti = (TableLayout) findViewById(R.id.times);
        final TextView breakfast = (TextView) findViewById(R.id.reak);
        final TextView lunch = (TextView) findViewById(R.id.lu);
        final TextView dinner = (TextView) findViewById(R.id.din);

        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan_device(patient_id);
            }
        });
        final DatabaseReference re;
        re = FirebaseDatabase.getInstance().getReference();


        re.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                center = dataSnapshot.child("patients").child(patient_id).child("quarantine").child("center").getValue().toString();
                count = dataSnapshot.child("patients").child(patient_id).child("quarantine").child("count").getValue().toString();
                if (center.equals("n/a")) {
                    qr.setVisibility(View.VISIBLE);
                    tx1.setVisibility(View.GONE);
                    tx2.setVisibility(View.GONE);
                    tx3.setVisibility(View.GONE);
                    ti.setVisibility(View.GONE);
                    name.setText("Not Alloted");
                    flag = 0;
                } else if (!center.equals("n/a")) {
                    nummmm = dataSnapshot.child("quarantine_centers").child(center).child("name").getValue().toString();
                    String na1 = dataSnapshot.child("quarantine_centers").child(center).child("contact").child("1").child("name").getValue().toString();
                    String na2 = dataSnapshot.child("quarantine_centers").child(center).child("contact").child("2").child("name").getValue().toString();
                    String co1 = dataSnapshot.child("quarantine_centers").child(center).child("contact").child("1").child("num").getValue().toString();
                    String co2 = dataSnapshot.child("quarantine_centers").child(center).child("contact").child("2").child("num").getValue().toString();
                    String br = dataSnapshot.child("quarantine_centers").child(center).child("timings").child("break").getValue().toString();
                    String lu = dataSnapshot.child("quarantine_centers").child(center).child("timings").child("lunch").getValue().toString();
                    String di = dataSnapshot.child("quarantine_centers").child(center).child("timings").child("dinner").getValue().toString();

                    breakfast.setText(br);
                    lunch.setText(lu);
                    dinner.setText(di);
                    c1.setText(na1);
                    c2.setText(na2);
                    n1.setText(co1);
                    n2.setText(co2);
                    ti.setVisibility(View.VISIBLE);
                    ty.setVisibility(View.VISIBLE);
                    c1.setVisibility(View.VISIBLE);
                    c2.setVisibility(View.VISIBLE);
                    n1.setVisibility(View.VISIBLE);
                    n2.setVisibility(View.VISIBLE);
                    flag = 1;
                    et.setText(count);
                    qr.setVisibility(View.GONE);
                    tx1.setVisibility(View.VISIBLE);
                    tx2.setVisibility(View.VISIBLE);
                    tx3.setVisibility(View.VISIBLE);
                    name.setText(nummmm);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tx3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tx3.setVisibility(View.GONE);
                tx4.setVisibility(View.VISIBLE);
                tx5.setVisibility(View.VISIBLE);
                et.setVisibility(View.VISIBLE);
            }
        });
        tx5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = String.valueOf(et.getText());
                re.child("patients").child(patient_id).child("quarantine").child("count").setValue(count);
                tx3.setVisibility(View.VISIBLE);
                tx4.setVisibility(View.GONE);
                tx5.setVisibility(View.GONE);
                et.setVisibility(View.GONE);
            }
        });
        n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phone = new Intent(Intent.ACTION_CALL);
                phone.setData(Uri.parse("tel:" + n1.getText()));
                if (ActivityCompat.checkSelfPermission(quarantine_window.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(quarantine_window.this, new String[] {Manifest.permission.CALL_PHONE}, 4);
                    return;
                }
                startActivity(phone);
            }
        });
        n2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phone = new Intent(Intent.ACTION_CALL);
                phone.setData(Uri.parse("tel:"+n2.getText()));
                if (ActivityCompat.checkSelfPermission(quarantine_window.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(quarantine_window.this, new String[] {Manifest.permission.CALL_PHONE}, 4);
                    return;
                }
                startActivity(phone);
            }
        });
    }



    public void  scan_device(final String patient_id) {
        final DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("patients").child(patient_id).child("quarantine");
        ZXingScannerView scannerView = new ZXingScannerView(this);
        scannerView = new ZXingScannerView(this);
        final ZXingScannerView finalScannerView = scannerView;
        scannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                String code = result.getText();
                ref.child("center").setValue(code);
                Toast.makeText(quarantine_window.this," CODE SCANNED SUCESSFULLY ",Toast.LENGTH_SHORT).show();
                sendx(patient_id);
                finalScannerView.stopCamera();
            }
        });
        setContentView(scannerView);
        scannerView.startCamera();


    }

    public void sendx(String patient_id) {
        Intent intent = new Intent(this,quarantine_window.class);
        intent.putExtra(EXTRA_TEXT,patient_id);
        startActivity(intent);
    }
}
