package com.group20.cscb07project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AuthenActivity extends AppCompatActivity {
    private View currentFragmentView;
    private List<Integer> pin = new ArrayList<>();
    private ImageView[] pinDots;
    private static final int PIN_LENGTH = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Start with launch fragment
        showLaunchFragment();
    }

    public void showLaunchFragment() {
        View fragmentView = getLayoutInflater().inflate(R.layout.fragment_launch, null);
        setFragmentView(fragmentView);
        
        Button getStartedButton = fragmentView.findViewById(R.id.getStartedButton);
        FloatingExitButton exitButton = fragmentView.findViewById(R.id.exitButton);
        CheckBox disclaimerCheckbox = fragmentView.findViewById(R.id.disclaimerCheckbox);
        TextView disclaimerTextView = fragmentView.findViewById(R.id.disclaimerTextView);

        // Initially disable the button
        getStartedButton.setEnabled(false);
        getStartedButton.setAlpha(0.5f);

        disclaimerCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Enable button
                getStartedButton.setEnabled(true);
                getStartedButton.setAlpha(1.0f);
            } else {
                // Disable button
                getStartedButton.setEnabled(false);
                getStartedButton.setAlpha(0.5f);
            }
        });

        getStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (disclaimerCheckbox.isChecked()) {
                    showLoginFragment();
                }
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitButton.setActivity(AuthenActivity.this);
                exitButton.exitApp();
            }
        });
    }

    public void showLoginFragment() {
        View fragmentView = getLayoutInflater().inflate(R.layout.fragment_login, null);
        setFragmentView(fragmentView);

        FloatingExitButton exitButton = fragmentView.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitButton.setActivity(AuthenActivity.this);
                exitButton.exitApp();
            }
        });
        
        SharedPreferences preferences = getSharedPreferences("projectpreferences", MODE_PRIVATE);
        String pin = preferences.getString("PIN", null);

        if (pin != null) {
            TextView pinLogin = fragmentView.findViewById(R.id.PinTextView);
            pinLogin.setVisibility(View.VISIBLE);
        }

        TextView firebaseLogin = fragmentView.findViewById(R.id.FirebaseTextView);
        firebaseLogin.setOnClickListener(v -> {
            // Handle Firebase login logic here
            // For now, just navigate to set pin
            // In a real implementation, you would handle Firebase authentication here
            // and then navigate to MainActivity after successful auth
            String existingPin = preferences.getString("PIN", null);
            if(existingPin==null){
                showSetPinFragment();
            } else {
                // Navigate to MainActivity
                Intent intent = new Intent(AuthenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView signout = fragmentView.findViewById(R.id.SignOutTextView);
        signout.setOnClickListener(v -> {
            // Handle sign out logic here
        });
    }

    public void showSetPinFragment() {
        View fragmentView = getLayoutInflater().inflate(R.layout.fragment_set_pin, null);
        setFragmentView(fragmentView);

        pinDots = new ImageView[] {
                fragmentView.findViewById(R.id.pinDot1),
                fragmentView.findViewById(R.id.pinDot2),
                fragmentView.findViewById(R.id.pinDot3),
                fragmentView.findViewById(R.id.pinDot4)
        };

        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };
        
        for (int id : buttonIds) {
            Button btn = fragmentView.findViewById(id);
            btn.setOnClickListener(v -> onDigitPressed(Integer.parseInt(btn.getText().toString())));
        }

        ImageButton backspace = fragmentView.findViewById(R.id.btnBackspace);
        backspace.setOnClickListener(v -> onBackspacePressed());
        
        pin.clear();
        updatePinDots();
    }

    public void showSignupFragment() {
        View fragmentView = getLayoutInflater().inflate(R.layout.fragment_login, null);
        setFragmentView(fragmentView);
        
        // Handle signup logic here
        // For now, just navigate back to login
        showLoginFragment();
    }

    private void setFragmentView(View fragmentView) {
        ViewGroup fragmentContainer = findViewById(R.id.fragment_container);
        if (fragmentContainer != null) {
            fragmentContainer.removeAllViews();
            fragmentContainer.addView(fragmentView);
            currentFragmentView = fragmentView;
        }
    }

    private void onDigitPressed(int digit) {
        if (pin.size() < PIN_LENGTH) {
            pin.add(digit);
            updatePinDots();
            if (pin.size() == PIN_LENGTH) {
                // Handle PIN complete
                SharedPreferences preferences = getSharedPreferences("projectpreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                StringBuilder p = new StringBuilder();
                for(int i=0; i<pin.size(); i++){
                    p.append(pin.get(i));
                }
                editor.putString("PIN", String.valueOf(p));
                editor.apply();
                
                // Navigate to MainActivity after PIN setup
                Intent intent = new Intent(AuthenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void onBackspacePressed() {
        if (!pin.isEmpty()) {
            pin.remove(pin.size() - 1);
            updatePinDots();
        }
    }

    private void updatePinDots() {
        for (int i = 0; i < PIN_LENGTH; i++) {
            if (i < pin.size()) {
                pinDots[i].setImageResource(R.drawable.pin_dot_filled);
            } else {
                pinDots[i].setImageResource(R.drawable.pin_dot_unfilled);
            }
        }
    }
}


