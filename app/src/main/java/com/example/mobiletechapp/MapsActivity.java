package com.example.mobiletechapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mobiletechapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    MyLocationPlaceMap myLocationsPlaces;
    boolean isClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myLocationsPlaces = new MyLocationPlaceMap(getApplicationContext(),
                MapsActivity.this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if (isClicked) {
                    showDirection(findViewById(R.id.buttonDirection));
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            double lat = extras.getDouble("latitude");
            double lon = extras.getDouble("longitude");
            String addr = extras.getString("address");

            LatLng myPlace = new LatLng(lat, lon);
            mMap.addMarker(new MarkerOptions().position(myPlace).title(addr));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 22));
        }

        myLocationsPlaces.getNearbyPlaces(mMap, "AIzaSyBPB6F8zdJ0KVX6J7LJkMvrtK56farfOss");

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Nullable
            @Override
            public View getInfoContents(@NonNull Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
                TextView title = infoWindow.findViewById(R.id.textViewTitle);
                TextView snippet = infoWindow.findViewById(R.id.textViewSnippet);
                ImageView image = infoWindow.findViewById(R.id.imageView);
                if (marker.getTitle() != null && marker.getSnippet() != null) {
                    title.setText(marker.getTitle());
                    snippet.setText(marker.getSnippet());
                } else {
                    title.setText("No info available");
                    snippet.setText("No info available");
                }
                image.setImageDrawable(getResources().getDrawable(R.mipmap.goalie, getTheme()));
                return infoWindow;

            }

            @Nullable
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                return null;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                LatLng coord  = marker.getPosition();
                changeToStreetView(coord);
                return false;
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the current state
        outState.putBoolean("button_clicked", isClicked);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the saved state
        isClicked = savedInstanceState.getBoolean("button_clicked");
    }

    public void changeToStreetView(LatLng coordinates) {
        Intent intent = new Intent(this, StreetViewActivity.class);
        intent.putExtra("latitude", coordinates.latitude);
        intent.putExtra("longitude", coordinates.longitude);
        startActivity(intent);
    }

    public void showDirection(View view) {
        isClicked = true;
        LatLng canberraCentre = new LatLng(-35.279065, 149.133239);
        LatLng ucBuilding6 = new LatLng(-35.2366541,149.08422);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(canberraCentre, 13));
        drawRoute(canberraCentre, ucBuilding6);
    }

    public void drawRoute(LatLng origin, LatLng destination) {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + origin.latitude + "," + origin.longitude + "&destination="
                + destination.latitude + "," + destination.longitude
                + "&mode=driving&key=AIzaSyBPB6F8zdJ0KVX6J7LJkMvrtK56farfOss";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse the JSON response and draw the route on the map
                        PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.BLACK);
                        polylineOptions.width(5);
                        String distance = "", duration = "";
                        JSONArray routes = null;
                        try {
                            routes = response.getJSONArray("routes");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        for (int i = 0; i < routes.length(); i++) {
                            try {
                                JSONObject route = routes.getJSONObject(i);
                                JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                                String points = overviewPolyline.getString("points");
                                List<LatLng> path = PolyUtil.decode(points);
                                polylineOptions.addAll(path);

                                distance = route.getJSONArray("legs")
                                        .getJSONObject(0)
                                        .getJSONObject("distance")
                                        .getString("text");

                                duration = route.getJSONArray("legs")
                                        .getJSONObject(0)
                                        .getJSONObject("duration")
                                        .getString("text");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        TextView disDur = findViewById(R.id.textViewDistanceDuration);
                        disDur.setText("Distance: " + distance + " Duration: "
                                + duration);
                        mMap.addPolyline(polylineOptions);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

}