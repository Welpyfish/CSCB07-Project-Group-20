package com.group20.cscb07project.Authorization;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.group20.cscb07project.R;


public class EmailAuthActivity extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private TextInputLayout emailLayout;
    private MaterialButton signInButton;
    private MaterialButton signUpButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_auth);

        // TODO: Initialize Firebase Auth here

        initializeViews();
        setupClickListeners();
        
        // Pre-fill email
        String email = getIntent().getStringExtra("email");
        if (email != null && !email.isEmpty()) {
            emailEditText.setText(email);
        }
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.email_edit_text);
        emailLayout = findViewById(R.id.email_layout);
        signInButton = findViewById(R.id.sign_in_button);
        signUpButton = findViewById(R.id.sign_up_button);
    }

    private void setupClickListeners() {
        signInButton.setOnClickListener(v -> navigateToSignIn());
        signUpButton.setOnClickListener(v -> navigateToSignUp());
    }

    private void navigateToSignIn() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailLayout.setError("Email is required");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Please enter a valid email");
            return;
        }
        emailLayout.setError(null);

        // Navigate to SignInActivity with email
        Intent intent = new Intent(EmailAuthActivity.this, SignInActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    private void navigateToSignUp() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailLayout.setError("Email is required");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Please enter a valid email");
            return;
        }
        emailLayout.setError(null);

        // Navigate to SignUpActivity with email
        Intent intent = new Intent(EmailAuthActivity.this, SignUpActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: Check if user is already signed in with Firebase
        // TODO: If signed in, navigate to MainActivity
    }
} 