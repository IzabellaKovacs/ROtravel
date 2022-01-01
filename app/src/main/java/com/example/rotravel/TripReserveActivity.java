package com.example.rotravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotravel.HelperClasses.AllCitiesRecViewAdapter;
import com.example.rotravel.HelperClasses.AllPlacesToStayRecViewAdapter;
import com.example.rotravel.HelperClasses.BaseMenuActivity;
import com.example.rotravel.Model.Hotel;
import com.example.rotravel.Model.Place;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

public class TripReserveActivity extends AppCompatActivity {
    public static String TRIP = "trip";

    ImageView btnBack;
    ImageView imgView;
    TextView txtPlace, choose;
    RecyclerView allPlacesToStay;
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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        showAllPlacesToStay();

        Picasso.get().load(place.getImage()).into(imgView);
        txtPlace.setText(place.getName());

        UUID.randomUUID().toString()
    }


    private void showAllPlacesToStay() {
        ArrayList<Hotel> hotels = new ArrayList<>();
        hotels.add(new Hotel(R.drawable.tm, "Hotel Timisoara"));
        hotels.add(new Hotel(R.drawable.tm, "Hotel Timisoara"));
        hotels.add(new Hotel(R.drawable.tm, "Hotel Timisoara"));
        hotels.add(new Hotel(R.drawable.tm, "Hotel Timisoara"));
        hotels.add(new Hotel(R.drawable.tm, "Hotel Timisoara"));
        hotels.add(new Hotel(R.drawable.tm, "Hotel Timisoara"));



        allPlacesToStay.setHasFixedSize(true);
        allPlacesToStay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new AllPlacesToStayRecViewAdapter(this, hotels);
        allPlacesToStay.setAdapter(adapter);
    }

}