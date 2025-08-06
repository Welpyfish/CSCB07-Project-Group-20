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

public class AddEditContactFragment extends Fragment {

    private TextInputEditText contactNameEditText;
    private TextInputEditText relationshipEditText;
    private TextInputEditText phoneEditText;
    private MaterialButton saveContactButton;
    private MaterialButton cancelContactButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_contact, container, false);

        contactNameEditText = view.findViewById(R.id.contact_name_edit_text);
        relationshipEditText = view.findViewById(R.id.relationship_edit_text);
        phoneEditText = view.findViewById(R.id.phone_edit_text);
        saveContactButton = view.findViewById(R.id.save_contact_button);
        cancelContactButton = view.findViewById(R.id.cancel_contact_button);

        saveContactButton.setOnClickListener(v -> {
        });

        cancelContactButton.setOnClickListener(v -> {
        });

        return view;
    }
} 