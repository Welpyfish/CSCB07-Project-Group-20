package com.group20.cscb07project.EmergencyInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group20.cscb07project.R;

public class MedicationsListFragment extends Fragment {

    private RecyclerView medicationsRecyclerView;
    private FloatingActionButton addMedicationFab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medications_list, container, false);

        medicationsRecyclerView = view.findViewById(R.id.medications_recycler_view);
        addMedicationFab = view.findViewById(R.id.add_medication);

        medicationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // TODO: Set adapter when MedicationAdapter is created

        addMedicationFab.setOnClickListener(v -> {
            // TODO: Navigate to add/edit medication fragment
        });

        return view;
    }
} 