package com.example.myapplication.controller.controller.controller;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Activity {

    private EditText dest;
    private EditText phoneNumberField;
    private EditText emailField;
    private Button orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
     * @param v
     */
    public void orderRide(View v) {


    }

    /**
     * a method that allows to enable order button unless all needed information is entered
     */
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String userDestInput = dest.getText().toString().trim();
            String userPhoneInput = phoneNumberField.getText().toString().trim();
            String userEmailInput = emailField.getText().toString().trim();

            orderButton.setEnabled(!userDestInput.isEmpty() && (!userPhoneInput.isEmpty() || !userEmailInput.isEmpty()));

        }

        @Override
        public void afterTextChanged(Editable s) { }
    };
}
