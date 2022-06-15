package com.example.rotravel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotravel.HelperClasses.AllReservedPropertiesAdapter;
import com.example.rotravel.HelperClasses.ApplicationManager;
import com.example.rotravel.HelperClasses.BaseMenuActivity;
import com.example.rotravel.Model.Property;
import com.example.rotravel.Model.Reservation;
import com.example.rotravel.Model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class UserProfileActivity extends BaseMenuActivity {

    // Widgets
    private TextView txtName;
    private MaterialButton btnMyProperties, btnEdit;
    private TextInputLayout editPhone, editEmail;

    RecyclerView allReservations;
    private AllReservedPropertiesAdapter adapter;

    private DatabaseReference mDatabase, userDatabase;

    private final ArrayList<Property> properties = new ArrayList<>();
    private final ArrayList<Reservation> reservations = new ArrayList<>();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txtName = findViewById(R.id.txtName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        allReservations = findViewById(R.id.allReservations);
        btnMyProperties = findViewById(R.id.btnMyProperties);
        btnEdit = findViewById(R.id.btnEdit);

        user = ApplicationManager.getInstance().getUser();
        String fullName = user.getFirstName() + " " + user.getLastName();

        txtName.setText(fullName);

        Objects.requireNonNull(editPhone.getEditText()).setText(user.getPhone());
        Objects.requireNonNull(editEmail.getEditText()).setText(user.getEmail());


        btnMyProperties.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UserPropertiesActivity.class);
            startActivity(intent);
        });

        btnEdit.setOnClickListener(v -> editUserDetails());

        mDatabase = FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("AcceptedReservations");
        userDatabase = FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("User");

        showReservations();
    }

    private void editUserDetails() {
        String phone = Objects.requireNonNull(editPhone.getEditText()).getText().toString();
        String email = Objects.requireNonNull(editEmail.getEditText()).getText().toString();

        if(phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_LONG).show();
        } else {
            userDatabase.child(user.getId()).child("phone").setValue(phone);
            userDatabase.child(user.getId()).child("email").setValue(email);

            Toast.makeText(this, "Data has been updated", Toast.LENGTH_LONG).show();
        }
    }

    private void showReservations() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reservations.clear();
                properties.clear();
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
                                                properties.add(property);

                                        }
                                        adapter.notifyDataSetChanged();
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

        adapter = new AllReservedPropertiesAdapter(this, properties, reservations );
        allReservations.setAdapter(adapter);

    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_user_profile;
    }

}