package com.example.myapplication.controller.controller.controller;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;

import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import com.example.myapplication.R;
import com.example.myapplication.controller.controller.model.backend.Backend;
import com.example.myapplication.controller.controller.model.backend.BackendFactory;
import com.example.myapplication.controller.controller.model.datasource.Firebase_DBManager;
import com.example.myapplication.controller.controller.model.entities.Ride;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    private PlaceAutocompleteFragment placeAutocompleteFragment1;

    private EditText userName;
    private EditText dest;
    private String location = null;
    private EditText phoneNumberField;
    private EditText emailField;
    private Button orderButton;
    private Backend backend;

    // Acquire a reference to the system Location Manager
    LocationManager locationManager;


    // Define a listener that responds to location updates
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        backend = BackendFactory.getInstance();

        // watches for changes in order to enable the button
        userName.addTextChangedListener(loginTextWatcher);
        dest.addTextChangedListener(loginTextWatcher);
        phoneNumberField.addTextChangedListener(loginTextWatcher);
        emailField.addTextChangedListener(loginTextWatcher);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
        }

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location locat) {
                location = getPlace(locat);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        placeAutocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Location target = new Location("target");
                target.setLatitude(place.getLatLng().latitude);
                target.setLongitude(place.getLatLng().longitude);
                String destination = getPlace(target);
                if (destination.equals("IOException ...")) {
                    Toast.makeText(getBaseContext(), "the destination is not recognized \n\t please try again", Toast.LENGTH_LONG).show();
                } else {
                    dest.setText(destination);
                }
            }

            @Override
            public void onError(Status status) {

            }
        });

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                FragmentManager fm = getFragmentManager();
                Fragment fragment = (fm.findFragmentById(R.id.place_autocomplete_fragment1));
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(fragment);
                ft.commit();
            }
        });
    }

    private void findViews() {
        //get name field
        userName = (EditText) findViewById(R.id.myName);

        //get destination field
        dest = (EditText) findViewById(R.id.myDestination);

        //get email field
        phoneNumberField = (EditText) findViewById(R.id.myPhoneNumber);

        //get phone number field
        emailField = (EditText) findViewById(R.id.myEmail);

        orderButton = (Button) findViewById(R.id.button2);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFieldsInput(v);
            }
        });
        placeAutocompleteFragment1 = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment1);
        ;
    }

    /**
     * executed at click of the order button
     * enters the order to the database
     *
     * @param v
     */
    public void orderRide(View v) throws Exception {
        try {
            String name = userName.getText().toString();
            String destination = dest.getText().toString();
            String phone = phoneNumberField.getText().toString();
            String email = emailField.getText().toString();

            if (location == null)
                location = getPlace(getGpsLocation());

            if (location.equals("IOException ...")) {
                throw new Exception("cannot find your location please try later");
            }
            Ride myRide = new Ride(name, destination, location, phone, email);
            backend.addRide(myRide, new Backend.Action() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getBaseContext(), "successfully sent a pickup request to" + location, Toast.LENGTH_LONG).show();
                    resetView();
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getBaseContext(), "Error \n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onProgress(String status, double percent) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error \n" + e.getMessage(), Toast.LENGTH_LONG).show();
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

            String userNameInput = userName.getText().toString().trim();
            String userDestInput = dest.getText().toString().trim();
            String userPhoneInput = phoneNumberField.getText().toString().trim();
            String userEmailInput = emailField.getText().toString().trim();

            // the information field of destination and phone number must be correct inorder to send a request
            orderButton.setEnabled(!userDestInput.isEmpty() && !userPhoneInput.isEmpty()
                    && !userNameInput.isEmpty() && !userEmailInput.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    private Location getGpsLocation() {
        //     Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        return locationManager.getLastKnownLocation(locationManager.PASSIVE_PROVIDER);
    }

    /**
     * gets a gps location object and returns a string of name of place.
     *
     * @param location
     * @return
     */
    public String getPlace(Location location) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


            if (addresses.size() > 0) {
                String cityName = addresses.get(0).getAddressLine(0);

                return cityName;
            }

            return "no place: \n (" + location.getLongitude() + " , " + location.getLatitude() + ")";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "IOException ...";
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            } else {
                Toast.makeText(this, "Until you grant the permission, we cannot display the location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkFieldsInput(View v) {
        // Reset errors.
        userName.setError(null);
        phoneNumberField.setError(null);
        emailField.setError(null);

        // Store values at the time of the pickup attempt.
        String name = userName.getText().toString();
        String phone = phoneNumberField.getText().toString();
        String email = emailField.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // check that name field has a value
        if (TextUtils.isEmpty(name)) {
            userName.setError("please enter a user name");
            focusView = userName;
            cancel = true;
        }

        // Check for a valid phone number.
        if (!TextUtils.isEmpty(phone) && !isPhoneValid(phone)) {
            phoneNumberField.setError("please enter a valid phone number");
            focusView = phoneNumberField;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailField.setError("please enter an email address");
            focusView = emailField;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailField.setError("email address is not valid");
            focusView = emailField;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt pickup and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            try {
                orderRide(v);
            } catch (Exception e) {
            }
        }
    }

    private boolean isEmailValid(String email) {
        String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(EMAIL_REGEX);
    }

    private boolean isPhoneValid(String phone) {
        return (phone.length() == 10 && phone.startsWith("05"));
    }

    private void resetView() {
        userName.setText("");
        dest.setText("");
        phoneNumberField.setText("");
        emailField.setText("");
    }
}