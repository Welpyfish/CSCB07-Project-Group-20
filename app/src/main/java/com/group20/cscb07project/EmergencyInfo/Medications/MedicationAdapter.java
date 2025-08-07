package com.group20.cscb07project.EmergencyInfo.Medications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group20.cscb07project.R;

import java.util.ArrayList;
import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder> {

    private List<MedicationItem> medications;
    private OnMedicationClickListener listener;

    public interface OnMedicationClickListener {
        void onEditMedication(MedicationItem medication);
        void onDeleteMedication(MedicationItem medication);
    }

    public MedicationAdapter(List<MedicationItem> medications, OnMedicationClickListener listener) {
        this.medications = medications != null ? medications : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medication_card, parent, false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        MedicationItem medication = medications.get(position);
        holder.bind(medication);
    }

    @Override
    public int getItemCount() {
        return medications.size();
    }

    public void updateMedications(List<MedicationItem> newMedications) {
        this.medications = newMedications != null ? newMedications : new ArrayList<>();
        notifyDataSetChanged();
    }

    class MedicationViewHolder extends RecyclerView.ViewHolder {
        private TextView medicationName;
        private TextView medicationDosage;
        private ImageButton editButton;
        private ImageButton deleteButton;

        public MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
            medicationName = itemView.findViewById(R.id.medication_name);
            medicationDosage = itemView.findViewById(R.id.medication_dosage);
            editButton = itemView.findViewById(R.id.edit_medication_button);
            deleteButton = itemView.findViewById(R.id.delete_medication_button);
        }

        public void bind(MedicationItem medication) {
            medicationName.setText(medication.getName());
            medicationDosage.setText(medication.getDosage());

            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditMedication(medication);
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteMedication(medication);
                }
            });
        }
    }
}
