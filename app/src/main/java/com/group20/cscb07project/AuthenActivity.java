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
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.group20.cscb07project.PinManager;

public class AuthenActivity extends AppCompatActivity {
    private View currentFragmentView;
    private List<Integer> pin = new ArrayList<>();
    private ImageView[] pinDots;
    private static final int PIN_LENGTH = 4;
    FloatingExitButton exitButton;

    // Firebase Authentication
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            
            // Check if PIN exists, if not go to SetPinActivity
            boolean pinExists = PinManager.doesPinExist(this);
            if(!pinExists){
                // First time user - create PIN
                Intent intent = new Intent(AuthenActivity.this, SetPinActivity.class);
                startActivity(intent);
            } else {
                // PIN exists, navigate to main
                Intent intent = new Intent(AuthenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            if (response != null) {
                Toast.makeText(this, "Authentication failed: " + response.getError().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        showLaunchFragment();
    }

    public void showLaunchFragment() {
        View fragmentView = getLayoutInflater().inflate(R.layout.fragment_launch, null);
        setFragmentView(fragmentView);
        
        Button getStartedButton = fragmentView.findViewById(R.id.getStartedButton);
        exitButton = findViewById(R.id.exitButton);
        CheckBox disclaimerCheckbox = fragmentView.findViewById(R.id.disclaimerCheckbox);
        TextView disclaimerTextView = fragmentView.findViewById(R.id.disclaimerTextView);

        // Initially disable the button
        getStartedButton.setEnabled(false);
        getStartedButton.setAlpha(0.5f);

        disclaimerCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                getStartedButton.setEnabled(true);
                getStartedButton.setAlpha(1.0f);
            } else {
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

        exitButton = fragmentView.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitButton.setActivity(AuthenActivity.this);
                exitButton.exitApp();
            }
        });
        
        boolean pinExists = PinManager.doesPinExist(this);

        MaterialButton pinSignInButton = fragmentView.findViewById(R.id.PINSignInButton);
        if (pinExists) {
            pinSignInButton.setVisibility(View.VISIBLE);
        } else {
            pinSignInButton.setVisibility(View.GONE);
        }

        Button emailSignInButton = fragmentView.findViewById(R.id.emailSignInButton);
        emailSignInButton.setOnClickListener(v -> {
            // Handle email sign-in
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build());

            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);
        });

        Button googleSignInButton = fragmentView.findViewById(R.id.googleSignInButton);
        googleSignInButton.setOnClickListener(v -> {
            // Handle Google sign-in
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);
        });


    }

    public void showSetPinFragment() {
        View fragmentView = getLayoutInflater().inflate(R.layout.fragment_set_pin, null);
        setFragmentView(fragmentView);

        // Set the title based on whether PIN exists
        TextView titleTextView = fragmentView.findViewById(R.id.setPinTextView);
        boolean pinExists = PinManager.doesPinExist(this);
        if (pinExists) {
            titleTextView.setText(R.string.enter_pin);
        } else {
            titleTextView.setText(R.string.set_pin);
        }

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
        try {
            if (pin.size() < PIN_LENGTH) {
                pin.add(digit);
                updatePinDots();
                if (pin.size() == PIN_LENGTH) {
                    StringBuilder p = new StringBuilder();
                    for(int i=0; i<pin.size(); i++){
                        p.append(pin.get(i));
                    }
                    String pinString = p.toString();
                    
                    // Check if PIN exists to determine if we're setting or entering
                    boolean pinExists = PinManager.doesPinExist(this);
                    
                                    if (!pinExists) {
                    // Setting new PIN
                    boolean saved = PinManager.savePin(this, pinString);
                    if (saved) {
                        Intent intent = new Intent(AuthenActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    boolean isCorrect = PinManager.verifyPin(this, pinString);
                    if (isCorrect) {
                        Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AuthenActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Wrong PIN
                        pin.clear();
                        updatePinDots();
                        Toast.makeText(this, "Incorrect PIN. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                }
            }
        } catch (Exception e) {
            pin.clear();
            updatePinDots();
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


