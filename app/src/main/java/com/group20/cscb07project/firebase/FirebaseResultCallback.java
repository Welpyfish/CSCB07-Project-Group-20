package com.group20.cscb07project.firebase;

public interface FirebaseResultCallback {
    void onSuccess();
    void onFailure(Exception e);
}
