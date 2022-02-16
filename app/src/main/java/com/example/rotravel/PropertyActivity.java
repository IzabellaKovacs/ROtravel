package com.example.rotravel;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.util.Pair;

import android.widget.TextView;
import com.example.rotravel.HelperClasses.ApplicationManager;
import com.example.rotravel.HelperClasses.BaseMenuActivity;
import com.example.rotravel.Model.Property;
import com.example.rotravel.Model.Reservation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class PropertyActivity extends BaseMenuActivity {

    TextView txtPropertyName;
    TextView txtPropertyDescription;
    TextView txtPropertyPrice;
    TextView txtDateSelected;

    MaterialButton btnSelectDate;
    MaterialButton btnReserve;

    Property property;
    private DatabaseReference mDatabase;

    public static String PROPERTY = "property";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        property = getIntent().getParcelableExtra(PROPERTY);

        txtPropertyName = findViewById(R.id.txtPropertyName);
        txtPropertyDescription = findViewById(R.id.txtPropertyDescription);
        txtPropertyPrice = findViewById(R.id.txtPropertyPricePerNight);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnReserve = findViewById(R.id.btnReserve);
        txtDateSelected = findViewById(R.id.selectedDate);

        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        btnSelectDate.setOnClickListener(
                v -> materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(
                selection -> txtDateSelected.setText(materialDatePicker.getHeaderText()));

        txtPropertyName.setText(property.getName());
        txtPropertyDescription.setText(property.getDescription());
        String price = String.valueOf(property.getPrice());
        txtPropertyPrice.setText(price);

        mDatabase = FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Reservations");

        reserve();


    }

    private void reserve() {
        Reservation newReservation = new Reservation();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reservation reservation = dataSnapshot.getValue(Reservation.class);

                    assert reservation != null;
                    if (reservation.getIdProperty().equals(property.getId()) &&
                            ApplicationManager.getInstance().getUser().getId().equals(reservation.getIdUser())) {
                        btnReserve.setEnabled(false);
                        btnReserve.setBackgroundColor(getResources().getColor(R.color.textColor));
                    } else {
                        btnReserve.setOnClickListener(v -> {
                            newReservation.setId(UUID.randomUUID().toString());
                            newReservation.setIdProperty(property.getId());
                            newReservation.setIdUser(ApplicationManager.getInstance().getUser().getId());
                            newReservation.setDate(txtDateSelected.getText().toString());

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