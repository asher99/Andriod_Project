package com.example.myapplication.controller.controller.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
    }

    public void orderRide(View v) {
        //get destination field
        EditText dest = (EditText)findViewById(R.id.myDestination);
        String destinationField = dest.getText().toString();
        //get email field
        EditText phoneNumberField = (EditText)findViewById(R.id.myPhoneNumber);
        //get phone number field
        EditText emailField = (EditText)findViewById(R.id.myEmail);
    }
}
