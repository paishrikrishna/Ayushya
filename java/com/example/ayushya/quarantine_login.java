package com.example.ayushya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class quarantine_login extends AppCompatActivity {
    String resul;
    String pas;
    AlertDialog dialog;
    int count=0,flag = 0,i=0,total=0;
    String passw[] = new String[9999];
    String usn[] = new String[9999];

    int tp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quarantine_login);
        Intent intent = getIntent();
        final String Center_name = intent.getStringExtra(login.EXTRA_TEXT);
        final TextView tx1 = (TextView) findViewById(R.id.textView44);

        final TextView tx2 = (TextView) findViewById(R.id.textView45);
        final TextView tn1 = (TextView) findViewById(R.id.n1);
        final TextView tn2 = (TextView) findViewById(R.id.n2);
        final TextView to1 = (TextView) findViewById(R.id.o1);
        final TextView to2 = (TextView) findViewById(R.id.o2);
        final TextView rr = (TextView) findViewById(R.id.textView35);
        final TextView pin = (TextView) findViewById(R.id.pasw);
        final TextView lo = (TextView) findViewById(R.id.done);
        final TableLayout ti = (TableLayout) findViewById(R.id.times);
        final TextView breakfast = (TextView) findViewById(R.id.reak);
        final TextView lunch = (TextView) findViewById(R.id.lu);
        final TextView dinner = (TextView) findViewById(R.id.din);
        final TextView tx = (TextView) findViewById(R.id.textView41);
        final TextView coun = (TextView) findViewById(R.id.count);
        final DatabaseReference tr;
        tr = FirebaseDatabase.getInstance().getReference().child("quarantine_centers");
        tr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resul = dataSnapshot.child(Center_name).child("name").getValue().toString();
                count = (int) dataSnapshot.getChildrenCount();
                for( i=1;i<=count;i++) {
                    usn[i] = dataSnapshot.child(String.valueOf(i)).child("name").getValue().toString();
                    passw[i] = dataSnapshot.child(String.valueOf(i)).child("password").getValue().toString();
                }
                String cn1 = dataSnapshot.child(Center_name).child("contact").child("1").child("name").getValue().toString();
                String co1 = dataSnapshot.child(Center_name).child("contact").child("1").child("num").getValue().toString();
                String cn2 = dataSnapshot.child(Center_name).child("contact").child("2").child("name").getValue().toString();
                String co2 = dataSnapshot.child(Center_name).child("contact").child("2").child("num").getValue().toString();
                String br = dataSnapshot.child(Center_name).child("timings").child("break").getValue().toString();
                String lu = dataSnapshot.child(Center_name).child("timings").child("lunch").getValue().toString();
                String di = dataSnapshot.child(Center_name).child("timings").child("dinner").getValue().toString();
                tn1.setText(cn1);
                to1.setText(co1);
                tn2.setText(cn2);
                to2.setText(co2);
                breakfast.setText(br);
                lunch.setText(lu);
                dinner.setText(di);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_dialog(breakfast,Center_name);
            }
        });
        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_dialog(lunch,Center_name);
            }
        });
        dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_dialog(dinner,Center_name);
            }
        });
        tn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                open_dialog(tn1,Center_name);
            }
        });
        tn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                open_dialog(tn2,Center_name);
            }
        });
        to1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                open_dialog(to1,Center_name);
            }
        });
        to2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                open_dialog(to2,Center_name);
            }
        });

             lo.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     pas = pin.getText().toString();
                     for( i = 1;i<=count;i++){
                         if(usn[i].equals(resul)&&passw[i].equals(pas)){
                             flag=1;
                             Toast.makeText(quarantine_login.this," CORRECT PIN ",Toast.LENGTH_SHORT).show();
                             rr.setVisibility(View.GONE);
                             pin.setVisibility(View.GONE);
                             lo.setVisibility(View.GONE);
                             tx.setVisibility(View.VISIBLE);
                             coun.setVisibility(View.VISIBLE);
                             tx1.setVisibility(View.VISIBLE);
                             ti.setVisibility(View.VISIBLE);
                             tx2.setVisibility(View.VISIBLE);
                             tn1.setVisibility(View.VISIBLE);
                             to1.setVisibility(View.VISIBLE);
                             tn2.setVisibility(View.VISIBLE);
                             to2.setVisibility(View.VISIBLE);

                             break;
                         }
                         else{
                             flag=0;
                         }
                     }
                     if(flag==0){
                         Toast.makeText(quarantine_login.this," WRONG PIN ",Toast.LENGTH_SHORT).show();
                     }

                 }

             });

        final DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("patients");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tp = 0;
                total = (int) dataSnapshot.getChildrenCount();
                for(i=1;i<=total;i++){
                    String cent = dataSnapshot.child(String.valueOf(i)).child("quarantine").child("center").getValue().toString();
                    if(cent.equals(Center_name)){
                        tp = tp + Integer.parseInt(dataSnapshot.child(String.valueOf(i)).child("quarantine").child("count").getValue().toString());
                    }
                }
                coun.setText(String.valueOf(tp));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void open_dialog(final TextView textView, final String Cent) {
        final TextView tn1 = (TextView) findViewById(R.id.n1);
        final TextView tn2 = (TextView) findViewById(R.id.n2);
        final TextView to1 = (TextView) findViewById(R.id.o1);
        final TextView to2 = (TextView) findViewById(R.id.o2);
        final TextView breakfast = (TextView) findViewById(R.id.reak);
        final TextView lunch = (TextView) findViewById(R.id.lu);
        final TextView dinner = (TextView) findViewById(R.id.din);
        final EditText ety = new EditText(this);
        ety.setText(textView.getText());
    dialog = new AlertDialog.Builder(this).create();
    dialog.setTitle("Enter Value to Edit");
    dialog.setView(ety);
    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "DONE", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            textView.setText(ety.getText());
            final DatabaseReference rey;
            rey = FirebaseDatabase.getInstance().getReference().child("quarantine_centers").child(Cent);
            rey.child("contact").child("1").child("name").setValue(tn1.getText().toString());
            rey.child("contact").child("1").child("num").setValue(to1.getText().toString());
            rey.child("contact").child("2").child("name").setValue(tn2.getText().toString());
            rey.child("contact").child("2").child("num").setValue(to2.getText().toString());
            rey.child("timings").child("break").setValue(breakfast.getText().toString());
            rey.child("timings").child("lunch").setValue(lunch.getText().toString());
            rey.child("timings").child("dinner").setValue(dinner.getText().toString());
        }
    });
        dialog.show();

    }


}
