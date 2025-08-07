package com.group20.cscb07project.Authorization;


import com.group20.cscb07project.FirebaseResultCallback;

public class FirebaseAuthPresenter {


    private FirebaseAuthModel model;
    private FirebaseAuthView view;


    public FirebaseAuthPresenter(FirebaseAuthView view, FirebaseAuthModel model){
        this.view=view;
        this.model=model;

    }


    public void createAccountEP(String email, String password){
        //validate email/pw first
        model.createAccountEmailPassword(email, password, new FirebaseResultCallback(){
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
        model.signInEmailPassword(email, password, new FirebaseResultCallback() {
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
        model.signInG(view, new FirebaseResultCallback() {
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
        model.signOut(view, new FirebaseResultCallback() {
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
