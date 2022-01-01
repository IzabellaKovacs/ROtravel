package com.example.rotravel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.rotravel.HelperClasses.BaseMenuActivity;


public class WelcomeActivity extends BaseMenuActivity {

    RelativeLayout btnPlanTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnPlanTrip = findViewById(R.id.btnPlanTrip);

        btnPlanTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, TripPlanActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_welcome;
    }


}