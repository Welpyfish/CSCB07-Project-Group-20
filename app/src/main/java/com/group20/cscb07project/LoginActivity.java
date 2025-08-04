package com.group20.cscb07project;

import static androidx.constraintlayout.widget.ConstraintSet.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.view.View;
import java.util.ArrayList;
import com.google.android.material.button.MaterialButton;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.List;

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
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitButton.setActivity(LoginActivity.this);
                exitButton.exitApp();
            }
        });
        SharedPreferences preferences = getSharedPreferences("projectpreferences", MODE_PRIVATE);
        String pin = preferences.getString("PIN", null);

        // Set up PIN sign-in button - EXACTLY like email button
        MaterialButton pinSignInButton = findViewById(R.id.PINSignInButton);
        pinSignInButton.setOnClickListener(v -> {
            Toast.makeText(this, "PIN BUTTON CLICKED!", Toast.LENGTH_SHORT).show();
            if (pin != null) {
                // PIN exists, navigate to enter PIN fragment
                showEnterPinFragment();
            } else {
                // No PIN exists, navigate to set PIN activity
                Intent intent = new Intent(LoginActivity.this, SetPinActivity.class);
                startActivity(intent);
            }
        });

        // Set up email sign-in button
        MaterialButton emailSignInButton = findViewById(R.id.emailSignInButton);
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

        // Set up Google sign-in button
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

        TextView signout = findViewById(R.id.SignOutTextView);
        signout.setOnClickListener(v -> {
            signOut();
        });
    }
//


    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            SharedPreferences preferences = getSharedPreferences("projectpreferences", MODE_PRIVATE);
            String pin = preferences.getString("PIN", null);
            if (pin==null) {
                Intent intent = new Intent(LoginActivity.this, SetPinActivity.class);
                startActivity(intent);
            } else {
                // PIN exists, navigate to MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void showEnterPinFragment() {
        // Inflate the enter PIN layout
        View enterPinView = getLayoutInflater().inflate(R.layout.fragment_enter_pin, null);
        setContentView(enterPinView);

        // Initialize PIN verification logic
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

        // Set up number button click listeners
        for (int id : buttonIds) {
            Button btn = enterPinView.findViewById(id);
            btn.setOnClickListener(v -> {
                if (enteredPin.size() < PIN_LENGTH) {
                    enteredPin.add(Integer.parseInt(btn.getText().toString()));
                    updatePinDots(pinDots, enteredPin.size());
                    
                    if (enteredPin.size() == PIN_LENGTH) {
                        // Verify PIN
                        verifyPin(enteredPin);
                    }
                }
            });
        }

        // Set up backspace button
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
        SharedPreferences preferences = getSharedPreferences("projectpreferences", MODE_PRIVATE);
        String savedPin = preferences.getString("PIN", null);
        
        if (savedPin != null) {
            StringBuilder enteredPinString = new StringBuilder();
            for (int digit : enteredPin) {
                enteredPinString.append(digit);
            }
            
            if (enteredPinString.toString().equals(savedPin)) {
                // PIN is correct, navigate to MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                // PIN is incorrect, clear and show error
                enteredPin.clear();
                // Reset the PIN dots to show empty state
                ImageView[] pinDots = {
                        findViewById(R.id.pinDot1),
                        findViewById(R.id.pinDot2),
                        findViewById(R.id.pinDot3),
                        findViewById(R.id.pinDot4)
                };
                updatePinDots(pinDots, 0);
                // Show error message
                Toast.makeText(this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void signOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
} 