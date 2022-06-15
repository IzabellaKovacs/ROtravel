package com.example.rotravel;

import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rotravel.HelperClasses.ApplicationManager;
import com.example.rotravel.Model.Property;
import com.example.rotravel.Model.Reservation;

import com.example.rotravel.Model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PropertyActivity extends AppCompatActivity {

    // Init widgets
    private TextView txtPropertyName;
    private TextView txtPropertyDescription;
    private TextView txtPropertyPrice;
    private TextView txtDateSelected;
    private TextView txtTotalPayment;
    private TextView txtMaxCapacity;
    private MaterialCardView notForOwner;
    private TextView btnCheckOnMap;
    private ImageView imageProperty;
    private ImageView btnBack;
    private MaterialButton btnSelectDate;
    private MaterialButton btnReserve;
    private MaterialButton btnContactOwner;
    private EditText txtEnterCapacity;

    public static String PROPERTY = "property";
    Property property;

    private DatabaseReference mDatabase;

    int numDay;
    int maxCap;
    int payment;
    long first, last;
    String totalPayment;
    String capacityEntered;
    MaterialDatePicker<Pair<Long, Long>> datePicker;
    ArrayList<User> owner2 = new ArrayList<>();

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
        btnCheckOnMap = findViewById(R.id.btnCheckOnMap);
        notForOwner = findViewById(R.id.notForOwner);
        btnContactOwner = findViewById(R.id.btnContactOwner);

        btnBack.setOnClickListener(v -> onBackPressed());

        if(ApplicationManager.getInstance().getUser().getId().equals(property.getIdUser())){
            notForOwner.setVisibility(View.GONE);
        }

        datePicker = MaterialDatePicker.Builder
                .dateRangePicker()
                .setCalendarConstraints(new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build())
                .build();

        btnSelectDate.setOnClickListener(v -> datePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        datePicker.addOnPositiveButtonClickListener(selection -> {
            first = selection.first;
            last = selection.second;

            long numDays = last - first;
            numDay = (int) TimeUnit.MILLISECONDS.toDays(numDays);

            payment = numDay*property.getPrice();
            capacityEntered = txtEnterCapacity.getText().toString();

            try{
                maxCap = Integer.parseInt(capacityEntered);
            }catch(Exception e){
                txtEnterCapacity.setError("Capacity is required");
                txtEnterCapacity.requestFocus();
                return;
            }

            totalPayment = payment* maxCap + "";

            txtTotalPayment.setText(totalPayment);
            txtDateSelected.setText(datePicker.getHeaderText());
        });

        // Show property details
        txtPropertyName.setText(property.getName());
        txtPropertyDescription.setText(property.getDescription());
        String price = String.valueOf(property.getPrice());
        txtPropertyPrice.setText(price);
        Picasso.get().load(property.getImage()).into(imageProperty);
        String capacity = String.valueOf(property.getCapacity());
        txtMaxCapacity.setText(capacity);

        btnCheckOnMap.setOnClickListener(v -> {
            Intent intent = new Intent(PropertyActivity.this, FindPlacesNearby.class);
            intent.putExtra("LAT", property.getLat());
            intent.putExtra("LNG", property.getLng());
            intent.putExtra("Desc", property.getName());
            startActivity(intent);
        });

        getOwner();

        btnContactOwner.setOnClickListener(v -> {
            contactOwner();
        });

        mDatabase = FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Reservations");

        reserve();
    }

    private void contactOwner() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", owner2.get(0).getPhone(), null ));
        intent.putExtra("sms_body", "[Reservation]: ");
        startActivity(intent);

    }

    private void getOwner(){
        FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("User")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            ArrayList<User> owner = new ArrayList<>();
                            User user = dataSnapshot.getValue(User.class);

                            assert user != null;
                            if(user.getId().equals(property.getIdUser())){
                                owner.add(user);
                                owner2.add(owner.get(0));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void reserve() {
        Reservation newReservation = new Reservation();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Reservation reservation = dataSnapshot.getValue(Reservation.class);

                    assert reservation != null;

                        btnReserve.setOnClickListener(v -> {
                            if(numDay == 0){
                                Toast.makeText(PropertyActivity.this, "You must select a date", Toast.LENGTH_LONG).show();
                            }else if(maxCap > property.getCapacity()){
                                Toast.makeText(PropertyActivity.this, "Maximum capacity is " + property.getCapacity(), Toast.LENGTH_LONG).show();
                            } else {

                                newReservation.setId(UUID.randomUUID().toString());
                                newReservation.setIdProperty(property.getId());
                                newReservation.setIdUser(ApplicationManager.getInstance().getUser().getId());
                                newReservation.setDate(txtDateSelected.getText().toString());
                                newReservation.setFirst(first);
                                newReservation.setLast(last);
                                newReservation.setTotal(txtTotalPayment.getText().toString());
                                newReservation.setTotalCapacity(maxCap);

                                mDatabase.child(newReservation.getId()).setValue(newReservation);

                                AlertDialog.Builder builder = new AlertDialog.Builder(PropertyActivity.this);
                                builder.setTitle("THE REQUEST HAS BEEN SENT!");
                                builder.setMessage("\n\nCheck your profile to see if the owner has approved/declined your request. ");
                                builder.setNegativeButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss());
                                builder.show();
                            }
                        });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mDatabase.addValueEventListener(postListener);
    }

}