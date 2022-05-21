package com.example.rotravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.rotravel.HelperClasses.AppPermissions;
import com.example.rotravel.Model.Property;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class FindPlacesNearby extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {


    private ImageView btnBack;
    private MaterialButton save;
    private TextInputEditText txtSearchPlaceName;

    private boolean canAddMarker = false;
    private double lat;
    private double lng;
    private String name;

    private HashMap<Integer, Property> propertiesHashMap = new HashMap<>();

    private GoogleMap mGoogleMap;
    private boolean permissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_places_nearby);

        btnBack = findViewById(R.id.btnBack);
        save = findViewById(R.id.saveMarker);
        txtSearchPlaceName = findViewById(R.id.txtSearchPlaceName);

        if (getIntent() != null && getIntent().getExtras() != null) {
            canAddMarker = getIntent().getExtras().getBoolean("ADD_MARKER", false);
            if (!canAddMarker) {
                save.setVisibility(View.GONE);
            }
        } else save.setVisibility(View.GONE);

        if (getIntent() != null && getIntent().getExtras() != null) {
            lat = getIntent().getExtras().getDouble("LAT", 0);
            lng = getIntent().getExtras().getDouble("LNG", 0);
            name = getIntent().getExtras().getString("Desc");
        }

        btnBack.setOnClickListener(v -> onBackPressed());
        save.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("LAT", lat);
            intent.putExtra("LNG", lng);
            setResult(RESULT_OK, intent);
            finish();
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.homeMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
       mGoogleMap = googleMap;
       mGoogleMap.setOnMarkerClickListener(this);
       enableMyLocation();
       if (lat != 0 && lng != 0 && name != null) {
           mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16));
           MarkerOptions markerOptions = new MarkerOptions();
           markerOptions.position(new LatLng(lat, lng));
           markerOptions.title(name);
           mGoogleMap.clear();
           mGoogleMap.addMarker(markerOptions);
       } else if (!canAddMarker){
           getDeviceLocation();
       }

       if (canAddMarker) {
           txtSearchPlaceName.setVisibility(View.GONE);
           mGoogleMap.setOnMapClickListener(latLng -> {
               lat = latLng.latitude;
               lng = latLng.longitude;
               MarkerOptions markerOptions = new MarkerOptions();
               markerOptions.position(latLng);
               markerOptions.title(latLng.latitude + " : " + latLng.longitude);
               mGoogleMap.clear();
               mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
               mGoogleMap.addMarker(markerOptions);
           });
       }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(this);

        try {
            if (!permissionDenied) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            Location lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                getAllLocations(lastKnownLocation);
                            }
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void getAllLocations(Location lastKnownLocation) {
        FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Properties")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int i = 0;
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Property property = dataSnapshot.getValue(Property.class);

                            if(property == null) continue;

                            if(distance(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), property.getLat(), property.getLng()) < 10){
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(new LatLng(property.getLat(), property.getLng()));
                                markerOptions.title(property.getName());
                                Marker marker = mGoogleMap.addMarker(markerOptions);
                                marker.setTag(i);
                                propertiesHashMap.put(i, property);
                                i++;
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                  ) {
            if (mGoogleMap != null) {
                mGoogleMap.setMyLocationEnabled(true);
            }
        }else {
            AppPermissions.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
                return;
        }

        if (AppPermissions.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();
        } else {
            permissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        AppPermissions.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Property property = propertiesHashMap.get(marker.getTag());
        if(property != null){
            Intent intent = new Intent(FindPlacesNearby.this, PropertyActivity.class);
            intent.putExtra(PropertyActivity.PROPERTY, property);
            startActivity(intent);
        }

        return false;
    }
}