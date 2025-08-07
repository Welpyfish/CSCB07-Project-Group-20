package com.group20.cscb07project.Authorization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import com.google.android.material.button.MaterialButton;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import com.group20.cscb07project.FloatingExitButton;
import com.group20.cscb07project.MainActivity;
import com.group20.cscb07project.R;

public class LoginActivity extends AppCompatActivity {
    // See: https://developer.android.com/training/basics/intents/result
    private boolean existsPin;
    FloatingExitButton exitButton;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        existsPin = PinManager.doesPinExist(this);
        exitButton = findViewById(R.id.exitButton);
        setupEmergencyExit();

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitButton.setActivity(LoginActivity.this);
                exitButton.exitApp();
            }
        });
        // Check if PIN exists using PinManager
        boolean pinExists = PinManager.doesPinExist(this);

        MaterialButton pinSignInButton = findViewById(R.id.PINSignInButton);
        if (pinExists) {
            pinSignInButton.setVisibility(View.VISIBLE);
            pinSignInButton.setOnClickListener(v -> {
                showEnterPinFragment();
            });
        } else {
            pinSignInButton.setVisibility(View.GONE);
        }

        MaterialButton emailSignInButton = findViewById(R.id.emailSignInButton);
        emailSignInButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, EmailAuthActivity.class);
            startActivity(intent);
        });

        MaterialButton googleSignInButton = findViewById(R.id.googleSignInButton);
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

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                boolean pinExists = PinManager.doesPinExist(this);
                if (!pinExists) {
                    // First time user - create PIN
                    Intent intent = new Intent(LoginActivity.this, SetPinActivity.class);
                    startActivity(intent);
                } else {
                    // PIN exists, navigate to main
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        } else {
            if (response != null) {
                Toast.makeText(this, "Authentication failed: " + response.getError().getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showEnterPinFragment() {
        View enterPinView = getLayoutInflater().inflate(R.layout.fragment_enter_pin, null);
        setContentView(enterPinView);
        exitButton = findViewById(R.id.exitButton);
        setupEmergencyExit();

        ImageView[] pinDots = {
                enterPinView.findViewById(R.id.pinDot1),
                enterPinView.findViewById(R.id.pinDot2),
                enterPinView.findViewById(R.id.pinDot3),
                enterPinView.findViewById(R.id.pinDot4)
        };

        int[] buttonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };

        List<Integer> enteredPin = new ArrayList<>();
        final int PIN_LENGTH = 4;

        for (int id : buttonIds) {
            Button btn = enterPinView.findViewById(id);
            btn.setOnClickListener(v -> {
                if (enteredPin.size() < PIN_LENGTH) {
                    enteredPin.add(Integer.parseInt(btn.getText().toString()));
                    updatePinDots(pinDots, enteredPin.size());

                    if (enteredPin.size() == PIN_LENGTH) {
                        verifyPin(enteredPin); // Verify
                    }
                }
            });
        }

        ImageButton backspace = enterPinView.findViewById(R.id.btnBackspace);
        backspace.setOnClickListener(v -> {
            if (!enteredPin.isEmpty()) {
                enteredPin.remove(enteredPin.size() - 1);
                updatePinDots(pinDots, enteredPin.size());
            }
        });

        updatePinDots(pinDots, 0);
    }

    private void updatePinDots(ImageView[] pinDots, int filledCount) {
        for (int i = 0; i < pinDots.length; i++) {
            if (i < filledCount) {
                pinDots[i].setImageResource(R.drawable.pin_dot_filled);
            } else {
                pinDots[i].setImageResource(R.drawable.pin_dot_unfilled);
            }
        }
    }

    private void verifyPin(List<Integer> enteredPin) {
        StringBuilder enteredPinString = new StringBuilder();
        for (int digit : enteredPin) {
            enteredPinString.append(digit);
        }

        String enteredPinStr = enteredPinString.toString();
        boolean pinExists = PinManager.doesPinExist(this);

        if (pinExists) {
            boolean isCorrect = PinManager.verifyPin(this, enteredPinStr);

            if (isCorrect) {
                Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                enteredPin.clear();
                Toast.makeText(this, "Incorrect PIN. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No PIN found", Toast.LENGTH_SHORT).show();
        }
    }
    private void setupEmergencyExit() {
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitButton.setActivity(LoginActivity.this);
                exitButton.exitApp();
            }
        });
    }

}