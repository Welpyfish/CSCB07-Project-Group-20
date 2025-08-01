package com.group20.cscb07project;

import static androidx.constraintlayout.widget.ConstraintSet.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    // See: https://developer.android.com/training/basics/intents/result

    private boolean existsPin;
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        existsPin = PinManager.doesPinExist(this);

        FloatingExitButton exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitButton.setActivity(LoginActivity.this);
                exitButton.exitApp();
            }
        });

        if (existsPin) {
            TextView pinLogin = findViewById(R.id.PinTextView);
            pinLogin.setVisibility(View.VISIBLE);
            pinLogin.setOnClickListener(v -> {
                    startActivity(new Intent(this, SetPinActivity.class));
            });
        }


        //this goes to the login flow. IDK IF FIREBASEUI COUNTS OR NOT FOR MVP
        TextView firebaseLogin = findViewById(R.id.FirebaseTextView);

        firebaseLogin.setOnClickListener(v -> {
            //this might all be useless since its not really mvp
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);

            //maybe this works
//            Intent intent = new Intent(LoginActivity.this, FirebaseActivity.class);
//            startActivity(intent);

        });

        TextView signout = findViewById(R.id.SignOutTextView);

        signout.setOnClickListener(v -> {
            signOut();
        });
    }
//







    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            if (!existsPin){
                Intent intent = new Intent(LoginActivity.this, SetPinActivity.class);
                startActivity(intent);
            }


            //go to main page
            // ...

        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }

    private void signOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
} 