package com.example.rotravel.HelperClasses;

import com.example.rotravel.Model.NearbyPlace;
import com.example.rotravel.R;

import java.util.ArrayList;
import java.util.Arrays;

public interface AllConstants {
    int STORAGE_REQUEST_CODE = 1000;
    int LOCATION_REQUEST_CODE = 2000;

    ArrayList<NearbyPlace> nearbyPlacesName = new ArrayList<>(
            Arrays.asList(
                    new NearbyPlace(1, R.drawable.ic_restaurant_icon, "Restaurants", "restaurant"),
                    new NearbyPlace(2, R.drawable.ic_local_hospital_icon, "Hospitals & Clinics", "hospital"),
                    new NearbyPlace(3, R.drawable.ic_shopping_icon, "Groceries", "supermarket")
                    )
    );
}
