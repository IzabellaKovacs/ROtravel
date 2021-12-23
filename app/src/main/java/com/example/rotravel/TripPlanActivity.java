package com.example.rotravel;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.rotravel.HelperClasses.AllCitiesRecViewAdapter;
import com.example.rotravel.HelperClasses.BaseMenuActivity;
import com.example.rotravel.Model.Place;

import java.util.ArrayList;

public class TripPlanActivity extends BaseMenuActivity {

    RecyclerView allPlaces;
    AllCitiesRecViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allPlaces = findViewById(R.id.allPlaces);

        showAllPlaces();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_trip_plan;
    }

    private void showAllPlaces(){
        allPlaces.setHasFixedSize(true);
        allPlaces.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ArrayList<Place> places = new ArrayList<>();
        places.add(new Place(R.drawable.tm, "Timisoara"));
        places.add(new Place(R.drawable.carpati, "Muntii Carpati"));
        places.add(new Place(R.drawable.oradea, "Oradea"));
        places.add(new Place(R.drawable.orsova, "Orsova"));
        places.add(new Place(R.drawable.b, "Bucuresti"));
        places.add(new Place(R.drawable.belis, "Belis"));
        places.add(new Place(R.drawable.iasi, "Iasi"));
        places.add(new Place(R.drawable.poiana, "Poiana Marului"));
        places.add(new Place(R.drawable.remetea, "Remetea Bihor"));
        places.add(new Place(R.drawable.retezat, "Muntii Retezat"));
        places.add(new Place(R.drawable.alba, "Alba Iulia"));
        places.add(new Place(R.drawable.cj, "Cluj-Napoca"));
        places.add(new Place(R.drawable.con, "Constanta"));
        places.add(new Place(R.drawable.vfmoldoveanu, "Varful Moldoveanu"));
        places.add(new Place(R.drawable.craiova, "Craiova"));
        places.add(new Place(R.drawable.vfomu, "Varful Omu"));
        places.add(new Place(R.drawable.fagaras, "Muntii Fagaras"));
        places.add(new Place(R.drawable.sb, "Sibiu"));


        adapter = new AllCitiesRecViewAdapter(this, places);
        allPlaces.setAdapter(adapter);
    }
}