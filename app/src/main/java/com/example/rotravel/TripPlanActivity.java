package com.example.rotravel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.example.rotravel.HelperClasses.AllCitiesRecViewAdapter;
import com.example.rotravel.HelperClasses.BaseMenuActivity;
import com.example.rotravel.Model.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripPlanActivity extends BaseMenuActivity {

    private static final Object TAG = "mergi" ;
    private RecyclerView allPlaces;
    private AllCitiesRecViewAdapter adapter;
    private DatabaseReference mDatabse;
    ArrayList<Place> places = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allPlaces = findViewById(R.id.allPlaces);

        mDatabse = FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Places");

        showAllPlaces();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_trip_plan;
    }

    private void showAllPlaces(){
        ValueEventListener postListener = new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Place place = dataSnapshot.getValue(Place.class);
                    places.add(place);

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w((String) TAG, "loadPost:onCancelled", error.toException());
            }
        };

        mDatabse.addValueEventListener(postListener);

        allPlaces.setHasFixedSize(true);
        allPlaces.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new AllCitiesRecViewAdapter(this, places);
        allPlaces.setAdapter(adapter);
    }
}