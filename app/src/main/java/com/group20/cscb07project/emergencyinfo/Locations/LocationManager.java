package com.group20.cscb07project.emergencyinfo.Locations;

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

public class LocationManager {
    private static LocationManager instance;
    private final DatabaseReference databaseRef;
    private String userId;

    public interface LocationCallback {
        void onSuccess();
        void onFailure(String error);
    }

    public interface LocationListCallback {
        void onSuccess(List<LocationItem> locations);
        void onFailure(String error);
    }

    private LocationManager() {
        databaseRef = FirebaseDatabase.getInstance("https://cscb07-project-group-20-default-rtdb.firebaseio.com").getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        }
    }

    public static LocationManager getInstance() {
        if (instance == null) {
            instance = new LocationManager();
        }
        return instance;
    }

    public void addLocation(LocationItem location, LocationCallback callback) {
        if (userId == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        String locationId = UUID.randomUUID().toString();
        location.setId(locationId);

        Map<String, Object> locationData = new HashMap<>();
        locationData.put("address", location.getAddress());
        locationData.put("note", location.getNote());

        databaseRef.child("users").child(userId).child("emergency_locations").child(locationId)
                .setValue(locationData)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void updateLocation(LocationItem location, LocationCallback callback) {
        if (userId == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        Map<String, Object> locationData = new HashMap<>();
        locationData.put("address", location.getAddress());
        locationData.put("note", location.getNote());

        databaseRef.child("users").child(userId).child("emergency_locations").child(location.getId())
                .setValue(locationData)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void deleteLocation(String locationId, LocationCallback callback) {
        if (userId == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        databaseRef.child("users").child(userId).child("emergency_locations").child(locationId)
                .removeValue()
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void getLocations(LocationListCallback callback) {
        if (userId == null) {
            callback.onFailure("User not authenticated");
            return;
        }

        databaseRef.child("users").child(userId).child("emergency_locations")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<LocationItem> locations = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            LocationItem location = snapshot.getValue(LocationItem.class);
                            if (location != null) {
                                location.setId(snapshot.getKey());
                                locations.add(location);
                            }
                        }
                        callback.onSuccess(locations);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onFailure(databaseError.getMessage());
                    }
                });
    }

}