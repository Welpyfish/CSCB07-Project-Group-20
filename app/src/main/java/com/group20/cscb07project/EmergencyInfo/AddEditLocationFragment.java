package com.group20.cscb07project.EmergencyInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.group20.cscb07project.R;

public class AddEditLocationFragment extends Fragment {

    private TextInputEditText addressEditText;
    private TextInputEditText locationNoteEditText;
    private MaterialButton saveLocationButton;
    private MaterialButton cancelLocationButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_location, container, false);

        addressEditText = view.findViewById(R.id.address_edit_text);
        locationNoteEditText = view.findViewById(R.id.location_note_edit_text);
        saveLocationButton = view.findViewById(R.id.save_location_button);
        cancelLocationButton = view.findViewById(R.id.cancel_location_button);

        saveLocationButton.setOnClickListener(v -> {
        });

        cancelLocationButton.setOnClickListener(v -> {
        });

        return view;
    }
} 