package com.example.ayushya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Temperature extends AppCompatActivity {
    public static final String EXTRA_TEXT ="com.example.application.example.EXTRA_TEXT";
    public static final String EXTRA_NUMBER ="com.example.application.example.EXTRA_NUMBER";
    String content;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        Intent intent = getIntent();
        final String number = intent.getStringExtra(option_list.EXTRA_TEXT);
        final TextView butto = (TextView) findViewById(R.id.switch_button);
        final TextView test_name = (TextView) findViewById(R.id.textView22);
        final DatabaseReference re;
        re = FirebaseDatabase.getInstance().getReference().child("patients").child(number);
        re.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String tes = dataSnapshot.child("current_test").getValue().toString();
                if(tes.equals("n/a")){
                    butto.setText("   Start   ");
                }
                else if(tes.equals("Done")){
                    butto.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), test_name.getText().toString()+" Done Successful", Toast.LENGTH_SHORT).show();
                    butto.setText("   NEXT   ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        butto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = butto.getText().toString();
                if(content.equals("   Start   ")){
                    re.child("current_test").setValue("Temperature");
                    Toast.makeText(getApplicationContext(), test_name.getText().toString()+" Started", Toast.LENGTH_SHORT).show();
                    butto.setVisibility(View.GONE);
                }
                else if(content.equals("   NEXT   ")){
                    re.child("current_test").setValue("n/a");
                    open_nest_test(number);
                }
            }
        });

    }

    public void open_nest_test(String number) {
        Intent intent = new Intent(this,cough_test.class);
        intent.putExtra(EXTRA_TEXT,number);
        startActivity(intent);
    }

}
