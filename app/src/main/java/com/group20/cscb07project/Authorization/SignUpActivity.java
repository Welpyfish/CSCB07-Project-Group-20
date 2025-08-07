package com.group20.cscb07project.Authorization;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.group20.cscb07project.FloatingExitButton;
import com.group20.cscb07project.MainActivity;
import com.group20.cscb07project.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText newPasswordEditText;
    private TextInputLayout emailLayout;
    private TextInputLayout nameLayout;
    private TextInputLayout newPasswordLayout;
    private MaterialButton signUpButton;
    private FirebaseAuth auth;
    FloatingExitButton exitButton;
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        initializeViews();
        setupClickListeners();
        exitButton = findViewById(R.id.exitButton);
        setupEmergencyExit();
        
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
        signUpButton = findViewById(R.id.sign_up_button);
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

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignUpActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            String errorMessage = "Authentication failed.";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                errorMessage = "Password is too weak. Please choose a stronger one.";
                                newPasswordLayout.setError(errorMessage);
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                errorMessage = "The email address is malformed or invalid.";
                                emailLayout.setError(errorMessage);
                            } catch (FirebaseAuthUserCollisionException e) {
                                errorMessage = "An account with this email address already exists.";
                                emailLayout.setError(errorMessage);
                            } catch (Exception e) {
                                errorMessage += " " + e.getMessage();
                            }
                            Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_LONG).show();

                            signUpButton.setEnabled(true);
                            signUpButton.setText("Sign Up");
                        }
                    }
                });
    }
    private void setupEmergencyExit() {
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitButton.setActivity(SignUpActivity.this);
                exitButton.exitApp();
            }
        });
    }
}