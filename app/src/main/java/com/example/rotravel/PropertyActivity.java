package com.example.rotravel;

import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rotravel.HelperClasses.ApplicationManager;
import com.example.rotravel.Model.Property;
import com.example.rotravel.Model.Reservation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PropertyActivity extends AppCompatActivity {

    TextView txtPropertyName;
    TextView txtPropertyDescription;
    TextView txtPropertyPrice;
    TextView txtDateSelected;
    TextView txtTotalPayment;
    TextView txtMaxCapacity;
    ImageView imageProperty;
    ImageView btnBack;
    MaterialButton btnSelectDate;
    MaterialButton btnReserve;
    EditText txtEnterCapacity;

    Property property;
    private DatabaseReference mDatabase;
    int numDay;
    String payment;
    MaterialDatePicker<Pair<Long, Long>> datePicker;
    public static String PROPERTY = "property";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property);

        property = getIntent().getParcelableExtra(PROPERTY);

        //Init widgets
        imageProperty = findViewById(R.id.imageProperty);
        txtPropertyName = findViewById(R.id.txtPropertyName);
        txtPropertyDescription = findViewById(R.id.txtPropertyDescription);
        txtPropertyPrice = findViewById(R.id.txtPropertyPricePerNight);
        txtTotalPayment = findViewById(R.id.totalPayment);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnReserve = findViewById(R.id.btnReserve);
        btnBack = findViewById(R.id.btnBack);
        txtDateSelected = findViewById(R.id.selectedDate);
        txtMaxCapacity = findViewById(R.id.txtMaxCapacity);
        txtEnterCapacity = findViewById(R.id.txtEnterCapacity);

        btnBack.setOnClickListener(v -> onBackPressed());

        datePicker = MaterialDatePicker.Builder
                .dateRangePicker()
                .setCalendarConstraints(new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build())
                .build();

        btnSelectDate.setOnClickListener(v -> datePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));
        datePicker.addOnPositiveButtonClickListener(selection -> {
            long first = selection.first;
            long last = selection.second;

            long numDays = last - first;
            numDay = (int) TimeUnit.MILLISECONDS.toDays(numDays);

            payment = numDay*property.getPrice() + "";
            txtDateSelected.setText(datePicker.getHeaderText());
            txtTotalPayment.setText(payment);
        });


        txtPropertyName.setText(property.getName());

        txtPropertyDescription.setText(property.getDescription());

        String price = String.valueOf(property.getPrice());
        txtPropertyPrice.setText(price);

        Picasso.get().load(property.getImage()).into(imageProperty);

        String capacity = String.valueOf(property.getCapacity());
        txtMaxCapacity.setText(capacity);

        mDatabase = FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Reservations");

        reserve();
    }

    private void reserve() {
        Reservation newReservation = new Reservation();

//        String capacityEntered = txtEnterCapacity.getText().toString();
//        int maxCap;
//        try{
//            maxCap = Integer.parseInt(capacityEntered);
//        }catch(Exception e){
//            txtEnterCapacity.setError("Capacity is required");
//            txtEnterCapacity.requestFocus();
//            return;
//        }

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reservation reservation = dataSnapshot.getValue(Reservation.class);

                    assert reservation != null;
                    if (reservation.getIdProperty().equals(property.getId()) &&
                            ApplicationManager.getInstance().getUser().getId().equals(reservation.getIdUser())) {

                        btnSelectDate.setEnabled(false);
                        btnSelectDate.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                        txtTotalPayment.setText(reservation.getTotal());
                        btnReserve.setBackgroundColor(getResources().getColor(R.color.lightBlue));
                        btnReserve.setEnabled(false);
                        btnReserve.setText("Reserved");
                    } else {
                        btnReserve.setOnClickListener(v -> {
                            if(numDay == 0){
                                Toast.makeText(PropertyActivity.this, "You must select a date", Toast.LENGTH_LONG).show();
                            } else {
                                newReservation.setId(UUID.randomUUID().toString());
                                newReservation.setIdProperty(property.getId());
                                newReservation.setIdUser(ApplicationManager.getInstance().getUser().getId());
                                newReservation.setDate(txtDateSelected.getText().toString());
                                newReservation.setTotal(txtTotalPayment.getText().toString());
                              //  newReservation.setTotalCapacity(maxCap);

                                mDatabase.child(newReservation.getId()).setValue(newReservation);
                            }
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

}