package com.group20.cscb07project.Authorization;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class PinManager {

    private static final String PREFS_NAME = "secure_prefs";
    private static final String PIN_KEY = "PIN";

    private static SharedPreferences getEncryptedPrefs(Context context)
            throws GeneralSecurityException, IOException {

        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        return EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    /** Checks if a PIN exists */
    public static boolean doesPinExist(Context context) {
        try {
            return getEncryptedPrefs(context).contains(PIN_KEY);
        } catch (GeneralSecurityException | IOException e) {
            return false;
        }
    }

    /** Saves a new PIN */
    public static boolean savePin(Context context, String pin) {
        try {
            getEncryptedPrefs(context).edit()
                    .putString(PIN_KEY, pin)
                    .apply();
            return true;
        } catch (GeneralSecurityException | IOException e) {
            return false;
        }
    }

    /** Verifies if entered PIN matches stored one */
    public static boolean verifyPin(Context context, String enteredPin) {
        try {
            SharedPreferences prefs = getEncryptedPrefs(context);
            String storedPin = prefs.getString(PIN_KEY, null);
            return storedPin != null && storedPin.equals(enteredPin);
        } catch (GeneralSecurityException | IOException e) {
            return false;
        }
    }

    /** Removes stored PIN (e.g., for reset) */
    public static boolean clearPin(Context context) {
        try {
            getEncryptedPrefs(context).edit().remove(PIN_KEY).apply();
            return true;
        } catch (GeneralSecurityException | IOException e) {
            return false;
        }
    }
}