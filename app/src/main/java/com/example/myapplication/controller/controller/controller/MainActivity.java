package com.example.myapplication.controller.controller.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;

import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
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

    private EditText dest;
    private String location = null;
    private EditText phoneNumberField;
    private EditText emailField;
    private Button orderButton;
    private Backend backend;

    Location locationA = new Location("A");

    // Acquire a reference to the system Location Manager
    LocationManager locationManager;


    // Define a listener that responds to location updates
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        backend = BackendFactory.getInstance();

        // watches for changes in order to enable the button
        dest.addTextChangedListener(loginTextWatcher);
        phoneNumberField.addTextChangedListener(loginTextWatcher);
        emailField.addTextChangedListener(loginTextWatcher);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

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
                locationA.setLatitude(place.getLatLng().latitude);
                locationA.setLongitude(place.getLatLng().longitude);

            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    private void findViews() {
        //get destination field
        dest = (EditText) findViewById(R.id.myDestination);

        //get email field
        phoneNumberField = (EditText) findViewById(R.id.myPhoneNumber);

        //get phone number field
        emailField = (EditText) findViewById(R.id.myEmail);

        orderButton = (Button) findViewById(R.id.button2);

        placeAutocompleteFragment1 = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment1);
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

            if (location == null)
                location = getPlace(getGpsLocation());

            Ride myRide = new Ride(destination, location, phone, email);

            backend.addRide(myRide, new Firebase_DBManager.Action<Long>() {
                @Override
                public void onSuccess(Long obj) {
                    Toast.makeText(getBaseContext(), "successfully sent a pickup request to" + location, Toast.LENGTH_LONG).show();
                    resetView();
                }

                @Override
                public void onFailure(Exception exception) {
                    Toast.makeText(getBaseContext(), "Error \n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                    //resetView();
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

    public String getPlace(Location location) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


            if (addresses.size() > 0) {
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                return stateName + "\n" + cityName + "\n" + countryName;
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
                Toast.makeText(this, "Until you grant the permission, we canot display the location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void resetView() {
        dest.setText("");
        phoneNumberField.setText("");
        emailField.setText("");
    }
}
