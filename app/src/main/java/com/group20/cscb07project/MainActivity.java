package com.group20.cscb07project;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.group20.cscb07project.EmergencyInfo.EmergencyInfoFragment;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private BottomNavigationView bottomNavigationView;
    private FloatingExitButton exitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance("https://cscb07-project-group-20-default-rtdb.firebaseio.com/");
        auth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        exitButton = findViewById(R.id.exitButton);

        setupBottomNavigation();
        setupEmergencyExit();

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            if (item.getItemId() == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_emergency_info) {
                fragment = new EmergencyInfoFragment();
            } else if (item.getItemId() == R.id.nav_settings) {
                fragment = new SettingsFragment();
            } else if (item.getItemId() == R.id.nav_safety_plan) {
                fragment = new SafetyPlanFragment();
            }
            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }
            return false;
        });
    }

    private void setupEmergencyExit() {
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitButton.setActivity(MainActivity.this);
                exitButton.exitApp();
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public FirebaseDatabase getDatabase() {
        return db;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }
}


