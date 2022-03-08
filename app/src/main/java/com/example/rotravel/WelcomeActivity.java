package com.example.rotravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.rotravel.HelperClasses.BaseMenuActivity;


public class WelcomeActivity extends BaseMenuActivity {

    private Button btnPlanTrip;
    private Button btnFindPlacesNearby;
    private Button btnShareExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnPlanTrip = findViewById(R.id.btnPlanTrip);
        btnFindPlacesNearby = findViewById(R.id.btnFindPlaces);
        btnShareExperience = findViewById(R.id.btnShareExperience);

        btnPlanTrip.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, TripPlanActivity.class);
            startActivity(intent);
        });

        btnFindPlacesNearby.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, FindPlacesNearby.class);
            startActivity(intent);
        });

        btnShareExperience.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, ShareExperienceActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_welcome;
    }


}