package com.group20.cscb07project.EmergencyInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.card.MaterialCardView;
import com.group20.cscb07project.EmergencyInfo.Contacts.ContactsListFragment;
import com.group20.cscb07project.EmergencyInfo.Locations.LocationsListFragment;
import com.group20.cscb07project.EmergencyInfo.Medications.MedicationsListFragment;
import com.group20.cscb07project.R;

public class EmergencyInfoFragment extends Fragment {

    private MaterialCardView contactsCard;
    private MaterialCardView locationsCard;
    private MaterialCardView medicationsCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency_info, container, false);

        contactsCard = view.findViewById(R.id.contacts_card);
        locationsCard = view.findViewById(R.id.locations_card);
        medicationsCard = view.findViewById(R.id.medications_card);

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