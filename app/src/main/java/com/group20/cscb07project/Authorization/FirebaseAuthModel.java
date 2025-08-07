package com.group20.cscb07project.Authorization;

import com.group20.cscb07project.Authorization.FirebaseResultCallback;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthModel {

    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthModel() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signIn(String email, String password, FirebaseResultCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    public void signUp(String email, String password, FirebaseResultCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }
}
