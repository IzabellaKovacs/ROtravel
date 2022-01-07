package com.example.rotravel;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.rotravel.HelperClasses.ApplicationManager;
import com.example.rotravel.HelperClasses.BaseMenuActivity;
import com.example.rotravel.Model.Property;
import com.example.rotravel.Model.Reservation;
import com.example.rotravel.Model.UtilsReservations;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class PropertyActivity extends BaseMenuActivity {

    TextView txtPropertyName;
    TextView txtPropertyDescription;
    TextView txtPropertyPrice;

    MaterialButton btnSelectDate;
    MaterialButton btnCall;
    MaterialButton btnReserve;

    Property property;
    private DatabaseReference mDatabase;

    public static String PROPERTY = "property";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        property = getIntent().getParcelableExtra(PROPERTY);

        txtPropertyName = findViewById(R.id.txtPropertyName);
        txtPropertyDescription = findViewById(R.id.txtPropertyDescription);
        txtPropertyPrice = findViewById(R.id.txtPropertyPricePerNight);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnCall = findViewById(R.id.btnCall);
        btnReserve = findViewById(R.id.btnReserve);

        txtPropertyName.setText(property.getName());
        txtPropertyDescription.setText(property.getDescription());
        String price = String.valueOf(property.getPrice());
        txtPropertyPrice.setText(price);

        mDatabase = FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Reservations");


        reserve();

       // TO DO: Calendar picker
    }

    private void reserve() {
        Reservation newReservation = new Reservation();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reservation reservation = dataSnapshot.getValue(Reservation.class);

                    if (reservation.getIdProperty().equals(property.getId()) &&
                            ApplicationManager.getInstance().getUser().getId().equals(reservation.getIdUser())) {
                        btnReserve.setEnabled(false);
                        btnReserve.setBackgroundColor(getResources().getColor(R.color.textColor));
                    } else {
                        btnReserve.setOnClickListener(v -> {
                            newReservation.setId(UUID.randomUUID().toString());
                            newReservation.setIdProperty(property.getId());
                            newReservation.setIdUser(ApplicationManager.getInstance().getUser().getId());
                            newReservation.setDate("20/10/2022");

                            mDatabase.child(newReservation.getId()).setValue(newReservation);
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mDatabase.addValueEventListener(postListener);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_property;
    }
}