package com.example.rotravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.rotravel.HelperClasses.AcceptedReservationsAdapter;
import com.example.rotravel.HelperClasses.MadeReservationsAdapter;
import com.example.rotravel.Model.AcceptedReservation;
import com.example.rotravel.Model.Property;
import com.example.rotravel.Model.Reservation;
import com.example.rotravel.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class AcceptedReservationsActivity extends AppCompatActivity {

    public static final String PROPERTY_ACCEPTED_RESERVATIONS = "accepted";

    // Init widgets
    private ImageView btnBack;
    RecyclerView allAcceptedReservations;
    private DatabaseReference mDatabase;

    private AcceptedReservationsAdapter adapter;

    Property property;
    private final ArrayList<AcceptedReservation> reservations = new ArrayList<>();
    private final ArrayList<User> users = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_reservations);

        btnBack = findViewById(R.id.btnBack);
        allAcceptedReservations = findViewById(R.id.allAcceptedReservations);

        property = getIntent().getParcelableExtra(PROPERTY_ACCEPTED_RESERVATIONS);

        btnBack.setOnClickListener(v -> onBackPressed());

        mDatabase = FirebaseDatabase.getInstance("https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("AcceptedReservations");

        showAllAcceptedReservations();
    }

    private void showAllAcceptedReservations() {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reservations.clear();
                users.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AcceptedReservation reservation = dataSnapshot.getValue(AcceptedReservation.class);

                    assert reservation != null;
                    if(property.getId().equals(reservation.getIdProperty())){
                        reservations.add(reservation);

                        FirebaseDatabase.getInstance("https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app")
                                .getReference("User")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                            User user = dataSnapshot1.getValue(User.class);

                                            assert user != null;
                                            if(user.getId().equals(reservation.getIdUser())) {
                                                users.add(user);
                                            }
                                            adapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                }

                Collections.sort(reservations, (r1, r2) -> {
                    if(r1.getFirst() < r2.getFirst())
                        return -1;

                    if(r1.getFirst() == r2.getFirst())
                        return 0;

                    return 1;
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mDatabase.addValueEventListener(valueEventListener);

        allAcceptedReservations.setHasFixedSize(true);
        allAcceptedReservations.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new AcceptedReservationsAdapter(this, reservations, users);
        allAcceptedReservations.setAdapter(adapter);
    }

}