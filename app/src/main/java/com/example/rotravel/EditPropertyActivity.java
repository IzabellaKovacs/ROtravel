package com.example.rotravel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rotravel.Model.Property;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class EditPropertyActivity extends AppCompatActivity {

    public static String EDIT_PROPERTY = "edit property";
    private TextInputLayout editName, editPrice, editDescription, editCapacity;
    private MaterialButton btnEdit;
    private ImageView btnBack;

    DatabaseReference database;

    Property property;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_property);

        property = getIntent().getParcelableExtra(EDIT_PROPERTY);

        database = FirebaseDatabase.getInstance("https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Properties");

        editName = findViewById(R.id.editName);
        editPrice = findViewById(R.id.editPrice);
        editDescription = findViewById(R.id.editDescription);
        editCapacity = findViewById(R.id.editCapacity);
        btnEdit = findViewById(R.id.btnEditProperty);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> onBackPressed());

        Objects.requireNonNull(editName.getEditText()).setText(property.getName());
        String price = String.valueOf(property.getPrice());
        Objects.requireNonNull(editPrice.getEditText()).setText(price);
        Objects.requireNonNull(editDescription.getEditText()).setText(property.getDescription());
        String capacity = String.valueOf(property.getCapacity());
        Objects.requireNonNull(editCapacity.getEditText()).setText(capacity);


        btnEdit.setOnClickListener(v -> edit());

    }


    public void edit(){
        String name = Objects.requireNonNull(editName.getEditText()).getText().toString();
        String price = editPrice.getEditText().getText().toString();
        String description = Objects.requireNonNull(editDescription.getEditText()).getText().toString();
        String capacity = Objects.requireNonNull(editCapacity.getEditText()).getText().toString();

        int priceToInt, capacityToInt;

        try{
            priceToInt = Integer.parseInt(price);
        }catch(Exception e){
            editPrice.setError("Price is required");
            editPrice.requestFocus();
            return;
        }

        try{
            capacityToInt = Integer.parseInt(capacity);
        }catch(Exception e){
            editCapacity.setError("Capacity is required");
            editCapacity.requestFocus();
            return;
        }

        if(name.isEmpty() || price.isEmpty() || description.isEmpty() || capacity.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_LONG).show();
        } else {
            database.child(property.getId()).child("name").setValue(name);
            database.child(property.getId()).child("price").setValue(priceToInt);
            database.child(property.getId()).child("description").setValue(description);
            database.child(property.getId()).child("capacity").setValue(capacityToInt);

            Toast.makeText(this, "Data has been updated", Toast.LENGTH_LONG).show();
        }
    }

}