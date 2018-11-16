package com.example.myapplication.controller.controller.controller;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends Activity {

    private EditText dest;
    private EditText phoneNumberField;
    private EditText emailField;
    private Button orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
/*
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                statusText.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                statusText.setText("Failed to read value.");
            }
        });

        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mStorageRef.child("temp").child("myFile.abc").putFile(uri);
*/
        setContentView(R.layout.activity_main);

        //get destination field
        dest = (EditText) findViewById(R.id.myDestination);
        String destinationField = dest.getText().toString();

        //get email field
        phoneNumberField = (EditText) findViewById(R.id.myPhoneNumber);

        //get phone number field
        emailField = (EditText) findViewById(R.id.myEmail);

        orderButton = (Button) findViewById(R.id.button2);

        // watches for changes in order to enable the button
        dest.addTextChangedListener(loginTextWatcher);
        phoneNumberField.addTextChangedListener(loginTextWatcher);
        emailField.addTextChangedListener(loginTextWatcher);


    }

    /**
     * executed at click of the order button
     * enters the order to the database
     *
     * @param v
     */
    public void orderRide(View v) {


    }

    /**
     * a method that allows to enable order button unless all needed information is entered
     */
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String userDestInput = dest.getText().toString().trim();
            String userPhoneInput = phoneNumberField.getText().toString().trim();
            String userEmailInput = emailField.getText().toString().trim();

            orderButton.setEnabled(!userDestInput.isEmpty() && (!userPhoneInput.isEmpty() || !userEmailInput.isEmpty()));

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
