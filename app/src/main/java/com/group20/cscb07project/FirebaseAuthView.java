package com.group20.cscb07project;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthView extends AppCompatActivity {
    private FirebaseAuthPresenter presenter;
    private static final String TAG = "FirebaseActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        presenter = new FirebaseAuthPresenter(this, new FirebaseAuthModel());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

    }

    private void reload(){
        //go to the app
    }

    public void notifyError(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void loginAccepted(){
        //go to main page
    }

    public void reset(){

    }

}
