package com.example.mobiletechapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class StreetViewActivity extends AppCompatActivity {

    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_street_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lat = extras.getDouble("latitude");
            lon = extras.getDouble("longitude");

            SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                    (SupportStreetViewPanoramaFragment)
                            getSupportFragmentManager().findFragmentById(R.id.streetView);
            streetViewPanoramaFragment.getStreetViewPanoramaAsync(
                    new OnStreetViewPanoramaReadyCallback() {
                        @Override
                        public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                            LatLng position = new LatLng(lat, lon);
                            panorama.setPosition(position);
                        }
                    });
        } else {
            SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                    (SupportStreetViewPanoramaFragment)
                            getSupportFragmentManager().findFragmentById(R.id.streetView);
            streetViewPanoramaFragment.getStreetViewPanoramaAsync(
                    new OnStreetViewPanoramaReadyCallback() {
                        @Override
                        public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                            LatLng uc = new LatLng(-35.2366, 149.0850);
                            panorama.setPosition(uc);
                        }
                    });
        }
    }
}