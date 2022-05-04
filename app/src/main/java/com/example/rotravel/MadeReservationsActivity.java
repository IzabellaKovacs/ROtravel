package com.example.rotravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.rotravel.HelperClasses.AllPlacesToStayRecViewAdapter;
import com.example.rotravel.HelperClasses.MadeReservationsAdapter;
import com.example.rotravel.Model.Property;
import com.example.rotravel.Model.Reservation;
import com.example.rotravel.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MadeReservationsActivity extends AppCompatActivity {

    public static String PROPERTY_RESERVATIONS = "propertyReservations";

    private ImageView btnBack;
    private RecyclerView allMadeReservations;
    private DatabaseReference mDatabase;
    MadeReservationsAdapter adapter;

    Property property;
    ArrayList<Reservation> reservations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_reservations);

        property = getIntent().getParcelableExtra(PROPERTY_RESERVATIONS);

        btnBack = findViewById(R.id.btnBack);
        allMadeReservations = findViewById(R.id.allMadeReservations);

        btnBack.setOnClickListener(v -> onBackPressed());

        mDatabase = FirebaseDatabase.getInstance("https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Reservations");

        showAllMadeReservations();
    }

    private void showAllMadeReservations() {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Reservation reservation = dataSnapshot.getValue(Reservation.class);

                    assert reservation != null;
                    if(property.getId().equals(reservation.getIdProperty()))
                        reservations.add(reservation);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mDatabase.addValueEventListener(valueEventListener);

        allMadeReservations.setHasFixedSize(true);
        allMadeReservations.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new MadeReservationsAdapter(this, reservations);
        allMadeReservations.setAdapter(adapter);
    }


}