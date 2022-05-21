package com.example.rotravel;

import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.util.Pair;

import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rotravel.HelperClasses.ApplicationManager;
import com.example.rotravel.Model.Property;
import com.example.rotravel.Model.Reservation;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
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

    private TextView txtPropertyName;
    private TextView txtPropertyDescription;
    private TextView txtPropertyPrice;
    private TextView txtDateSelected;
    private TextView txtTotalPayment;
    private TextView txtMaxCapacity;
    private MaterialCardView notForOwner;
    TextView btnCheckOnMap;
    ImageView imageProperty;
    ImageView btnBack;
    MaterialButton btnSelectDate;
    MaterialButton btnReserve;
    EditText txtEnterCapacity;

    Property property;
    private DatabaseReference mDatabase;
    int numDay;
    int maxCap, propertyCapacity;
    int payment;
    long first, last;
    String totalPayment;
    String capacityEntered;
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
        btnCheckOnMap = findViewById(R.id.btnCheckOnMap);
        notForOwner = findViewById(R.id.notForOwner);

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

                                //waitResponseForReservation(newReservation);

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

//    private void waitResponseForReservation(Reservation newReservation) {
//        if(ApplicationManager.getInstance().getUser().getId().equals(property.getIdUser())) {
//            NotificationManager manager = (NotificationManager)
//                    getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//            // Initialize intent for yes button
//            Intent intent1 = new Intent(PropertyActivity.this, MadeReservationsActivity.class);
//            intent1.putExtra("yes", true);
//            intent1.putExtra(MadeReservationsActivity.RESPONSE, newReservation);
//            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent1 = PendingIntent.getActivity(
//                    PropertyActivity.this, 0, intent1, PendingIntent.FLAG_ONE_SHOT);
//
//            // Initialize intent for no button
//            Intent intent2 = new Intent(PropertyActivity.this, MadeReservationsActivity.class);
//            intent2.putExtra("no", false);
//            intent2.putExtra(MadeReservationsActivity.RESPONSE, newReservation);
//            intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent2 = PendingIntent.getActivity(
//                    PropertyActivity.this, 0, intent2, PendingIntent.FLAG_ONE_SHOT);
//
//            // Get default ringtone uri
//            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            //Initialize notification builder
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                    PropertyActivity.this, getString(R.string.app_name));
//            // Set notification title
//            builder.setContentTitle("Reservation request");
//            // Set content text
//            if (newReservation.getIdProperty().equals(property.getId()))
//                builder.setContentText("Reservation at " + property.getName() + ", date: " + newReservation.getDate());
//            // Set icon
//            builder.setSmallIcon(R.drawable.ic_notifications_ic);
//            // Set sound
//            builder.setSound(uri);
//            // Set auto cancel
//            builder.setAutoCancel(true);
//            // Set priority
//            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
//
//            builder.addAction(R.drawable.ic_launcher_foreground, "Yes", pendingIntent1);
//            builder.addAction(R.drawable.ic_launcher_foreground, "No", pendingIntent2);
//
//            // Notification manager
//            manager.notify(1, builder.build());
//        }
//
//        Intent intent = new Intent(PropertyActivity.this, MadeReservationsActivity.class);
//        intent.putExtra(MadeReservationsActivity.RESPONSE, newReservation);
//    }

}