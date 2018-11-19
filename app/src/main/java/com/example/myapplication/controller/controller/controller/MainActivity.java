package com.example.myapplication.controller.controller.controller;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.controller.controller.model.datasource.Firebase_DBManager;
import com.example.myapplication.controller.controller.model.entities.Ride;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Activity {

    private EditText dest;
    private Location location;
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
     *
     * @param v
     */
    public void orderRide(View v) throws Exception {

        try {
            String destination = dest.getText().toString();
            Long phone = Long.valueOf(phoneNumberField.getText().toString());
            String email = emailField.getText().toString();
            String location = "s";//getLocation();
            
            Ride myRide = new Ride(destination, location, phone, email);

            Firebase_DBManager.addRide(myRide, new Firebase_DBManager.Action<Long>() {
                @Override
                public void onSuccess(Long obj) {
                    Toast.makeText(getBaseContext(), "successfully sent a pickup request " + obj, Toast.LENGTH_LONG).show();
                    //  resetView();
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getBaseContext(), "Error \n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                    //  resetView();
                }

                @Override
                public void onProgress(String status, double percent) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error ", Toast.LENGTH_LONG).show();
        }
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

            // the information field of destination and phone number must be correct inorder to send a request
            orderButton.setEnabled(!userDestInput.isEmpty() && !userPhoneInput.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
