package com.example.rotravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rotravel.HelperClasses.AllPlacesToStayRecViewAdapter;
import com.example.rotravel.HelperClasses.ApplicationManager;
import com.example.rotravel.HelperClasses.MadeReservationsAdapter;
import com.example.rotravel.HelperClasses.UserPropertiesRecViewAdapter;
import com.example.rotravel.Model.AcceptedReservation;
import com.example.rotravel.Model.Property;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserPropertiesActivity extends AppCompatActivity {

    private RecyclerView allPropertiesAndReservations;
    private ImageView btnBack;
    private UserPropertiesRecViewAdapter adapter;
    private DatabaseReference mDatabase;
    ArrayList<Property> properties = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_properties);

        allPropertiesAndReservations = findViewById(R.id.allPropertiesAndReservations);
        btnBack = findViewById(R.id.btnBack);

        mDatabase = FirebaseDatabase.getInstance(" https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Properties");

        btnBack.setOnClickListener(v -> onBackPressed());

        showAllProperties();

    }

    private void showAllProperties() {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Property property = dataSnapshot.getValue(Property.class);

                    assert property != null;
                    if(property.getIdUser().equals(ApplicationManager.getInstance().getUser().getId()))
                        properties.add(property);


                    }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mDatabase.addValueEventListener(valueEventListener);

        allPropertiesAndReservations.setHasFixedSize(true);
        allPropertiesAndReservations.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new UserPropertiesRecViewAdapter(this, properties, new UserPropertiesRecViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Property property) {
                Intent intent = new Intent(UserPropertiesActivity.this, AcceptedReservationsActivity.class);
                intent.putExtra(AcceptedReservationsActivity.PROPERTY_ACCEPTED_RESERVATIONS, property);
                startActivity(intent);
            }

            @Override
            public void onItemClickRequests(Property property) {
                Intent intent = new Intent(UserPropertiesActivity.this, MadeReservationsActivity.class);
                intent.putExtra(MadeReservationsActivity.PROPERTY_RESERVATIONS, property);
                startActivity(intent);
            }
        });

       allPropertiesAndReservations.setAdapter(adapter);

    }
}