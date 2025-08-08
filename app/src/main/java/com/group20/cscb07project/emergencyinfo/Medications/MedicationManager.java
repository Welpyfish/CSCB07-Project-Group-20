package com.group20.cscb07project.emergencyinfo.Medications;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MedicationManager {
    private static MedicationManager instance;
    private final DatabaseReference databaseRef;
    private String userId;

    public interface MedicationCallback {
        void onSuccess();
        void onFailure(String error);
    }

    public interface MedicationListCallback {
        void onSuccess(List<MedicationItem> medications);
        void onFailure(String error);
    }

    private MedicationManager() {
        databaseRef = FirebaseDatabase.getInstance("https://cscb07-project-group-20-default-rtdb.firebaseio.com").getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        }
    }

    public static MedicationManager getInstance() {
        if (instance == null) {
            instance = new MedicationManager();
        }
        return instance;
    }

    public void addMedication(MedicationItem medication, MedicationCallback callback) {
        if (userId == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        String medicationId = UUID.randomUUID().toString();
        medication.setId(medicationId);

        Map<String, Object> medicationData = new HashMap<>();
        medicationData.put("name", medication.getName());
        medicationData.put("dosage", medication.getDosage());

        databaseRef.child("users").child(userId).child("emergency_medications").child(medicationId)
                .setValue(medicationData)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void updateMedication(MedicationItem medication, MedicationCallback callback) {
        if (userId == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        Map<String, Object> medicationData = new HashMap<>();
        medicationData.put("name", medication.getName());
        medicationData.put("dosage", medication.getDosage());

        databaseRef.child("users").child(userId).child("emergency_medications").child(medication.getId())
                .setValue(medicationData)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void deleteMedication(String medicationId, MedicationCallback callback) {
        if (userId == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        databaseRef.child("users").child(userId).child("emergency_medications").child(medicationId)
                .removeValue()
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void getMedications(MedicationListCallback callback) {
        if (userId == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        databaseRef.child("users").child(userId).child("emergency_medications")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<MedicationItem> medications = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            MedicationItem medication = snapshot.getValue(MedicationItem.class);
                            if (medication != null) {
                                medication.setId(snapshot.getKey());
                                medications.add(medication);
                            }
                        }
                        callback.onSuccess(medications);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onFailure(databaseError.getMessage());
                    }
                });
    }

}