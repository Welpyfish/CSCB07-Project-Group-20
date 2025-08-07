package com.group20.cscb07project.Authorization;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.group20.cscb07project.MainActivity;
import com.group20.cscb07project.R;


public class SignUpFragment extends Fragment {

    private TextInputEditText emailEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText newPasswordEditText;
    private TextInputLayout emailLayout;
    private TextInputLayout nameLayout;
    private TextInputLayout newPasswordLayout;
    private MaterialButton signUpButton;

    private FirebaseAuthPresenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sign_up, container, false);
        initializeViews(view);

        // TODO: Initialize Firebase Auth here

        initializeViews(view);
        setupClickListeners();

        // Pre-fill email
        Bundle args = getArguments();
        if (args != null) {
            String email = args.getString("email");
            if (email != null && !email.isEmpty()) {
                emailEditText.setText(email);
            }

        }
        return view;
    }

    private void initializeViews(View view) {
        emailEditText = view.findViewById(R.id.email_edit_text);
        nameEditText = view.findViewById(R.id.name_edit_text);
        newPasswordEditText = view.findViewById(R.id.new_password_edit_text);
        emailLayout = view.findViewById(R.id.email_layout);
        nameLayout = view.findViewById(R.id.name_layout);
        newPasswordLayout = view.findViewById(R.id.new_password_layout);
        signUpButton = view.findViewById(R.id.sign_in_button); // This is actually the sign up button
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

        presenter.createAccountEP(email, password);

    }

    public void setPresenter(FirebaseAuthPresenter presenter) {
        this.presenter = presenter;
    }
}