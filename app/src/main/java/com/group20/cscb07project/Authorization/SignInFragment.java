package com.group20.cscb07project.Authorization;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.group20.cscb07project.R;


public class SignInFragment extends Fragment {

    private TextInputEditText passwordEditText;
    private TextInputLayout passwordLayout;
    private MaterialButton signInButton;
    private FirebaseAuthPresenter presenter;

    private String userEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sign_in, container, false);
        initializeViews(view);

        // TODO: Initialize Firebase Auth here

        setupClickListeners();
        
        // Get email from bundle
        Bundle args = getArguments();
        if (args != null) {
            userEmail = args.getString("email");
        }
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(getContext(), "Email not provided", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        return view;
    }

    private void initializeViews(View view) {
        passwordEditText = view.findViewById(R.id.password_edit_text);
        passwordLayout = view.findViewById(R.id.password_layout);
        signInButton = view.findViewById(R.id.sign_in_button);
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

        presenter.signInEP(userEmail, password);

    }


    public void setPresenter(FirebaseAuthPresenter presenter) {
        this.presenter = presenter;
    }
}