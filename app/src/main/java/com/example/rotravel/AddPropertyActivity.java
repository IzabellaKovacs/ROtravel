package com.example.rotravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.rotravel.HelperClasses.BaseMenuActivity;
import com.example.rotravel.Model.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddPropertyActivity extends BaseMenuActivity {

    private DatabaseReference mDatabse;
    ArrayList<Place> places = new ArrayList<>();
    private Place selectedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabse = FirebaseDatabase.getInstance("https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Places");

        setSpinner();

    }

    private void setSpinner() {
        Spinner spinner = findViewById(R.id.spinnerPlaces);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPlace = places.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ValueEventListener postListener = new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> names = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Place place = dataSnapshot.getValue(Place.class);
                    places.add(place);
                    names.add(place.getName());
                }
                selectedPlace = places.get(0);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddPropertyActivity.this, R.layout.support_simple_spinner_dropdown_item, names);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDatabse.addValueEventListener(postListener);
    }


    @Override
    public int getLayoutRes() {
        return R.layout.activity_add_property;
    }
}