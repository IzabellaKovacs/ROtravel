package com.example.rotravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.rotravel.HelperClasses.ApplicationManager;
import com.example.rotravel.HelperClasses.BaseMenuActivity;
import com.example.rotravel.Model.Place;
import com.example.rotravel.Model.Property;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class AddPropertyActivity extends BaseMenuActivity {

    EditText txtName;
    EditText txtPrice;
    EditText txtDescription;
    MaterialButton btnChoose;
    MaterialButton btnUpload;
    ImageView imageView;

    private DatabaseReference mDatabse;
    DataSnapshot dataSnapshot;
    ArrayList<Place> places = new ArrayList<>();
    private Place selectedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txtName = findViewById(R.id.txtPropertyName);
        txtPrice = findViewById(R.id.txtPricePerNight);
        txtDescription = findViewById(R.id.txtDescription);
        btnChoose = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.imgView);

        // TO DO: Upload photo of the property accessing photo gallery and camera

        mDatabse = FirebaseDatabase.getInstance("https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Places");

        setSpinner();

        btnUpload.setOnClickListener(v -> createProperty());

    }

    private void createProperty() {
        String name = txtName.getText().toString();
        String description = txtDescription.getText().toString();
        String price = txtPrice.getText().toString();
        int num = 0;
        try{
           num = Integer.parseInt(price);
        }catch(Exception e){
            txtPrice.setError("Price is required");
            txtPrice.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(name)) {
            txtName.setError("Property name is required");
            txtName.requestFocus();
        }else if(TextUtils.isEmpty(description)){
            txtDescription.setError("Description is required");
            txtDescription.requestFocus();
        }else if(TextUtils.isEmpty(price) || num > 100 ){
            txtPrice.setError("Price must be between 10 - 100 ron / night");
            txtPrice.requestFocus();
        }else {
            Property property = new Property();
            property.setId(UUID.randomUUID().toString());
            property.setIdPlace(selectedPlace.getId());
            property.setIdUser(ApplicationManager.getInstance().getUser().getId());
            property.setName(name);
            property.setPrice(num);
            property.setDescription(description);

            FirebaseDatabase
                    .getInstance("https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app")
                    .getReference("Properties")
                    .child(property.getId())
                    .setValue(property);

        }
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
                dataSnapshot = snapshot;
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