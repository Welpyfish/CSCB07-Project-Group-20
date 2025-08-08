package com.group20.cscb07project.emergencyinfo.Medications;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.group20.cscb07project.R;

import java.util.Objects;

public class AddEditMedicationFragment extends Fragment {

    private TextInputEditText medicationNameEditText;
    private TextInputEditText medicationDosageEditText;
    private MedicationManager medicationManager;
    private boolean isEditMode = false;
    private String medicationId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_medication, container, false);

        medicationNameEditText = view.findViewById(R.id.medication_name_edit_text);
        medicationDosageEditText = view.findViewById(R.id.dosage_edit_text);
        MaterialButton saveMedicationButton = view.findViewById(R.id.save_medication_button);
        MaterialButton cancelMedicationButton = view.findViewById(R.id.cancel_medication_button);

        medicationManager = MedicationManager.getInstance();

        // Check if we're in edit mode
        Bundle args = getArguments();
        if (args != null) {
            medicationId = args.getString("medication_id");
            if (medicationId != null) {
                isEditMode = true;
                medicationNameEditText.setText(args.getString("medication_name", ""));
                medicationDosageEditText.setText(args.getString("medication_dosage", ""));
            }
        }

        saveMedicationButton.setOnClickListener(v -> saveMedication());
        cancelMedicationButton.setOnClickListener(v -> goBack());

        return view;
    }

    private void saveMedication() {
        String name = Objects.requireNonNull(medicationNameEditText.getText()).toString().trim();
        String dosage = Objects.requireNonNull(medicationDosageEditText.getText()).toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name)) {
            medicationNameEditText.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(dosage)) {
            medicationDosageEditText.setError("Dosage is required");
            return;
        }

        MedicationItem medication = new MedicationItem();
        medication.setName(name);
        medication.setDosage(dosage);

        if (isEditMode) {
            medication.setId(medicationId);
            medicationManager.updateMedication(medication, new MedicationManager.MedicationCallback() {
                @Override
                public void onSuccess() {
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                Toast.makeText(getContext(), "Medication updated successfully", Toast.LENGTH_SHORT).show();
                                goBack();
                            } catch (Exception e) {
                                // Fragment might be destroyed, just ignore
                            }
                        });
                    }
                }

                @Override
                public void onFailure(String error) {
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                Toast.makeText(getContext(), "Error updating Medication", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                // Fragment might be destroyed, just ignore
                            }
                        });
                    }
                }
            });
        } else {
            medicationManager.addMedication(medication, new MedicationManager.MedicationCallback() {
                @Override
                public void onSuccess() {
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                Toast.makeText(getContext(), "Medication added successfully", Toast.LENGTH_SHORT).show();
                                goBack();
                            } catch (Exception e) {
                                // Fragment might be destroyed, just ignore
                            }
                        });
                    }
                }

                @Override
                public void onFailure(String error) {
                    if (isAdded() && getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            try {
                                Toast.makeText(getContext(), "Error adding Medication", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                // Fragment might be destroyed, just ignore
                            }
                        });
                    }
                }
            });
        }
    }

    private void goBack() {
        try {
            if (isAdded() && getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        } catch (Exception e) {
            // If navigation fails, just ignore
        }
    }
} 