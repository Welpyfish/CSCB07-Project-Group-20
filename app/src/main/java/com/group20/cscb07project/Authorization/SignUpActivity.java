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

public class SignUpActivity extends AppCompatActivity implements SignUpView {

    private TextInputEditText emailEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText newPasswordEditText;
    private TextInputLayout emailLayout;
    private TextInputLayout nameLayout;
    private TextInputLayout newPasswordLayout;
    private MaterialButton signUpButton;
    private SignUpPresenter presenter;
    FloatingExitButton exitButton;

    private PinManager pinManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeViews();

        String email = getIntent().getStringExtra("email");
        if (email != null && !email.isEmpty()) {
            emailEditText.setText(email);
        }

        presenter = new SignUpPresenter(this, new FirebaseAuthModel(), pinManager.doesPinExist(this));

        signUpButton.setOnClickListener(v -> presenter.signUp());

        exitButton.setOnClickListener(view -> {
            exitButton.setActivity(SignUpActivity.this);
            exitButton.exitApp();
        });
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.email_edit_text);
        nameEditText = findViewById(R.id.name_edit_text);
        newPasswordEditText = findViewById(R.id.new_password_edit_text);
        emailLayout = findViewById(R.id.email_layout);
        nameLayout = findViewById(R.id.name_layout);
        newPasswordLayout = findViewById(R.id.new_password_layout);
        signUpButton = findViewById(R.id.sign_up_button);
        exitButton = findViewById(R.id.exitButton);
    }

    // --- SignUpView methods ---
    @Override
    public void showProgress() {
        signUpButton.setEnabled(false);
        signUpButton.setText("Creating account...");
    }

    @Override
    public void hideProgress() {
        signUpButton.setEnabled(true);
        signUpButton.setText("Sign Up");
    }

    @Override
    public void showEmailError(String message) {
        emailLayout.setError(message);
    }

    @Override
    public void showNameError(String message) {
        nameLayout.setError(message);
    }

    @Override
    public void showPasswordError(String message) {
        newPasswordLayout.setError(message);
    }

    @Override
    public void clearErrors() {
        emailLayout.setError(null);
        nameLayout.setError(null);
        newPasswordLayout.setError(null);
    }

    @Override
    public void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void navigateToSetPin() {
        Intent intent = new Intent(SignUpActivity.this, SetPinActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public String getEmail() {
        return emailEditText.getText().toString();
    }

    @Override
    public String getName() {
        return nameEditText.getText().toString();
    }

    @Override
    public String getPassword() {
        return newPasswordEditText.getText().toString();
    }
}