package com.group20.cscb07project.Authorization;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.group20.cscb07project.FloatingExitButton;
import com.group20.cscb07project.MainActivity;
import com.group20.cscb07project.R;

public class SignInActivity extends AppCompatActivity implements SignInView {

    private TextInputEditText passwordEditText;
    private TextInputLayout passwordLayout;
    private MaterialButton signInButton;
    private FloatingExitButton exitButton;

    private SignInPresenter presenter;

    private PinManager pinManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        passwordEditText = findViewById(R.id.password_edit_text);
        passwordLayout = findViewById(R.id.password_layout);
        signInButton = findViewById(R.id.sign_in_button);
        exitButton = findViewById(R.id.exitButton);

        String userEmail = getIntent().getStringExtra("email");
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "Email not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        presenter = new SignInPresenter(this, new FirebaseAuthModel(), userEmail, pinManager.doesPinExist(this));

        signInButton.setOnClickListener(v -> presenter.signIn());

        exitButton.setOnClickListener(view -> {
            exitButton.setActivity(SignInActivity.this);
            exitButton.exitApp();
        });
    }

    // --- SignInView methods ---
    @Override
    public void showProgress() {
        signInButton.setEnabled(false);
        signInButton.setText("Signing in...");
    }

    @Override
    public void hideProgress() {
        signInButton.setEnabled(true);
        signInButton.setText("Sign In");
    }

    @Override
    public void showPasswordError(String message) {
        passwordLayout.setError(message);
    }

    @Override
    public void clearPasswordError() {
        passwordLayout.setError(null);
    }

    @Override
    public void navigateToMain() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToSetPin() {
        Intent intent = new Intent(SignInActivity.this, SetPinActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showAuthFailed(String message) {
        Toast.makeText(SignInActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public String getPassword() {
        return passwordEditText.getText().toString().trim();
    }
}