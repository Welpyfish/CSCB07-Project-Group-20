package com.group20.cscb07project.Authorization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.group20.cscb07project.FloatingExitButton;
import com.group20.cscb07project.MainActivity;
import com.group20.cscb07project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.util.Log;


public class SignInActivity extends AppCompatActivity {

    private TextInputEditText passwordEditText;
    private TextInputLayout passwordLayout;
    private MaterialButton signInButton;

    private String userEmail;
    private FirebaseAuth auth;
    FloatingExitButton exitButton;
    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        auth = FirebaseAuth.getInstance();
        initializeViews();
        setupClickListeners();
        exitButton = findViewById(R.id.exitButton);
        setupEmergencyExit();
        
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

        auth.signInWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener(this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<com.google.firebase.auth.AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed",
                                    Toast.LENGTH_LONG).show();
                            signInButton.setEnabled(true);
                            signInButton.setText("Sign In");
                        }
                    }
                });
    }
    private void setupEmergencyExit() {
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitButton.setActivity(SignInActivity.this);
                exitButton.exitApp();
            }
        });
    }
}