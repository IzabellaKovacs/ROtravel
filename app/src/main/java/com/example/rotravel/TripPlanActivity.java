package com.example.rotravel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rotravel.HelperClasses.AllCitiesRecViewAdapter;
import com.example.rotravel.HelperClasses.BaseMenuActivity;
import com.example.rotravel.Model.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripPlanActivity extends BaseMenuActivity {

    private RecyclerView allPlaces;
    ImageButton btnSearch;
    EditText txtSearch;

    private static final Object TAG = "mergi" ;
    private AllCitiesRecViewAdapter adapter;
    private DatabaseReference mDatabse;
    private DataSnapshot placesDataSnapshot;
    ArrayList<Place> places = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allPlaces = findViewById(R.id.allPlaces);
        btnSearch = findViewById(R.id.btnSearch);
        txtSearch = findViewById(R.id.txtSearch);

        mDatabse = FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Places");

        showAllPlaces();

        btnSearch.setOnClickListener(v -> {
            String name = txtSearch.getText().toString();
            firebasePlacesSearch(name);
        });
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_trip_plan;
    }

    private void showAllPlaces(){
        ValueEventListener postListener = new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                placesDataSnapshot = snapshot;
                firebasePlacesSearch("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w((String) TAG, "loadPost:onCancelled", error.toException());
            }
        };


        mDatabse.addValueEventListener(postListener);

        allPlaces.setHasFixedSize(true);
        allPlaces.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new AllCitiesRecViewAdapter(this, places, place -> {
            Intent intent = new Intent(TripPlanActivity.this, TripReserveActivity.class);
            intent.putExtra(TripReserveActivity.TRIP, place);
            startActivity(intent);
        });
        allPlaces.setAdapter(adapter);
    }

    private void firebasePlacesSearch(String name) {
        places.clear();
        for (DataSnapshot dataSnapshot : placesDataSnapshot.getChildren()) {
            Place place = dataSnapshot.getValue(Place.class);
            if (name.isEmpty()) {
                places.add(place);
            } else {
                if (place.getName().startsWith(name)) {
                    places.add(place);
                }
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}