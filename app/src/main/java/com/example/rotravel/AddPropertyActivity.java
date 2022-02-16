package com.example.rotravel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rotravel.HelperClasses.ApplicationManager;
import com.example.rotravel.HelperClasses.BaseMenuActivity;
import com.example.rotravel.Model.Place;
import com.example.rotravel.Model.Property;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class AddPropertyActivity extends BaseMenuActivity {

    //widgets
    private EditText txtName;
    private EditText txtPrice;
    private EditText txtDescription;
    private ImageView imageView;
    private MaterialButton btnAddProperty;

    private DataSnapshot dataSnapshot;
    private ArrayList<Place> places = new ArrayList<>();
    private Place selectedPlace;
    private Uri uri;

    //firebase
    private DatabaseReference referencePlaces;

    private DatabaseReference referenceProperties;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txtName = findViewById(R.id.txtPropertyName);
        txtPrice = findViewById(R.id.txtPricePerNight);
        txtDescription = findViewById(R.id.txtDescription);
        imageView = findViewById(R.id.imageView);
        btnAddProperty = findViewById(R.id.btnAddProperty);

        referencePlaces = FirebaseDatabase.getInstance("https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Places");
        referenceProperties = FirebaseDatabase.getInstance("https://rotravel-f9f6a-default-rtdb.europe-west1.firebasedatabase.app").getReference("Properties");
        storageReference = FirebaseStorage.getInstance().getReference();

        setSpinner();

        imageView.setOnClickListener(v -> {
            openFileChooser();
        });

        btnAddProperty.setOnClickListener(v -> createProperty());

    }

    public void openFileChooser(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && resultCode == RESULT_OK && data!=null){
            uri = data.getData();
            Picasso.get().load(uri).into(imageView);
            //imageView.setImageURI(uri);
        }
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
        } else if(uri == null){
            Toast.makeText(this, "Upload image", Toast.LENGTH_SHORT).show();
        }else{
            Property property = new Property();
            property.setId(UUID.randomUUID().toString());
            property.setIdPlace(selectedPlace.getId());
            property.setIdUser(ApplicationManager.getInstance().getUser().getId());
            property.setName(name);
            property.setPrice(num);
            property.setDescription(description);

            uploadImageToFirebase(property);


            referenceProperties.child(property.getId()).setValue(property);
            Toast.makeText(this, "Property added", Toast.LENGTH_SHORT).show();
        }  
    }

    private void uploadImageToFirebase(Property property) {
        StorageReference file = storageReference.child("properties/" + System.currentTimeMillis() + "." + getFileExtension(uri));
        file.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String fileLink = task.getResult().toString();
                        property.setImage(fileLink);
                        referenceProperties.child(property.getId()).setValue(property);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPropertyActivity.this, "Upload image failed", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private String getFileExtension(Uri uri) {
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(getContentResolver().getType(uri));
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
        referencePlaces.addValueEventListener(postListener);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_add_property;
    }
}