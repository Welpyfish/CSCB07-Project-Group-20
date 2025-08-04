package com.group20.cscb07project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.group20.cscb07project.ChangePinFragment;

public class SettingsFragment extends Fragment {

    private Button changePinButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize views
        changePinButton = view.findViewById(R.id.change_pin_button);


        // Setup click listeners
        changePinButton.setOnClickListener(v -> loadFragment(new ChangePinFragment()));


        return view;
    }


    private void clearAllData() {
        // Clear SharedPreferences
        try {
            MasterKey masterKey = new MasterKey.Builder(requireContext())
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    requireContext(),
                    "safety_plan_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            sharedPreferences.edit().clear().apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Clear Firebase data (if user is authenticated)
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null && activity.getAuth().getCurrentUser() != null) {
            activity.getDatabase().getReference("users")
                    .child(activity.getAuth().getCurrentUser().getUid())
                    .removeValue();
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
} 