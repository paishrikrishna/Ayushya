package com.example.ayushya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class login extends AppCompatActivity {
    public static final String EXTRA_TEXT ="com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_NUMBER ="com.example.application.example.EXTRA_NUMBER";
    int flag =2;
    String resul;
    String fm[] = new String[9999];
    String fp[] = new String[9999];
    int count,i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        final String option = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        final TextView username = (TextView) findViewById(R.id.username);
        final TextView password =(TextView) findViewById(R.id.password);
        TextView quart = (TextView) findViewById(R.id.quarantine);
        TextView add_acc = (TextView) findViewById(R.id.add_acc);
        if(option.equals("doctors")){
            quart.setVisibility(View.VISIBLE);
            add_acc.setVisibility(View.GONE);
        }
        else{
            quart.setVisibility(View.GONE);
            add_acc.setVisibility(View.VISIBLE);
        }
        quart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                op();
            }
        });
        ImageView loginb = (ImageView) findViewById(R.id.login);
        final DatabaseReference tr;
        tr = FirebaseDatabase.getInstance().getReference().child(option);

        loginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        count = (int) dataSnapshot.getChildrenCount();
                        for( i=1;i<=count;i++) {
                            fm[i] = dataSnapshot.child(String.valueOf(i)).child("mobile").getValue().toString();
                            fp[i] = dataSnapshot.child(String.valueOf(i)).child("password").getValue().toString();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                final String un = username.getText().toString();
                final String pw = password.getText().toString();
                for (i = 1; i <= count; i++) {

                    if (fm[i].equals(un) && fp[i].equals(pw)) {
                        flag = 0;
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        open_next(String.valueOf(i), option);
                        break;
                    } else {
                        flag = 1;
                    }

                }
                if (flag == 1) {
                    Toast.makeText(getApplicationContext(), "Either your No. or PIN is Incorrect", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    flag = 0;
                }
            }
        });
        add_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_acc();
            }
        });

    }

    public void open_acc() {
        Intent intent = new Intent(this,add_account.class);
        startActivity(intent);
    }

    public void op() {

        ZXingScannerView scannerView = new ZXingScannerView(this);
        scannerView = new ZXingScannerView(this);
        final ZXingScannerView finalScannerView = scannerView;
        scannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
               String  resl = result.getText();
                //setContentView(R.layout.activity_quarantine_login);

                Toast.makeText(login.this," Enter PIN ",Toast.LENGTH_SHORT).show();
                iu(resl);
                finalScannerView.stopCamera();
            }
        });
        setContentView(scannerView);
        scannerView.startCamera();

    }
    public void iu(String resl){
        final Intent intent = new Intent(this, quarantine_login.class);
        intent.putExtra(EXTRA_TEXT,resl);
        startActivity(intent);
    }



            public void open_next(String un, String option) {
                Intent intent = new Intent(this, option_list.class);
                intent.putExtra(EXTRA_TEXT, un);
                intent.putExtra(EXTRA_NUMBER, option);
                startActivity(intent);
            }

}