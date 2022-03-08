package com.example.rotravel;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotravel.HelperClasses.AllPlacesToStayRecViewAdapter;
import com.example.rotravel.Model.Property;
import com.example.rotravel.Model.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

public class TripReserveActivity extends AppCompatActivity {
    public static String TRIP = "trip";

    ImageView btnBack;
    ImageView imgView;
    TextView txtPlace, choose;

    RecyclerView allPlacesToStay;
    private DatabaseReference mDatabse;
    AllPlacesToStayRecViewAdapter adapter;
    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_reserve);

        place = getIntent().getParcelableExtra(TRIP);

        btnBack = findViewById(R.id.btnBack);
        imgView = findViewById(R.id.imgView);
        txtPlace = findViewById(R.id.txtPlace);
        choose = findViewById(R.id.choose);
        allPlacesToStay = findViewById(R.id.allPlacesToStay);

        btnBack.setOnClickListener(v -> onBackPressed());

        mDatabse = FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Properties");

        showAllPlacesToStay();

        Picasso.get().load(place.getImage()).into(imgView);
        txtPlace.setText(place.getName());

    }

    private void showAllPlacesToStay() {
        ArrayList<Property> properties = new ArrayList<>();
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Property property = dataSnapshot.getValue(Property.class);
                    assert property != null;
                    if(property.getIdPlace().equals(place.getId()))
                        properties.add(property);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }
        };

        mDatabse.addValueEventListener(postListener);

        allPlacesToStay.setHasFixedSize(true);
        allPlacesToStay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new AllPlacesToStayRecViewAdapter(this, properties, property -> {
            Intent intent = new Intent(TripReserveActivity.this, PropertyActivity.class);
            intent.putExtra(PropertyActivity.PROPERTY, property);
            startActivity(intent);
        });
        allPlacesToStay.setAdapter(adapter);
    }

}