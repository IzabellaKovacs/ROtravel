package com.example.rotravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rotravel.HelperClasses.AllConstants;
import com.example.rotravel.HelperClasses.AppPermissions;
import com.example.rotravel.Model.NearbyPlace;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;



public class FindPlacesNearby extends AppCompatActivity implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private ImageView btnBack;
    private MaterialButton save;

    private boolean canAddMarker = false;
    private double lat;
    private double lng;

    private GoogleMap mGoogleMap;
    private boolean permissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_places_nearby);

        if (getIntent() != null && getIntent().getExtras() != null ) {
            canAddMarker = getIntent().getExtras().getBoolean("ADD_MARKER", false);
            if (!canAddMarker) {
                save.setVisibility(View.GONE);
            }
        }
        if (getIntent() != null && getIntent().getExtras() != null) {
            lat = getIntent().getExtras().getDouble("LAT", 0);
            lat = getIntent().getExtras().getDouble("LNG", 0);
        }

        btnBack = findViewById(R.id.btnBack);
        save = findViewById(R.id.saveMarker);

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
       enableMyLocation();
       if (lat != 0 && lng != 0) {
           mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16));
       }
       if (canAddMarker) {
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
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                  ) {
            if (mGoogleMap != null) {
                mGoogleMap.setMyLocationEnabled(true);
            }
        }else {
            // Permission to access the location is missing. Show rationale and request permission
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
}