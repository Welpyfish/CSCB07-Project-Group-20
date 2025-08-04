package com.group20.cscb07project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class EmergencyInfoFragment extends Fragment {

    private Button documentsButton;
    private Button contactsButton;
    private Button locationsButton;
    private Button medicationsButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency_info, container, false);

        // Initialize views
        documentsButton = view.findViewById(R.id.documents_button);
        contactsButton = view.findViewById(R.id.contacts_button);
        locationsButton = view.findViewById(R.id.locations_button);
        medicationsButton = view.findViewById(R.id.medications_button);

        // Setup click listeners
        contactsButton.setOnClickListener(v -> loadFragment(new EmergencyContactsFragment()));

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
} 