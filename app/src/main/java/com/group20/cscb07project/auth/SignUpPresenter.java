package com.group20.cscb07project.auth;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.group20.cscb07project.firebase.FirebaseAuthService;
import com.group20.cscb07project.firebase.FirebaseResultCallback;

public class SignUpPresenter {

    private final SignUpView view;
    private final FirebaseAuthService model;

    private final boolean pinExists;

    public SignUpPresenter(SignUpView view, FirebaseAuthService model, boolean pinExists) {
        this.view = view;
        this.model = model;
        this.pinExists = pinExists;
    }

    public void signUp() {
        String email = view.getEmail().trim();
        String name = view.getName().trim();
        String password = view.getPassword().trim();

        view.clearErrors();

        if (email.isEmpty()) {
            view.showEmailError("Email is required");
            return;
        }
        if (name.isEmpty()) {
            view.showNameError("Name is required");
            return;
        }
        if (password.isEmpty()) {
            view.showPasswordError("Password is required");
            return;
        }
        if (password.length() < 6) {
            view.showPasswordError("Password must be at least 6 characters");
            return;
        }

        view.showProgress();

        model.signUp(email, password, new FirebaseResultCallback() {
            @Override
            public void onSuccess() {
                view.logSuccess();
                view.hideProgress();
                view.showToast("Account created successfully!");
                if(pinExists){
                    view.navigateToMain();
                }else{
                    view.navigateToSetPin();
                }
            }

            @Override
            public void onFailure(Exception e) {
                view.logFailure(e);
                view.hideProgress();

                String errorMessage = "Authentication failed.";
                if (e instanceof FirebaseAuthWeakPasswordException) {
                    errorMessage = "Password is too weak. Please choose a stronger one.";
                    view.showPasswordError(errorMessage);
                } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    errorMessage = "The email address is malformed or invalid.";
                    view.showEmailError(errorMessage);
                } else if (e instanceof FirebaseAuthUserCollisionException) {
                    errorMessage = "An account with this email address already exists.";
                    view.showEmailError(errorMessage);
                } else {
                    errorMessage += " " + e.getMessage();
                }

                view.showToast(errorMessage);
            }
        });
    }
}
