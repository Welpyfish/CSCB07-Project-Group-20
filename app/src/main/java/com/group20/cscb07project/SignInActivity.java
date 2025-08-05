package com.group20.cscb07project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class SignInActivity extends AppCompatActivity {

    private TextInputEditText passwordEditText;
    private TextInputLayout passwordLayout;
    private MaterialButton signInButton;

    private String userEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // TODO: Initialize Firebase Auth here

        initializeViews();
        setupClickListeners();
        
        // Get email from intent
        userEmail = getIntent().getStringExtra("email");
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "Email not provided", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        passwordEditText = findViewById(R.id.password_edit_text);
        passwordLayout = findViewById(R.id.password_layout);
        signInButton = findViewById(R.id.sign_in_button);
    }

    private void setupClickListeners() {
        signInButton.setOnClickListener(v -> signIn());
    }

    private void signIn() {
        String password = passwordEditText.getText().toString().trim();

        if (password.isEmpty()) {
            passwordLayout.setError("Password is required");
            return;
        }
        passwordLayout.setError(null);

        signInButton.setEnabled(false);
        signInButton.setText("Signing in...");

        // TODO: Implement Firebase sign in with email and password
        // TODO: Handle success - navigate to MainActivity
        // TODO: Handle failure - show error message
        
        // For now, just show success message and navigate
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: Check if user is already signed in with Firebase
        // TODO: If signed in, navigate to MainActivity
    }
} 