package com.example.ayushya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class add_account extends AppCompatActivity {
    String phone_num[] = new String[9999];
    int count = 0,flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        final EditText name = (EditText) findViewById(R.id.editText3);
        final EditText phone = (EditText) findViewById(R.id.editText2);
        final EditText pin = (EditText) findViewById(R.id.editText4);
        final EditText repin = (EditText) findViewById(R.id.editText5);
        final TextView load = (TextView) findViewById(R.id.textView50);
        final ImageView submit = (ImageView) findViewById(R.id.imageView);
        final DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference().child("patients");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = (int) dataSnapshot.getChildrenCount();
                for(int i = 0;i<count;i++){
                    phone_num[i] = dataSnapshot.child(String.valueOf(i+1)).child("mobile").getValue().toString(); // Load all phone numbers
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<count;i++){
                    if(phone_num[i].equals(phone.getText().toString())){
                        flag = 1;
                        phone.setText("");
                        Toast.makeText(getApplicationContext(), "Account Already Exists", Toast.LENGTH_LONG).show();
                        break;
                    }
                    else {
                        flag=2;
                    }
                }
                if(flag==2){
                    if(repin.getText().toString().equals(pin.getText().toString())){
                        submit.setVisibility(View.GONE);
                        load.setVisibility(View.VISIBLE);
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int coun = (int) dataSnapshot.getChildrenCount();
                                ref.child(String.valueOf(coun + 1)).child("mobile").setValue(phone.getText().toString());
                                ref.child(String.valueOf(coun + 1)).child("password").setValue(repin.getText().toString());
                                ref.child(String.valueOf(coun + 1)).child("name").setValue(name.getText().toString());
                                ref.child(String.valueOf(coun + 1)).child("disease").setValue("n/a");
                                ref.child(String.valueOf(coun + 1)).child("current_test").setValue("n/a");
                                ref.child(String.valueOf(coun + 1)).child("device_no").setValue("n/a");
                                ref.child(String.valueOf(coun + 1)).child("location").child("lat").setValue("o/f");
                                ref.child(String.valueOf(coun + 1)).child("location").child("long").setValue("o/f");
                                ref.child(String.valueOf(coun + 1)).child("quarantine").child("center").setValue("n/a");
                                ref.child(String.valueOf(coun + 1)).child("quarantine").child("count").setValue("1");
                                ref.child(String.valueOf(coun + 1)).child("quarantine").child("status").setValue("no");
                                ref.child(String.valueOf(coun + 1)).child("specific_test").child("name").setValue("n/a");
                                ref.child(String.valueOf(coun + 1)).child("specific_test").child("value").setValue("");
                                ref.child(String.valueOf(coun + 1)).child("tests").child("coughi").setValue("n/a");
                                ref.child(String.valueOf(coun + 1)).child("tests").child("cought").setValue("n/a");
                                ref.child(String.valueOf(coun + 1)).child("tests").child("date").setValue("n/a");
                                ref.child(String.valueOf(coun + 1)).child("tests").child("pulse").setValue("0");
                                ref.child(String.valueOf(coun + 1)).child("tests").child("temperature").setValue("0.0");



                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 5s = 5000ms
                                        Toast.makeText(getApplicationContext(), "Account Created ", Toast.LENGTH_LONG).show();
                                       // open_acc();
                                    }
                                }, 5000);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    else{
                        repin.setText("");
                        Toast.makeText(getApplicationContext(), "PIN Doesn't match ", Toast.LENGTH_LONG).show();
                    }
                }
                }


        });

    }
    public void open_acc() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
