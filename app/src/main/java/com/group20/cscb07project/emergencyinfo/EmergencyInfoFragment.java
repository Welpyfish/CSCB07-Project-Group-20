package com.group20.cscb07project.emergencyinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.card.MaterialCardView;
import com.group20.cscb07project.emergencyinfo.Contacts.ContactsListFragment;
import com.group20.cscb07project.emergencyinfo.Locations.LocationsListFragment;
import com.group20.cscb07project.emergencyinfo.Medications.MedicationsListFragment;
import com.group20.cscb07project.R;

public class EmergencyInfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency_info, container, false);

        MaterialCardView contactsCard = view.findViewById(R.id.contacts_card);
        MaterialCardView locationsCard = view.findViewById(R.id.locations_card);
        MaterialCardView medicationsCard = view.findViewById(R.id.medications_card);

        contactsCard.setOnClickListener(v -> loadFragment(new ContactsListFragment()));
        locationsCard.setOnClickListener(v -> loadFragment(new LocationsListFragment())); // TODO: Replace with actual locations fragment
        medicationsCard.setOnClickListener(v -> loadFragment(new MedicationsListFragment())); // TODO: Replace with actual medications fragment

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
} 