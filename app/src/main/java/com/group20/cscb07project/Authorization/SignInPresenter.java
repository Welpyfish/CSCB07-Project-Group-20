package com.group20.cscb07project.Authorization;

import android.text.TextUtils;
import android.util.Log;
import com.group20.cscb07project.Authorization.PinManager;

import com.group20.cscb07project.Authorization.FirebaseResultCallback;

public class SignInPresenter {

    private final SignInView view;
    private final FirebaseAuthModel model;
    private final String email;

    private boolean pinExists;

    public SignInPresenter(SignInView view, FirebaseAuthModel model, String email, Boolean pinExists) {
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
