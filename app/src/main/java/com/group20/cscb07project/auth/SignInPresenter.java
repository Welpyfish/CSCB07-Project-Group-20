package com.group20.cscb07project.auth;

import com.group20.cscb07project.firebase.FirebaseAuthService;
import com.group20.cscb07project.firebase.FirebaseResultCallback;

public class SignInPresenter {

    private final SignInView view;
    private final FirebaseAuthService model;
    private final String email;

    private final boolean pinExists;

    public SignInPresenter(SignInView view, FirebaseAuthService model, String email, Boolean pinExists) {
        this.view = view;
        this.model = model;
        this.email = email;
        this.pinExists = pinExists;
    }

    public void signIn() {
        String password = view.getPassword();

        if (password.isEmpty()) {
            view.showPasswordError("Password is required");
            return;
        }

        view.clearPasswordError();
        view.showProgress();

        model.signIn(email, password, new FirebaseResultCallback() {
            @Override
            public void onSuccess() {
                view.logSuccess();
                view.hideProgress();
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
                view.showAuthFailed("Authentication failed");
            }
        });
    }
}
