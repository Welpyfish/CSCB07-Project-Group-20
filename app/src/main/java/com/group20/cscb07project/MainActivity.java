package com.group20.cscb07project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton emergencyExitFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        db = FirebaseDatabase.getInstance("https://b07-demo-summer-2024-default-rtdb.firebaseio.com/");
        auth = FirebaseAuth.getInstance();

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        emergencyExitFab = findViewById(R.id.exitButton);

        // Setup bottom navigation
        setupBottomNavigation();

        // Setup emergency exit
        setupEmergencyExit();

        // Load default fragment
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
        emergencyExitFab.setOnClickListener(v -> exitApp());
    }

    private void exitApp() {
        Uri url = Uri.parse("https://www.amazon.ca/");          //Uri for deep link to amazon website
        Intent redirection = new Intent(Intent.ACTION_VIEW, url);       //Create intent for redirection
        redirection.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);            //Flags redirection as new task
        startActivity(redirection);                                     //Redirects to amazon
        try {
            Thread.sleep(100);                                      //Delays termination so browser can open
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finishAndRemoveTask();                                          //Terminates activity and app
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


