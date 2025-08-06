package com.group20.cscb07project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddEditMedicationFragment extends Fragment {

    private TextInputEditText medicationNameEditText;
    private TextInputEditText dosageEditText;
    private MaterialButton saveMedicationButton;
    private MaterialButton cancelMedicationButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_medication, container, false);

        medicationNameEditText = view.findViewById(R.id.medication_name_edit_text);
        dosageEditText = view.findViewById(R.id.dosage_edit_text);
        saveMedicationButton = view.findViewById(R.id.save_medication_button);
        cancelMedicationButton = view.findViewById(R.id.cancel_medication_button);

        saveMedicationButton.setOnClickListener(v -> {
        });

        cancelMedicationButton.setOnClickListener(v -> {
        });

        return view;
    }
} 