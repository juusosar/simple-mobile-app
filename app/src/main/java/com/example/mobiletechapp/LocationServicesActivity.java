package com.example.mobiletechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;


public class LocationServicesActivity extends AppCompatActivity {

    MyLocationPlaceMap myLocationPlaceMap;
    ArrayList<MyLocationPlace> myLocations = new ArrayList<>();
    MyLocationPlace myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_services);

        myLocationPlaceMap = new MyLocationPlaceMap(getApplicationContext(),
        LocationServicesActivity.this);
        myLocationPlaceMap.requestPermissions();
        myLocationPlaceMap.getLatLngAddress(myLocations);
    }

    public void showCurrentLocation(View view) {
        myLocationPlaceMap.getLatLngAddress(myLocations);
        TextView tvlat = findViewById(R.id.textViewLatitude);
        TextView tvlng = findViewById(R.id.textViewLongitude);
        TextView tvaddr = findViewById(R.id.textViewStreetAddress);
        if (myLocations.size() > 0) {
            myLocation = myLocations.get(0);
            myLocations.clear();
            tvlat.setText("Latitude: " + myLocation.getLatitude());
            tvlng.setText("Longitude: " + myLocation.getLongitude());
            tvaddr.setText("Address: " + myLocation.getAddress());
        }
    }

    public void showPlacesOnMap(View view) {
        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude();
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        intent.putExtra("address", myLocation.getAddress());
        startActivity(intent);
    }
}