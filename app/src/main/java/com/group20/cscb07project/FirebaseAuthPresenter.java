package com.group20.cscb07project;


import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthPresenter {


    private FirebaseAuthModel model;
    private FirebaseAuthView view;


    public FirebaseAuthPresenter(FirebaseAuthView view, FirebaseAuthModel model){
        this.view=view;
        this.model=model;

    }


    public void createAccountEP(String email, String password){
        if(!email.contains("@")){
            view.notifyError("invalid email");
            return;
        }
        //validate email/pw first
        model.createAccountEmailPassword(email, password, new AuthResultCallback(){
            @Override
            public void onSuccess() {
                view.loginAccepted();
            }

            @Override
            public void onFailure() {
                view.notifyError("failed to create account");
            }
        });
    }


    public void signInEP(String email, String password) {
        if(!email.contains("@")){
            view.notifyError("invalid email");
            return;
        }
        model.signInEmailPassword(email, password, new AuthResultCallback() {
            @Override
            public void onSuccess() {
                view.loginAccepted();
            }

            @Override
            public void onFailure() {
                view.notifyError("login failed");
            }
        });
    }


    public void signInG() {
        model.signInG(view, new AuthResultCallback() {
            @Override
            public void onSuccess() {
                view.loginAccepted();
            }

            @Override
            public void onFailure() {
                view.notifyError("login failed");
            }
        });
    }


    public void signOut() {
        model.signOut(view, new AuthResultCallback() {
            @Override
            public void onSuccess() {
                view.reset();
            }

            @Override
            public void onFailure() {
                view.notifyError("login failed");
            }
        });
    }
}
