package com.example.rotravel.HelperClasses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.rotravel.AddPropertyActivity;
import com.example.rotravel.R;
import com.example.rotravel.TripPlanActivity;
import com.example.rotravel.WelcomeActivity;
import com.google.android.material.navigation.NavigationView;

public abstract class BaseMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());

        btnMenu = findViewById(R.id.btnMenu);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener( this);

        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
    }

    public abstract int getLayoutRes();

    public static void redirectActivity(Activity activity, Class<?> aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public void ClickAddProperty(MenuItem item) {
        redirectActivity(this, AddPropertyActivity.class);
    }

    public void ClickHome(MenuItem item) {
        if (this instanceof WelcomeActivity) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            redirectActivity(this, WelcomeActivity.class);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }


}