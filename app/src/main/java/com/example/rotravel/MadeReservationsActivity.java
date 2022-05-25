package com.example.rotravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

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
import java.util.Collections;

public class MadeReservationsActivity extends AppCompatActivity {

    public static String PROPERTY_RESERVATIONS = "propertyReservations";

    private ImageView btnBack;
    private RecyclerView allMadeReservations;
    private DatabaseReference mDatabaseRequests;

    MadeReservationsAdapter adapter;

    Property property;
    private final ArrayList<Reservation> reservations = new ArrayList<>();
    private final ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_reservations);

        btnBack = findViewById(R.id.btnBack);
        allMadeReservations = findViewById(R.id.allMadeReservations);

        property = getIntent().getParcelableExtra(PROPERTY_RESERVATIONS);

        btnBack.setOnClickListener(v -> onBackPressed());

        mDatabaseRequests = FirebaseDatabase.getInstance("https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Reservations");

        showAllMadeReservationsRequest();

    }

    private void showAllMadeReservationsRequest() {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reservations.clear();
                users.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reservation reservation = dataSnapshot.getValue(Reservation.class);

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

        mDatabaseRequests.addValueEventListener(valueEventListener);

        allMadeReservations.setHasFixedSize(true);
        allMadeReservations.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new MadeReservationsAdapter(this, reservations, users);
        allMadeReservations.setAdapter(adapter);
    }


}