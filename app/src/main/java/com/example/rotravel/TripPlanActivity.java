package com.example.rotravel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListView;

import com.example.rotravel.HelperClasses.AllCitiesRecViewAdapter;
import com.example.rotravel.HelperClasses.AllPlacesAdapter;
import com.example.rotravel.HelperClasses.BaseMenuActivity;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

import com.example.rotravel.Model.Place;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class TripPlanActivity extends BaseMenuActivity {

    List<Place> places = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView allPlacesList = findViewById(R.id.allPlacesList);

        final AllPlacesAdapter adapter = new AllPlacesAdapter(this, R.layout.list_item_city, places);
        allPlacesList.setAdapter(adapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();

        databaseReference.child("Places").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                try{
//                    Place place = snapshot.getValue(Place.class);
//
//                    if(place.getImage() != null && place.getName() != null){
//                        places.add(place);
//                        adapter.notifyDataSetChanged();
//                    }
//                }catch (Exception e){
//
//                }
                places.add(snapshot.getValue(Place.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_trip_plan;
    }

}