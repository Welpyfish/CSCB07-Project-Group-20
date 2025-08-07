package com.group20.cscb07project.EmergencyInfo.Medications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group20.cscb07project.R;

import java.util.ArrayList;
import java.util.List;

public class MedicationsListFragment extends Fragment implements MedicationAdapter.OnMedicationClickListener {

    private RecyclerView medicationsRecyclerView;
    private FloatingActionButton addMedicationFab;
    private MedicationAdapter medicationAdapter;
    private MedicationManager medicationManager;
    private List<MedicationItem> medicationsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medications_list, container, false);

        medicationsRecyclerView = view.findViewById(R.id.medications_recycler_view);
        addMedicationFab = view.findViewById(R.id.add_medication);

        medicationManager = MedicationManager.getInstance();
        medicationsList = new ArrayList<>();

        medicationAdapter = new MedicationAdapter(medicationsList, this);
        medicationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        medicationsRecyclerView.setAdapter(medicationAdapter);

        loadMedications();

        addMedicationFab.setOnClickListener(v -> {
            AddEditMedicationFragment fragment = new AddEditMedicationFragment();
            loadFragment(fragment);
        });

        return view;
    }

    private void loadMedications() {
        medicationManager.getMedications(new MedicationManager.MedicationListCallback() {
            @Override
            public void onSuccess(List<MedicationItem> medications) {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            medicationsList.clear();
                            medicationsList.addAll(medications);
                            medicationAdapter.updateMedications(medicationsList);
                        } catch (Exception e) {
                            // silent
                        }
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            Toast.makeText(getContext(), "Error loading medications", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // silent
                        }
                    });
                }
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        try {
            if (isAdded() && getParentFragmentManager() != null) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        } catch (Exception e) {
            // silent
        }
    }

    @Override
    public void onEditMedication(MedicationItem medication) {
        try {
            AddEditMedicationFragment fragment = new AddEditMedicationFragment();
            Bundle args = new Bundle();
            args.putString("medication_id", medication.getId());
            args.putString("medication_name", medication.getName());
            args.putString("medication_dosage", medication.getDosage());
            fragment.setArguments(args);
            loadFragment(fragment);
        } catch (Exception e) {
            // silent
        }
    }

    @Override
    public void onDeleteMedication(MedicationItem medication) {
        medicationManager.deleteMedication(medication.getId(), new MedicationManager.MedicationCallback() {
            @Override
            public void onSuccess() {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            Toast.makeText(getContext(), "Medication deleted", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // silent
                        }
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        try {
                            Toast.makeText(getContext(), "Error deleting medication", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // silent
                        }
                    });
                }
            }
        });
    }
}