package com.group20.cscb07project;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.ClearCredentialException;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executors;

public class FirebaseAuthModel {
    private FirebaseAuth mAuth;
    private CredentialManager credentialManager;


    public FirebaseAuthModel(){
        mAuth = FirebaseAuth.getInstance();
    }

    public boolean signedIn(){
        return mAuth.getCurrentUser()!=null;
    }

    public void createAccountEmailPassword(String email, String password, FirebaseResultCallback callback){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "createUserWithEmail:success");
                            callback.onSuccess();
                        } else {
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            callback.onFailure();
                        }
                    }
                });
    }

    public void signInEmailPassword(String email, String password, FirebaseResultCallback callback){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithEmail:success");
                            callback.onSuccess();
                        } else {
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            callback.onFailure();
                        }
                    }
                });
    }


    public void signInG(Context context, FirebaseResultCallback callback){
        // Instantiate a Google sign-in request
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .build();

// Create the Credential Manager request
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        CredentialManager credentialManager = CredentialManager.create(context);
        credentialManager.getCredentialAsync(context, request, null,
                Executors.newSingleThreadExecutor(),new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                            handleSignInG(result.getCredential(), callback);
                        }
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        callback.onFailure();
                    }
                } );
    }

    private void handleSignInG(Credential credential, FirebaseResultCallback callback) {
        // Check if credential is of type Google ID
        if (credential instanceof CustomCredential) {
            CustomCredential customCredential = (CustomCredential) credential;
            if(credential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
                // Create Google ID Token
                Bundle credentialData = customCredential.getData();
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);

                // Sign in to Firebase with using the token
                firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken(), callback);
            }
        } else {
            Log.w("TAG", "Credential is not of type Google ID!");
            callback.onFailure();
        }
    }

    private void firebaseAuthWithGoogle(String idToken, FirebaseResultCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure();
                    }
                });
    }


    public void signOut(Context context, FirebaseResultCallback callback) {
        // Firebase sign out
        mAuth.signOut();

        // When a user signs out, clear the current user credential state from all credential providers.
        ClearCredentialStateRequest clearRequest = new ClearCredentialStateRequest();
        CredentialManager credentialManager = CredentialManager.create(context);
        credentialManager.clearCredentialStateAsync(
                clearRequest,
                null,
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<Void, ClearCredentialException>() {
                    @Override
                    public void onResult(@NonNull Void result) {
                        callback.onSuccess();
                    }

                    @Override
                    public void onError(@NonNull ClearCredentialException e) {
                        Log.e("TAG", "Couldn't clear user credentials: " + e.getLocalizedMessage());
                        callback.onFailure();
                    }
                });
    }
}