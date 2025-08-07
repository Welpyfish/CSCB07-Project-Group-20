package com.group20.cscb07project.Authorization;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.group20.cscb07project.MainActivity;
import com.group20.cscb07project.R;


public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText newPasswordEditText;
    private TextInputLayout emailLayout;
    private TextInputLayout nameLayout;
    private TextInputLayout newPasswordLayout;
    private MaterialButton signUpButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
        nameEditText = findViewById(R.id.name_edit_text);
        newPasswordEditText = findViewById(R.id.new_password_edit_text);
        emailLayout = findViewById(R.id.email_layout);
        nameLayout = findViewById(R.id.name_layout);
        newPasswordLayout = findViewById(R.id.new_password_layout);
        signUpButton = findViewById(R.id.sign_in_button); // This is actually the sign up button
    }

    private void setupClickListeners() {
        signUpButton.setOnClickListener(v -> signUp());
    }

    private void signUp() {
        String email = emailEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String password = newPasswordEditText.getText().toString().trim();
        if (email.isEmpty()) {
            emailLayout.setError("Email is required");
            return;
        }
        if (name.isEmpty()) {
            nameLayout.setError("Name is required");
            return;
        }
        if (password.isEmpty()) {
            newPasswordLayout.setError("Password is required");
            return;
        }
        if (password.length() < 6) {
            newPasswordLayout.setError("Password must be at least 6 characters");
            return;
        }
        emailLayout.setError(null);
        nameLayout.setError(null);
        newPasswordLayout.setError(null);

        signUpButton.setEnabled(false);
        signUpButton.setText("Creating account...");

        // TODO: Implement Firebase user creation with email and password
        // TODO: Update user profile with display name
        // TODO: Handle success - navigate to MainActivity
        // TODO: Handle failure - show appropriate error messages
        
        // For now, just show success message and navigate
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
} 