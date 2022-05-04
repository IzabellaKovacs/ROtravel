package com.example.rotravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.os.Bundle;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;



public class FindPlacesNearby extends AppCompatActivity implements
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private ChipGroup placesGroup;
    private ImageView btnBack;

    private GoogleMap mGoogleMap;
    private boolean permissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_places_nearby);

        btnBack = findViewById(R.id.btnBack);
        placesGroup = findViewById(R.id.placesGroup);
        initChips();

        btnBack.setOnClickListener(v -> onBackPressed());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.homeMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    public void initChips() {
        for (NearbyPlace nearbyPlace : AllConstants.nearbyPlacesName) {
            Chip chip = new Chip(this);
            chip.setText(nearbyPlace.getName());
            chip.setId(nearbyPlace.getId());
            chip.setPadding(8, 8, 8, 8);
            chip.setTextColor(getResources().getColor(R.color.blue));
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));
            chip.setChipIconTint(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
            chip.setChipIcon(ResourcesCompat.getDrawable(getResources(), nearbyPlace.getDrawableId(), null));
            chip.setCheckable(true);
            chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
            chip.setChipStrokeWidth(4);

            placesGroup.addView(chip);

        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
       mGoogleMap = googleMap;
       enableMyLocation();
       mGoogleMap.setOnMapClickListener(latLng -> {
           MarkerOptions markerOptions = new MarkerOptions();
           markerOptions.position(latLng);
           markerOptions.title(latLng.latitude + " : " + latLng.longitude);
           mGoogleMap.clear();
           mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
           mGoogleMap.addMarker(markerOptions);
       });

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