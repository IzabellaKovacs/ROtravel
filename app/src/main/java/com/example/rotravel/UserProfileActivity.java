package com.example.rotravel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotravel.HelperClasses.AllCitiesRecViewAdapter;
import com.example.rotravel.HelperClasses.AllReservedPropertiesAdapter;
import com.example.rotravel.HelperClasses.ApplicationManager;
import com.example.rotravel.HelperClasses.BaseMenuActivity;
import com.example.rotravel.Model.Property;
import com.example.rotravel.Model.Reservation;
import com.example.rotravel.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.UUID;

public class UserProfileActivity extends BaseMenuActivity {

    TextView txtName;
    TextView txtPhone;
    TextView txtEmail;
    RecyclerView allReservations;

    private DatabaseReference mDatabase;

    private ArrayList<Property> reservedProperties = new ArrayList<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();

    private AllReservedPropertiesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);
        allReservations = findViewById(R.id.allReservations);

        User user = ApplicationManager.getInstance().getUser();
        String fullName = user.getFirstName() + " " + user.getLastName();

        txtName.setText(fullName);
        txtPhone.setText(user.getPhone());
        txtEmail.setText(user.getEmail());

        mDatabase = FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Reservations");
        showReservations();
    }

    private void showReservations() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reservation reservation = dataSnapshot.getValue(Reservation.class);

                    if (ApplicationManager.getInstance().getUser().getId().equals(reservation.getIdUser())) {
                        reservations.add(reservation);

                        FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app")
                                .getReference("Properties")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            Property property = dataSnapshot.getValue(Property.class);

                                            if(reservation.getIdProperty().equals(property.getId()))
                                                reservedProperties.add(property);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mDatabase.addValueEventListener(postListener);

        allReservations.setHasFixedSize(true);
        allReservations.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new AllReservedPropertiesAdapter(this, reservedProperties, reservations);
        allReservations.setAdapter(adapter);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_user_profile;
    }

    // TO DO: inflate list with the reservations
}